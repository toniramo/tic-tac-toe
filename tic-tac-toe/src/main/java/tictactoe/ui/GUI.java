package tictactoe.ui;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import tictactoe.ai.AI;
import tictactoe.dao.InMemoryDao;
import tictactoe.logic.GameService;
import tictactoe.logic.Move;
import tictactoe.logic.Player;
import tictactoe.logic.Player.PlayerType;
import tictactoe.logic.RuleBook;

/**
 * Graphical user interface for tic-tac-toe.
 */
public class GUI extends Application {

    private GameService gameService;
    private SimpleIntegerProperty turn;
    private GridPane gameBoard;
    private AI[] ais;
    private Stage stage;

    @Override
    public void init() {
        this.gameService = new GameService(new InMemoryDao());
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setScene(setUpStartUpScene());
        this.stage.show();
    }

    public void play(RuleBook rules) {
        this.gameService.startNewGame(rules);
        this.ais = new AI[2];
        for (int i = 0; i < this.ais.length; i++) {
            Player player = rules.getPlayerBasedOnTurn(i);
            if (player.getPlayerType() == PlayerType.AI) {
                ais[i] = new AI(this.gameService, player);
            }
        }

        VBox layout = new VBox();
        HBox topMenuLayout = new HBox();

        Button newGameButton = new Button("New game");
        Button backToStart = new Button("Back to main menu");
        Button exitButton = new Button("Exit");

        Label turnLabel = new Label("Turn: " + gameService.getCurrentPlayer().getMark());
        turnLabel.setPadding(new Insets(10, 10, 10, 10));

        this.gameBoard = new GridPane();

        topMenuLayout.getChildren().addAll(newGameButton, backToStart, exitButton, turnLabel);

        int n = gameService.getRules().getBoardsize();

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                StackPane stack = new StackPane();

                Rectangle tile = new Rectangle(40, 40);
                tile.setFill(Color.WHITE);
                tile.setStroke(Color.GRAY);

                stack.getChildren().add(tile);
                gameBoard.add(stack, i, j);

                stack.setOnMouseClicked((var event) -> {
                    int x = GridPane.getColumnIndex(stack);
                    int y = GridPane.getRowIndex(stack);

                    Label mark = new Label();
                    mark.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 25));
                    if (gameService.validMove(x, y)) {
                        makeMove(x, y, stack, turnLabel);
                    }
                    stack.setOnMouseClicked(null);
                });
            }
        }
        turn = new SimpleIntegerProperty(-1);
        turn.addListener((var ov, var oldValue, var newValue) -> {
            if (gameService.gameOver()) {
                String gameOverText;
                Player winner = gameService.getWinningPlayer();
                if (gameService.getWinningPlayer() != null) {
                    gameOverText = " Winner: " + winner.getMark();
                } else {
                    gameOverText = " Draw";
                }
                turnLabel.setText("Game over - " + gameOverText);
                //Turn off actions on game board and show winning row.
                Move[] row = gameService.getWinningRow();
                gameBoard.getChildren().forEach((node) -> {
                    node.setOnMouseClicked(null);
                    for (int i = 0; i < rules.getMarksToWin(); i++) {
                        if (GridPane.getColumnIndex(node) == row[i].getX()
                                && GridPane.getRowIndex(node) == row[i].getY()) {
                            Color color = gameService.getGameBoard().getLastMove().getPlayer().getMarkColor();
                            double r = color.getRed();
                            double g = color.getGreen();
                            double b = color.getBlue();
                            ((Rectangle) ((StackPane) node).getChildren().get(0)).setFill(new Color(r, g, b, 0.2));
                        }
                    }
                });

            } else if (gameService.getCurrentPlayer().getPlayerType().equals(PlayerType.AI)) {
                boolean aiVsAi = (ais[0] != null && ais[1] != null);
                if (gameService.getGameBoard().getNumberOfPlayedMoves() == 1 && aiVsAi) {
                    //update first AI player if aiVsAi game and user places first move.
                    ais[0].updateStateBasedOnLastMove();
                }
                Move move = ais[gameService.getTurn()].chooseMove();
                for (Node node : gameBoard.getChildren()) {
                    if (GridPane.getColumnIndex(node) == move.getX()
                            && GridPane.getRowIndex(node) == move.getY()) {
                        makeMove(move.getX(), move.getY(), (StackPane) node, turnLabel);
                    }
                }
            }
        });

        boolean aiVsAi = (ais[0] != null && ais[1] != null);
        //Let user choose first move in AIvsAI.
        if (!aiVsAi) {
            turn.set(gameService.getTurn());
        }

        newGameButton.setOnAction((ActionEvent event) -> {
            this.play(rules);
        });

        backToStart.setOnAction((ActionEvent event) -> {
            this.start(stage);
        });

        exitButton.setOnAction((ActionEvent event) -> {
            stage.close();
        });

        layout.getChildren().addAll(topMenuLayout, gameBoard);
        Scene scene = new Scene(layout);

        stage.setScene(scene);

        stage.show();
    }

    private void makeMove(int x, int y, StackPane tile, Label turnLabel) {
        Player playerBeforeMove = gameService.getCurrentPlayer();
        Label mark = new Label();
        mark.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 25));
        mark.setText(playerBeforeMove.getMark());
        mark.setTextFill(playerBeforeMove.getMarkColor());
        tile.getChildren().add(mark);
        gameService.makeMove(x, y);
        turnLabel.setText("Turn: " + gameService.getCurrentPlayer().getMark());
        turn.set(gameService.getTurn());
    }

    private Scene setUpStartUpScene() {
        BorderPane layout = new BorderPane();
        VBox menu = new VBox();

        Label title = new Label("Tic-tac-toe");
        Label subtitle = new Label("5-in-row variant\n");
        Label info = new Label("Choose game mode:");

        Button humanHuman = new Button("Human vs. human");
        Button humanAi = new Button("Human vs. AI");
        Button aiHuman = new Button("AI vs. human");
        Button aiAi = new Button("AI vs. AI\n(choose 1st move)");
        Button exit = new Button("Exit");

        Player humanPlayer1 = new Player("X", Color.TOMATO, PlayerType.HUMAN);
        Player humanPlayer2 = new Player("O", Color.STEELBLUE, PlayerType.HUMAN);
        Player aiPlayer1 = new Player("X", Color.TOMATO, PlayerType.AI);
        Player aiPlayer2 = new Player("O", Color.STEELBLUE, PlayerType.AI);

        setUpGameModeButton(humanHuman, new RuleBook(20, 5, new Player[]{humanPlayer1, humanPlayer2}));
        setUpGameModeButton(humanAi, new RuleBook(20, 5, new Player[]{humanPlayer1, aiPlayer2}));
        setUpGameModeButton(aiHuman, new RuleBook(20, 5, new Player[]{aiPlayer1, humanPlayer2}));
        setUpGameModeButton(aiAi, new RuleBook(20, 5, new Player[]{aiPlayer1, aiPlayer2}));

        exit.setOnAction((ActionEvent event) -> {
            stage.close();
        });

        menu.getChildren().addAll(title, subtitle, info, humanHuman, humanAi, aiHuman, aiAi, exit);
        layout.setCenter(menu);
        return new Scene(layout);
    }

    private void setUpGameModeButton(Button button, RuleBook rules) {
        button.setOnAction((ActionEvent event) -> {
            play(rules);
        });

    }

}
