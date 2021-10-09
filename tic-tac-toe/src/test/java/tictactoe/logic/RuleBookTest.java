
package tictactoe.logic;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for RuleBook class
 */
public class RuleBookTest {

    public RuleBookTest() {
    }

    @Test
    public void twoRuleBooksWithSameParametersAreConsideredEqual() {
        int n = 20;
        int k = 5;
        Player[] players = new Player[]{new Player("X"), new Player("O")};
        RuleBook book1 = new RuleBook(n, k, players);
        RuleBook book2 = new RuleBook(n, k, players);
        assertEquals(book1, book2);
    }

    @Test
    public void twoDifferentRuleBooksAreNotConsideredEqual() {
        int n = 20;
        int k = 5;
        Player[] players = new Player[]{new Player("X"), new Player("O")};
        RuleBook book1 = new RuleBook(n, k, players);
        RuleBook book2 = new RuleBook(n - 1, k + 1, players);
        assertFalse(book1.equals(book2));
    }
}
