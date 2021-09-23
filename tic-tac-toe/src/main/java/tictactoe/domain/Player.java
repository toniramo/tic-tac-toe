package tictactoe.domain;

import java.util.Objects;
import javafx.scene.paint.Color;

/**
 * Collects player related information.
 */
public class Player {

    private final String mark;
    private final Color markColor;
    private final PlayerType type;

    public Player(String mark) {
        this(mark, Color.BLACK, PlayerType.HUMAN);
    }

    public Player(String mark, Color color) {
        this(mark, color, PlayerType.HUMAN);
    }

    public Player(String mark, Color color, PlayerType type) {
        this.mark = mark;
        this.markColor = color;
        this.type = type;
    }

    public String getMark() {
        return this.mark;
    }

    public Color getMarkColor() {
        return this.markColor;
    }

    public PlayerType getPlayerType() {
        return this.type;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.mark);
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
        final Player other = (Player) obj;
        return other.getMark().equals(this.mark);
    }

    public enum PlayerType {
        HUMAN,
        AI
    }
}
