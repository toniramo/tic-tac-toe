package tictactoe.logic;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for Move class.
 */
public class MoveTest {

    @Test
    public void twoSimilarMovesAreConsideredEqual() {
        Move move1 = new Move(new Player("X"), 4, 5);
        Move move2 = new Move(new Player("X"), 4, 5);
        assertTrue(move1.equals(move2) && move2.equals(move1));
        assertTrue(move1.hashCode() == move2.hashCode());
    }

    @Test
    public void twoDifferentMovesAreNotConsideredEqual() {
        Move move1 = new Move(new Player("X"), 4, 5);
        Move move2 = new Move(new Player("O"), 15, 2);
        assertFalse(move1.equals(move2) && move2.equals(move1));
        assertFalse(move1.hashCode() == move2.hashCode());
    }
}
