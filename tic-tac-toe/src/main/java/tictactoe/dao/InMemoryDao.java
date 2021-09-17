/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.dao;

import java.util.ArrayList;
import java.util.List;
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
    private List<Player> players;

    @Override
    public void initializeGameBoard(int n, int k) {
        this.n = n;
        this.k = k;
        this.moves = new Move[n + 1][n + 1];
        this.numberOfMoves = 0;
        this.turn = 0;
        this.players = new ArrayList<>();
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
    public void addPlayer(Player player) {
        this.players.add(player);
    }
    
    @Override
    public Player getCurrentPlayer() {
        return players.get(turn);
    }
    
    public void changeTurn() {
        if (turn == players.size() - 1) {
            turn = 0;
        } else {
            turn++;
        }
    }
}
