/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.domain;

/**
 *
 * @author toniramo
 */
public class Move {

    private final Player player;
    private final int x;
    private final int y;
    
    public Move(Player player, int x, int y){
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
}
