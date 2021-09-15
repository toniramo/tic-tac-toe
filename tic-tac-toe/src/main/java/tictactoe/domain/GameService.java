/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.domain;

import javafx.scene.paint.Color;
import tictactoe.dao.Dao;
import tictactoe.dao.InMemoryDao;

/**
 *
 * @author toniramo
 */
public class GameService {

    private Dao gameData;
    
    public GameService(Dao dao) {
        this.gameData = dao;
    }

    public void startNewGame(int gameBoardSize) {
        this.gameData.initializeGameBoard(gameBoardSize);
        Player[] players = new Player[2 + 1];
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
    
    public boolean makeMove(int x, int y) {
        if (gameData.getMove(x, y) == null) {
            gameData.setMove(new Move(this.getCurrentPlayer(), x, y));
            if (!this.gameOver()) {
                this.changeTurn();
                return true;
            }
        }
        return false;
    }

    private Player winner() {
        // TODO implement method
        return null;
    }
    
    private boolean gameBoardFull() {
        return gameData.getNumberOfMoves() == Math.pow(gameData.getGameBoardSize(), 2);
    }
    
    public boolean gameOver() {
        if (this.winner() != null) {
            return true;
        }
        return this.gameBoardFull();
    }
}
