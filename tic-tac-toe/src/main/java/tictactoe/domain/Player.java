/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.domain;

import javafx.scene.paint.Color;

/**
 *
 * @author toniramo
 */
public class Player {

    private final String mark;
    private final Color markColor;

    public Player(int playerNumber, String mark) {
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
}
