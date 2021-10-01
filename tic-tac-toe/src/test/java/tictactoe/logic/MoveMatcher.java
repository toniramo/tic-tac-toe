/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import org.mockito.ArgumentMatcher;
import tictactoe.domain.Move;

/**
 *
 * @author toniramo
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
