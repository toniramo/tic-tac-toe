package tictactoe.ui;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    //private AI ai;
    private SimpleIntegerProperty turn;
    private GridPane gameBoard;
    private AI[] ais;

    @Override
    public void init() {
        /* 
         * TODO: Choose game configuration via UI.
         * At the moment can be done from here by passing chosen RuleBook to gameService
         */
        Player humanPlayer1 = new Player("X", Color.TOMATO, PlayerType.HUMAN);
        Player humanPlayer2 = new Player("O", Color.STEELBLUE, PlayerType.HUMAN);
        Player aiPlayer1 = new Player("X", Color.TOMATO, PlayerType.AI);
        Player aiPlayer2 = new Player("O", Color.STEELBLUE, PlayerType.AI);

        /*
         * Choose one of the following: 
         */
        RuleBook humanAndAI = new RuleBook(20, 5, new Player[]{humanPlayer1, aiPlayer2});
        RuleBook AIandHuman = new RuleBook(20, 5, new Player[]{aiPlayer1, humanPlayer2});
        RuleBook onlyAIs = new RuleBook(20, 5, new Player[]{aiPlayer1, aiPlayer2});

        this.gameService = new GameService(new InMemoryDao());
        this.gameService.startNewGame(AIandHuman); //<-- ...and use is as agrument.
        this.ais = new AI[]{new AI(gameService, aiPlayer1), new AI(gameService, aiPlayer2)};
    }

    @Override
    public void start(Stage stage) {
        VBox layout = new VBox();
        HBox topMenuLayout = new HBox();

        Button newGameButton = new Button("New game");
        Button exitButton = new Button("Exit");

        Label turnLabel = new Label("Turn: " + gameService.getCurrentPlayer().getMark());
        turnLabel.setPadding(new Insets(10, 10, 10, 10));

        this.gameBoard = new GridPane();

        topMenuLayout.getChildren().addAll(newGameButton, exitButton, turnLabel);

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
                //Turn off actions on game board.
                gameBoard.getChildren().forEach((node) -> {
                    node.setOnMouseClicked(null);
                });
            } else if (gameService.getCurrentPlayer().getPlayerType().equals(PlayerType.AI)) {
                Move move;
                if (gameService.getCurrentPlayer().equals(ais[0])) {
                    move = ais[0].chooseMove();
                } else {
                    move = ais[1].chooseMove();
                }
                for (Node node : gameBoard.getChildren()) {
                    if (GridPane.getColumnIndex(node) == move.getX()
                            && GridPane.getRowIndex(node) == move.getY()) {
                        makeMove(move.getX(), move.getY(), (StackPane) node, turnLabel);
                    }
                }
            }
        });
        turn.set(gameService.getTurn());

        newGameButton.setOnAction((ActionEvent event) -> {
            this.init();
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
}
