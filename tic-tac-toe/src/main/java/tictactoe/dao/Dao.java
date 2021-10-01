/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.dao;

import tictactoe.logic.Move;
import tictactoe.logic.RuleBook;
import tictactoe.logic.GameBoard;

/**
 * Interface for data access object storing the game data.
 *
 * @author toniramo
 */
public interface Dao {

    public void initializeGameBoard(RuleBook rules);

    public RuleBook getRules();

    public void setGameBoard(GameBoard board);

    public GameBoard getGameBoard();

    /*
     * Stores Move object representing move made on board.
     * @param move 
     */
    public void setMove(Move move);

    /**
     * Get move from game board (if any) at chosen x,y location.
     *
     * @param x X-coordinate of tile on game board.
     * @param y Y-coordinate of tile on game board.
     * @return Move object if such already stored in chosen location. Otherwise
     * returns null.
     */
    public Move getMoveAt(int x, int y);

    /**
     *
     * @return Number of played moves. 0 if not any made.
     */
    public int getNumberOfPlayedMoves();

    /**
     * Set player in turn by player index.
     * @param turn index of player in turn
     */
    public void setTurn(int turn);
    
    /**
     * Get player in turn by player index.
     * @return index of player in turn
     */
    public int getTurn();

}
