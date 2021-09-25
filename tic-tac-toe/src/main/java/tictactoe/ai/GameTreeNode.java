package tictactoe.ai;

import java.util.List;

/**
 * Interface representing node in game tree.
 */
public interface GameTreeNode {

    /**
     * Gets value of this game tree node. If game is not over, a heuristic value
     * is calculated.
     *
     * @return value of this game tree node, heuristic if not end leaf.
     */
    public int value();

    /**
     * Tells whether this node is end state. In this case it means, if either
     * game is over or maximum search depth is reached.
     *
     * @return
     */
    public boolean isEndState();

    /**
     * Tells if player in turn is pursuing to minimize the value of outcome.
     *
     * @return true if player in turn is minimizing value of outcome, false for
     * maximizing player
     */
    public boolean isMinimizingNode();

    /**
     * Gets nearest child nodes of this node in game tree.
     *
     * @return
     */
    public List<GameTreeNode> getChildNodes();
}
