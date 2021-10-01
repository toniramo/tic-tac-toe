package tictactoe.domain;

import java.util.Arrays;

/**
 * RuleBook containing essential parameters to form rules for n,m,k-tic-tac-toe
 * game.
 */
public class RuleBook {

    private final int marksToWin;
    private final int boardSize;
    private final Player[] players;

    /**
     * Create new rule book.
     *
     * @param boardSize width of the game board, that is number of tiles in an
     * edge
     * @param marksToWin number of marks in a row needed for player to win the
     * game
     * @param players players of the game, by default two is given with first
     * having "X"-mark and the second "O".
     */
    public RuleBook(int boardSize, int marksToWin, Player[] players) {
        this.boardSize = boardSize;
        this.marksToWin = marksToWin;
        this.players = players;
    }

    /**
     * Gets number of marks in a row needed to win the game.
     * @return number of needed marks in a row for win.
     */
    public int getMarksToWin() {
        return this.marksToWin;
    }

    /**
     * Gets expected board size in terms of width of board edge
     * @return game board edge width
     */
    public int getBoardsize() {
        return this.boardSize;
    }

    /**
     * Gets all players of the game, by default two
     * @return players of the game.
     */
    public Player[] getPlayers() {
        return this.players;
    }

    /**
     * Gets player based on turn, that is first player on turn 0, second on 1 by default.
     * @param i turn, by default if first player's turn, i=0, and if second player's i=1.
     * @return player on turn index i
     */
    public Player getPlayerBasedOnTurn(int i) {
        return this.players[i];
    }

    /**
     * Gets hash value for this rule book.
     * @return hash value of this rule book
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + this.marksToWin;
        hash = 13 * hash + this.boardSize;
        hash = 13 * hash + Arrays.deepHashCode(this.players);
        return hash;
    }

    /**
     * Check if compared object is equal to this rule book.
     * @param obj
     * @return true if object is equal to this, false otherwise
     */
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
