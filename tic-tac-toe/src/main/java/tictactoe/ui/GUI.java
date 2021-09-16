/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.ui;

import javafx.application.Application;
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
import tictactoe.dao.InMemoryDao;
import tictactoe.domain.GameService;
import tictactoe.domain.Player;

/**
 *
 * @author toniramo
 */
public class GUI extends Application {

    private GameService gameService;

    @Override
    public void init() {
        this.gameService = new GameService(new InMemoryDao());
        this.gameService.startNewGame(20, 5);
    }

    @Override
    public void start(Stage stage) {
        VBox layout = new VBox();
        HBox topMenuLayout = new HBox();

        Button newGameButton = new Button("New game");
        Button exitButton = new Button("Exit");

        Label turnLabel = new Label("Turn: X");
        turnLabel.setPadding(new Insets(10, 10, 10, 10));

        GridPane gameBoard = new GridPane();

        topMenuLayout.getChildren().addAll(newGameButton, exitButton, turnLabel);

        int n = gameService.getGameBoardSize();

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                StackPane stack = new StackPane();

                Rectangle tile = new Rectangle(40, 40);
                tile.setFill(Color.WHITE);
                tile.setStroke(Color.GRAY);

                stack.getChildren().add(tile);
                gameBoard.add(stack, i, j);

                stack.setOnMouseClicked(event -> {
                    int x = GridPane.getColumnIndex(stack);
                    int y = GridPane.getRowIndex(stack);

                    Player player = gameService.getCurrentPlayer();
                    Label mark = new Label();
                    mark.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 25));
                    if (gameService.validMove(x, y)) {
                        gameService.makeMove(x, y);
                        mark.setText(player.getMark());
                        mark.setTextFill(player.getMarkColor());
                        turnLabel.setText("Turn: " + gameService.getCurrentPlayer().getMark());
                        stack.getChildren().add(mark);
                    }

                    if (gameService.gameOver()) {
                        String winner;

                        if (gameService.currentPlayerWin()) {
                            winner = "Winner: " + gameService.getCurrentPlayer().getMark();
                        } else {
                            winner = "Draw";
                        }

                        turnLabel.setText("Game over. " + winner);
                        //Turn of actions on game board.
                        gameBoard.getChildren().forEach((node) -> {
                            node.setOnMouseClicked(null);
                        });
                    }
                });
            }
        }

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
}
