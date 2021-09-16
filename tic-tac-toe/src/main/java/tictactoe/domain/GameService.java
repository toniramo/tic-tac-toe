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
        Player[] players = new Player[2];
        players[0] = new Player("X", Color.STEELBLUE);
        players[1] = new Player("O", Color.TOMATO);

        this.gameData.setPlayers(players);
        this.gameData.setTurn(0);
    }

    public int getGameBoardSize() {
        return gameData.getGameBoardSize();
    }

    private void setTurn(int playerIndex) {
        gameData.setTurn(playerIndex);
    }

    private void changeTurn() {
        int indexOfNextPlayer = (gameData.getTurn() + 1) % 2;
        this.setTurn(indexOfNextPlayer);
    }

    public Player getCurrentPlayer() {
        return gameData.getPlayers()[gameData.getTurn()];
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

    public Player getWinner() {
        int gameBoardSize = gameData.getGameBoardSize();
        
        //Check rows and columns
        Player currentPlayer = gameData.getCurrentPlayer();
        for (int x = 1; x <= gameBoardSize; x++) {
            int markCountRow = 0;
            int markCountCol = 0;
            for (int y = 1; y <= gameBoardSize; y++) {
                Move moveRow = gameData.getMove(x, y);
                Move moveCol = gameData.getMove(y, x);
                
                markCountRow = updateMarkCounter(markCountRow, moveRow, currentPlayer);
                markCountCol = updateMarkCounter(markCountCol, moveCol, currentPlayer);

                if (markCountRow == 5 || markCountCol == 5) {
                    return currentPlayer;
                }
            }
        }
        //TODO check diagonals.
        
        return null;
    }
    
    private int updateMarkCounter(int currentValue, Move move, Player player) {
        if (move != null && move.getPlayer().equals(player)) {
            return currentValue+1;
        }
        return 0;
    }

    private boolean gameBoardFull() {
        return gameData.getNumberOfMoves() == Math.pow(gameData.getGameBoardSize(), 2);
    }

    public boolean gameOver() {
        if (this.getWinner() != null) {
            return true;
        }
        return this.gameBoardFull();
    }
}
