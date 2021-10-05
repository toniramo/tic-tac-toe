package tictactoe.ui;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
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
    private final int windowHeight = 700;
    private final int windowWidth = 700;

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
        HBox topMenuLayout = new HBox(7);
        topMenuLayout.alignmentProperty().set(Pos.CENTER_LEFT);
        topMenuLayout.setPadding(new Insets(5));

        Button newGameButton = createButton("New game", Color.LIGHTGREY, false);
        Button backToStart = createButton("Back to main menu", Color.LIGHTGREY, false);
        Button exitButton = createButton("Exit", Color.GREY, false);
        exitButton.setStyle("-fx-text-fill: white");

        Label turnLabel = new Label("Turn: " + gameService.getCurrentPlayer().getMark());
        turnLabel.fontProperty().set(Font.font("Helvetica", FontWeight.THIN, 20));
        turnLabel.setPadding(new Insets(10, 10, 10, 10));

        this.gameBoard = new GridPane();

        topMenuLayout.getChildren().addAll(newGameButton, backToStart, exitButton, turnLabel);

        int n = gameService.getRules().getBoardsize();

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                StackPane stack = new StackPane();

                Rectangle tile = new Rectangle(40, 40);
                tile.setFill(Color.WHITE);
                tile.setStroke(Color.GREY.brighter());

                stack.getChildren().add(tile);
                gameBoard.add(stack, i, j);

                stack.setOnMouseClicked((var event) -> {
                    int x = GridPane.getColumnIndex(stack);
                    int y = GridPane.getRowIndex(stack);

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
        mark.setFont(Font.font("Helvetica", FontWeight.BOLD, 25));
        mark.setText(playerBeforeMove.getMark());
        mark.setTextFill(playerBeforeMove.getMarkColor());
        tile.getChildren().add(mark);
        gameService.makeMove(x, y);
        turnLabel.setText("Turn: " + gameService.getCurrentPlayer().getMark());
        turn.set(gameService.getTurn());
    }

    private Scene setUpStartUpScene() {
        BorderPane layout = new BorderPane();
        setBackground(layout);
        layout.setMinSize(windowWidth, windowHeight);
        VBox menu = new VBox(10);
        menu.setMaxSize(windowWidth / 2.5, windowHeight / 1.8);
        setBackgroundFill(menu, new Color(1, 1, 1, 0.95), 20, -40);

        DropShadow shadow = new DropShadow();
        shadow.spreadProperty().set(0.5);
        shadow.setColor(new Color(0.5, 0.5, 0.5, 0.5));
        menu.setEffect(shadow);

        Label title = new Label("Tic-tac-toe");
        title.setFont(Font.font("Helvetica", FontWeight.BOLD, 32));
        Label subtitle = new Label("5-in-row variant");

        Region r1 = new Region();
        r1.setMinHeight(20);

        Label info = new Label("Choose game mode:");

        Button humanHuman = createButton("Human vs. human", Color.LIGHTGREY, true);
        Button humanAi = createButton("Human vs. AI", Color.LIGHTGREY, true);
        Button aiHuman = createButton("AI vs. human", Color.LIGHTGREY, true);
        Button aiAi = createButton("AI vs. AI\n(choose 1st move)", Color.LIGHTGREY, true);

        Region r2 = new Region();
        r2.setMinHeight(20);

        Button exit = createButton("Exit", Color.GREY, true);
        exit.setStyle("-fx-text-fill: white");

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

        menu.setAlignment(Pos.CENTER);
        menu.getChildren().addAll(title, subtitle, r1, info, humanHuman, humanAi, aiHuman, aiAi, r2, exit);
        layout.setCenter(menu);
        return new Scene(layout);
    }

    private void setUpGameModeButton(Button button, RuleBook rules) {
        button.setOnAction((ActionEvent event) -> {
            play(rules);
        });

    }

    private Button createButton(String message, Color color, boolean useMaximumWidth) {

        Button button = new Button(message);

        if (useMaximumWidth) {
            button.setMaxWidth(Double.MAX_VALUE);
        }

        button.setMinHeight(50);

        BackgroundFill buttonFill = new BackgroundFill(color, new CornerRadii(10), Insets.EMPTY);
        BackgroundFill buttonPressedFill = new BackgroundFill(color.darker(), new CornerRadii(10), Insets.EMPTY);
        BackgroundFill mouseOverFill = new BackgroundFill(color.brighter(), new CornerRadii(10), Insets.EMPTY);

        button.setBackground(new Background(buttonFill));
        button.setOnMousePressed(e -> button.setBackground(new Background(buttonPressedFill)));
        button.setOnMouseReleased(e -> button.setBackground(new Background(buttonFill)));
        button.setOnMouseEntered(e -> button.setBackground(new Background(mouseOverFill)));
        button.setOnMouseExited(e -> button.setBackground(new Background(buttonFill)));

        DropShadow shadow = new DropShadow();
        shadow.radiusProperty().set(2);
        shadow.spreadProperty().set(0.5);
        shadow.setColor(new Color(0, 0, 0, 0.3));

        button.setEffect(shadow);

        button.textAlignmentProperty().set(TextAlignment.CENTER);
        return button;
    }

    private void setBackground(Pane pane) {
        BackgroundImage backgroundImage = new BackgroundImage(new Image("file:../documentation/images/background.png", windowWidth / 1.6, 0, true, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

        pane.setBackground(new Background(backgroundImage));

    }

    private void setBackgroundFill(Pane pane, Color color, int cornerRadii, int inset) {
        BackgroundFill bf = new BackgroundFill(
                color,
                new CornerRadii(cornerRadii), new Insets(inset));
        pane.setBackground(new Background(bf));
    }

}
