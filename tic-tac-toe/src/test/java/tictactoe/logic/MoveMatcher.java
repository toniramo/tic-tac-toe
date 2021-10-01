package tictactoe.logic;

import org.mockito.ArgumentMatcher;

/**
 * Matcher used in game service unit tests to test parameters given to game data mock.
 */
public class MoveMatcher implements ArgumentMatcher<Move> {

    private Move move;
    
    public MoveMatcher(Move move) {
        this.move = move;
    }
    
    @Override
    public boolean matches(Move move) {
        return this.move.equals(move);
    }
    
}
