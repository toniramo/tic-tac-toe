package tictactoe.domain;

import java.util.Objects;

/**
 * Move is used to collect and pass information about 
 * made move on game board of a player.
 */
public class Move {

    private final Player player;
    private final int x;
    private final int y;
    
    public Move(Player player, int x, int y) {
        this.player = player;
        this.x = x;
        this.y = y;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.player);
        hash = 67 * hash + this.x;
        hash = 67 * hash + this.y;
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
