package tictactoe.logic;

import javafx.scene.paint.Color;
import org.junit.Test;
import static org.junit.Assert.*;
import tictactoe.logic.Player.PlayerType;

/**
 * Tests for Player class.
 */
public class PlayerTest {

    @Test
    public void twoSimilarPlayersAreConsideredEqual() {
        Player player1 = new Player("X");
        Player player2 = new Player("X");

        assertTrue(player1.equals(player2) && player2.equals(player1));
        assertTrue(player1.hashCode() == player2.hashCode());
    }

    @Test
    public void twoDifferentPlayersAreNotConsideredEqual() {
        Player player1 = new Player("X", Color.AQUA, PlayerType.AI);
        Player player2 = new Player("O");

        assertFalse(player1.equals(player2) && player2.equals(player1));
        assertFalse(player1.hashCode() == player2.hashCode());
    }
}
