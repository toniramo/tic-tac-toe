/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.dao;

import tictactoe.domain.*;

/**
 * Interface for data access object storing the game data.
 * @author toniramo
 */
public interface Dao {
    /**
     * Initialize game board of n*n in size and set k-in-row rule.
     * @param n Number of tiles on edge of a square game board.
     * @param k Number of marks in a row to win (k-in-row).
     */
    public void initializeGameBoard(int n, int k);
    
    /**
     * Get number of tiles on an edge on n*n game board.
     * @return Returns number of tiles on an edge of game board
     */
    public int getGameBoardSize();
    
    /**
     * 
     * @return Returns number of marks in a row needed to win
     */
    public int getNumberOfMarksToWin();
    
    /**
     * Stores Move object representing move made on board.
     * @param move 
     */
    public void setMove(Move move);
    
    /**
     * Get move from game board (if any) at chosen x,y location.
     * @param x X-coordinate of tile on game board.
     * @param y Y-coordinate of tile on game board.
     * @return Move object if such already stored in chosen location. 
     * Otherwise returns null.
     */
    public Move getMove(int x, int y);
    
    /**
     * 
     * @return Number of played moves. 0 if not any made.
     */
    public int getNumberOfPlayedMoves();
    
    /**
     * Changes player in turn to next one. 
     */
    public void changeTurn();
    
    /**
     * Add new player to game data.
     * @param player Give player as Player object
     */
    public void addPlayer(Player player);
    
    /**
     * 
     * @return Current player in turn.
     */
    public Player getCurrentPlayer();
}
