package tictactoe.logic;

import java.util.Objects;
import javafx.scene.paint.Color;

/**
 * Collects player related information.
 */
public class Player {

    private final String mark;
    private final Color markColor;
    private final PlayerType type;

    /**
     * Create snew player with default mark color (black) and type (human).
     * @param mark chosen character representing players moves on board. 
     * Usually either "X" or "O".
     */
    public Player(String mark) {
        this(mark, Color.BLACK, PlayerType.HUMAN);
    }

    /**
     * Creates new player with default type (human).
     * @param mark chosen character representing players moves on board. 
     * Usually either "X" or "O".
     * @param color color of mark
     */
    public Player(String mark, Color color) {
        this(mark, color, PlayerType.HUMAN);
    }

    /**
     * Creates new player with custom parameters.
     * @param mark chosen character representing players moves on board. 
     * Usually either "X" or "O".
     * @param color color of mark
     * @param type either human or AI player
     */
    public Player(String mark, Color color, PlayerType type) {
        this.mark = mark;
        this.markColor = color;
        this.type = type;
    }

    /**
     * Gets player's mark.
     * @return mark of player
     */
    public String getMark() {
        return this.mark;
    }
    
    /**
     * Gets color of player mark.
     * @return mark of player's mark
     */
    public Color getMarkColor() {
        return this.markColor;
    }

    /**
     * Gets type of player (either human or AI).
     * @return player type - human or AI.
     */
    public PlayerType getPlayerType() {
        return this.type;
    }

    /**
     * 
     * @return hash value
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.mark);
        return hash;
    }

    /**
     * Tests if given object is same as this.
     * @param obj object to test
     * @return true if object is same as this, false otherwise
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
        final Player other = (Player) obj;
        return other.getMark().equals(this.mark);
    }

    /**
     * Player type, either human or AI. 
     */
    public enum PlayerType {
        HUMAN,
        AI
    }
}
