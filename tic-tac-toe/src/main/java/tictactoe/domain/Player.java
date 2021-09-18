package tictactoe.domain;

import java.util.Objects;
import javafx.scene.paint.Color;

/**
 * Collects player related information.
 */
public class Player {

    private final String mark;
    private final Color markColor;

    public Player(String mark) {
        this(mark, Color.BLACK);
    }

    public Player(String mark, Color color) {
        this.mark = mark;
        this.markColor = color;
    }

    public String getMark() {
        return this.mark;
    }
    
    public Color getMarkColor() {
        return this.markColor;
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
}
