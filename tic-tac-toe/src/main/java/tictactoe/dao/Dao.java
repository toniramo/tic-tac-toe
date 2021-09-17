/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.dao;

import tictactoe.domain.*;

/**
 *
 * @author toniramo
 */
public interface Dao {
    public void initializeGameBoard(int n, int k);
    public int getGameBoardSize();
    public int getNumberOfMarksToWin();
    public void setMove(Move move);
    public Move getMove(int x, int y);
    public int getNumberOfMoves();
    public void changeTurn();
    public void addPlayer(Player player);
    public Player getCurrentPlayer();
}
