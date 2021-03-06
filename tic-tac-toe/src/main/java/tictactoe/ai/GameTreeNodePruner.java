/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.ai;

import tictactoe.annotations.ExcludeFromJacocoGeneratedReport;

/**
 * Implements minimax algorithm with alpha beta pruning and gets value for child
 * node in game tree. Due to performance issues related to object arrays, class is replaced by 
 * {@link AlphaBetaMoveChooser}.
 */
@Deprecated
@ExcludeFromJacocoGeneratedReport
public class GameTreeNodePruner {

    public static int getNodeValue(GameTreeNode node, int alpha, int beta, int maxSearchDepth) {
        if (node.isMinimizingNode()) {
            return minValue(node, alpha, beta, 0, maxSearchDepth);
        }
        return maxValue(node, alpha, beta, 0, maxSearchDepth);
    }

    private static int maxValue(GameTreeNode node, int alpha, int beta, int nodeDepth, int maxSearchDepth) {
        if (node.isEndState() || nodeDepth == maxSearchDepth) {
            return node.value();
        }
        int value = Integer.MIN_VALUE;
        for (GameTreeNode child : node.getChildNodes()) {
            value = Math.max(value, minValue(child, alpha, beta, nodeDepth + 1, maxSearchDepth));
            alpha = Math.max(alpha, value);
            if (alpha >= beta) {
                return value;
            }
        }
        return value;
    }

    private static int minValue(GameTreeNode node, int alpha, int beta, int nodeDepth, int maxSearchDepth) {
        if (node.isEndState() || nodeDepth == maxSearchDepth) {
            return node.value();
        }
        int value = Integer.MAX_VALUE;
        for (GameTreeNode child : node.getChildNodes()) {
            value = Math.min(value, maxValue(child, alpha, beta, nodeDepth + 1, maxSearchDepth));
            beta = Math.min(beta, value);
            if (beta <= alpha) {
                return value;
            }
        }
        return value;
    }
}
