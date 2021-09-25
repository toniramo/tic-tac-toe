/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.domain;

import java.util.Arrays;

/**
 *
 * @author toniramo
 */
public class RuleBook {

    private final int marksToWin;
    private final int boardSize;
    private final Player[] players;

    public RuleBook(int boardSize, int marksToWin, Player[] players) {
        this.boardSize = boardSize;
        this.marksToWin = marksToWin;
        this.players = players;
    }

    public int getMarksToWin() {
        return this.marksToWin;
    }

    public int getBoardsize() {
        return this.boardSize;
    }

    public Player[] getPlayers() {
        return this.players;
    }
    
    public Player getPlayerBasedOnTurn(int i) {
        return this.players[i];
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + this.marksToWin;
        hash = 13 * hash + this.boardSize;
        hash = 13 * hash + Arrays.deepHashCode(this.players);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RuleBook other = (RuleBook) obj;
        if (this.marksToWin != other.marksToWin) {
            return false;
        }
        if (this.boardSize != other.boardSize) {
            return false;
        }
        if (!Arrays.deepEquals(this.players, other.players)) {
            return false;
        }
        return true;
    }
}
