package tictactoe.logic;

import java.util.Objects;

/**
 * Move is used to collect and pass information about made move on game board of
 * a player.
 */
public class Move {

    private final Player player;
    private final int x;
    private final int y;

    /**
     * Create move related to certain player and location on game board.
     *
     * @param player Player who has made the move
     * @param x X-coordinate of move on game board
     * @param y Y-coordinate of move on game board
     */
    public Move(Player player, int x, int y) {
        this.player = player;
        this.x = x;
        this.y = y;
    }

    /**
     * Get player who has made this move.
     *
     * @return player made this move
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get coordinate x of move.
     *
     * @return X-coordinate of move
     */
    public int getX() {
        return this.x;
    }

    /**
     * Get coordinate y of move.
     *
     * @return Y-coordinate of move
     */
    public int getY() {
        return this.y;
    }

    /**
     *
     * @return hash value
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.player);
        hash = 67 * hash + this.x;
        hash = 67 * hash + this.y;
        return hash;
    }

    /**
     * Tests if given object is same as this
     *
     * @param obj
     * @return true if object is same, false otherwise
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
        final Move other = (Move) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (!Objects.equals(this.player, other.player)) {
            return false;
        }
        return true;
    }
}
