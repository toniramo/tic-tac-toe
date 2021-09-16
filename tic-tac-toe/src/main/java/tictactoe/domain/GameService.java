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
    
    //This is currently implemented in brute-force way. To be improved.
    public Player getWinner() {
        int gameBoardSize = gameData.getGameBoardSize();

        //Check rows
        for (int y = 1; y <= gameBoardSize; y++) {
            int markCount = 0;
            for (int x = 1; x <= gameBoardSize; x++) {
                if (gameData.getMove(x, y) != null) {
                    if (gameData.getMove(x, y).getPlayer().equals(gameData.getCurrentPlayer())) {
                        markCount++;
                    } else {
                        markCount = 0;
                    }
                    if (markCount == 5) {
                        return gameData.getCurrentPlayer();
                    }
                }

            }
        }

        //Check columns
        for (int x = 1; x <= gameBoardSize; x++) {
            int markCount = 0;
            for (int y = 1; y <= gameBoardSize; y++) {
                if (gameData.getMove(x, y) != null) {
                    if (gameData.getMove(x, y).getPlayer().equals(gameData.getCurrentPlayer())) {
                        markCount++;
                    } else {
                        markCount = 0;
                    }
                    if (markCount == 5) {
                        return gameData.getCurrentPlayer();
                    }
                }
            }
        }
        //Check diagonals

        return null;
    }

    private void readLines() {

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
