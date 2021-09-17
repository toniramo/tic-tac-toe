/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.domain;

import javafx.scene.paint.Color;
import tictactoe.dao.Dao;

/**
 *
 * @author toniramo
 */
public class GameService {

    private Dao gameData;

    public GameService(Dao dao) {
        this.gameData = dao;
    }

    public void startNewGame(int gameBoardSize, int marksToWin) {
        this.gameData.initializeGameBoard(gameBoardSize, marksToWin);
        this.gameData.addPlayer(new Player("X", Color.STEELBLUE));
        this.gameData.addPlayer(new Player("O", Color.TOMATO));
        //Notice that one could add even more players if wish.
    }

    public int getGameBoardSize() {
        return gameData.getGameBoardSize();
    }

    private void changeTurn() {
        gameData.changeTurn();
    }

    public Player getCurrentPlayer() {
        return gameData.getCurrentPlayer();
    }

    public boolean validMove(int x, int y) {
        return gameData.getMove(x, y) == null;
    }

    //Notice that also UI needs to call same methods as makeMove
    //in order to know if move was valid or not and if game continues
    //after this. This could be improved by communicating the game state
    //to ui after makeMove() is called delivering the same info
    //I.e., was move valid? is game over? Was it a draw 
    //or did player 1 or 2 win?
    public void makeMove(int x, int y) {
        if (this.validMove(x, y)) {
            gameData.setMove(new Move(this.getCurrentPlayer(), x, y));
            if (!this.gameOver()) {
                this.changeTurn();
            }
        }
    }

    //This method violates checkstyle rule of max 20 lines (->split?).
    //Additionally, efficiency of the method could be improved
    //by limiting the checks focus only area with k-radius from the latest move.
    //Works good enough for now.
    public boolean currentPlayerWin() {
        int gameBoardSize = gameData.getGameBoardSize();

        for (int i = 1; i <= gameBoardSize; i++) {
            int markCountRow = 0;
            int markCountCol = 0;
            int markCountDiag1 = 0;
            int markCountDiag2 = 0;
            int diagOffset = 0;

            for (int j = 1; j <= gameBoardSize; j++) {
                Move moveRow = gameData.getMove(i, j);
                Move moveCol = gameData.getMove(j, i);
                markCountRow = updateMarkCounter(markCountRow, moveRow);
                markCountCol = updateMarkCounter(markCountCol, moveCol);

                if (i + diagOffset <= gameBoardSize) {
                    Move moveDiag1 = gameData.getMove(i + diagOffset, j);
                    markCountDiag1 = updateMarkCounter(markCountDiag1, moveDiag1);
                }
                if (i - diagOffset > 0) {
                    Move moveDiag2 = gameData.getMove(j, i - diagOffset);
                    markCountDiag2 = updateMarkCounter(markCountDiag2, moveDiag2);
                }
                diagOffset++;

                if (markCountRow >= 5 || markCountCol >= 5
                        || markCountDiag1 >= 5 || markCountDiag2 >= 5) {
                    return true;
                }
            }
        }
        return false;
    }

    private int updateMarkCounter(int currentValue, Move move) {
        if (move != null && move.getPlayer().equals(this.getCurrentPlayer())) {
            return currentValue + 1;
        }
        return 0;
    }

    private boolean gameBoardFull() {
        return gameData.getNumberOfMoves() == Math.pow(gameData.getGameBoardSize(), 2);
    }

    public boolean gameOver() {
        if (this.currentPlayerWin()) {
            return true;
        }
        return this.gameBoardFull();
    }
}
