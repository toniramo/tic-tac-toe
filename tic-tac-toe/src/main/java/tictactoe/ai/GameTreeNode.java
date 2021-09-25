/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.ai;

import java.util.List;

/**
 *
 * @author toniramo
 */
public interface GameTreeNode {
    public int value();
    public boolean isEndState();
    public boolean isMinimizingNode();
    public List<GameTreeNode> getChildNodes();
}
