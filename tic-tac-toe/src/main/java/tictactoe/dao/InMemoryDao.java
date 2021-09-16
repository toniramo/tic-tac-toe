/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.dao;

import tictactoe.domain.Move;
import tictactoe.domain.Player;

/**
 *
 * @author toniramo
 */
public class InMemoryDao implements Dao {

    private int n; //Game board size
    private int k; //Number of marks to win
    private Move[][] moves;
    private int numberOfMoves;
    private Move lastMove;
    private int turn;
    private Player[] players;

    @Override
    public void initializeGameBoard(int n, int k) {
        this.n = n;
        this.k = k;
        this.moves = new Move[n + 1][n + 1];
        this.numberOfMoves = 0;
    }

    @Override
    public int getGameBoardSize() {
        return this.n;
    }

    @Override
    public int getNumberOfMarksToWin() {
        return this.k;
    }
    
    @Override
    public void setMove(Move move) {
        this.moves[move.getX()][move.getY()] = move;
        this.lastMove = move;
    }

    @Override
    public Move getMove(int x, int y) {
        return this.moves[x][y];
    }

    public Move getLastMove() {
        return this.lastMove;
    }

    @Override
    public int getNumberOfMoves() {
        return this.numberOfMoves;
    }

    @Override
    public void setTurn(int playerIndex) {
        this.turn = playerIndex;
    }
    
    @Override
    public int getTurn() {
        return this.turn;
    }

    @Override
    public void setPlayers(Player[] players) {
        this.players = players;
    }

    @Override
    public Player[] getPlayers() {
        return this.players;
    }
}
