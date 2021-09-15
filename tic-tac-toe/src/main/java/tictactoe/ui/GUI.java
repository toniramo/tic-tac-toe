/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author toniramo
 */
public class GUI extends Application {

    private int n = 20; // Create NxN board;
    private int[][] memory = new int[n + 1][n + 1];
    private int turn = 1;
    //private int CELL_SIZE = 20;

    @Override
    public void start(Stage stage) {
        VBox layout = new VBox();
        HBox topMenuLayout = new HBox();

        Button newGameButton = new Button("New game");
        Button exitButton = new Button("Exit");

        Label turnLabel = new Label("Turn: X");
        turnLabel.setPadding(new Insets(10, 100, 10, 10));

        GridPane gameBoard = new GridPane();

        topMenuLayout.getChildren().addAll(turnLabel, newGameButton, exitButton);

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                StackPane stack = new StackPane();

                Rectangle tile = new Rectangle(40, 40);
                tile.setFill(Color.WHITE);
                tile.setStroke(Color.GRAY);

                stack.getChildren().add(tile);
                gameBoard.add(stack, i, j);

                tile.setOnMouseClicked(event -> {
                    //if (memory[tile.][tile.getY()]=0) {

                    //}
                    int x = GridPane.getColumnIndex(stack);
                    int y = GridPane.getRowIndex(stack);

                    System.out.println("x:" + x + " y:" + y);

                    if (memory[y][x] == 0) {
                        System.out.println("Click");
                        memory[y][x] = turn;
                        Label mark = new Label();
                        mark.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
                        if (turn == 1) {
                            turn++;
                            turnLabel.setText("Turn: O");
                            mark.setText("X");
                            mark.setTextFill(Color.STEELBLUE);
                        } else if (turn == 2) {
                            turn--;
                            turnLabel.setText("Turn: X");
                            mark.setText("O");
                            mark.setTextFill(Color.TOMATO);
                        }
                        stack.getChildren().add(mark);
                    } else {
                        System.out.println("tile reserved");
                    }
                });

            }
        }

        newGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //memory = new int[n+1][n+1];
                
                
            }
        });

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                stage.close();
            }
        });

        //EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
        layout.getChildren().addAll(topMenuLayout, gameBoard);
        Scene scene = new Scene(layout);

        stage.setScene(scene);
        stage.show();

    }
    
    private void createGameBoard() {
        
    }
}
