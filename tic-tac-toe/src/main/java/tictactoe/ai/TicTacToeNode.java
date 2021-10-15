package tictactoe.ai;

import tictactoe.logic.Move;
import tictactoe.logic.RuleBook;
import tictactoe.logic.GameBoard;
import tictactoe.logic.Player;
import tictactoe.logic.GameService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import tictactoe.annotations.ExcludeFromJacocoGeneratedReport;

/**
 * Represents tic-tac-toe specific node of game tree that is used to find most
 * optimal move by AI. This is no more developed further due to performance reasons. 
 * AI keeps simpler data structure for node data and uses {@link GameTreeNodePruner} 
 * to get the optimal move
 */
@Deprecated
@ExcludeFromJacocoGeneratedReport
public class TicTacToeNode implements GameTreeNode {

    private GameBoard board;
    private RuleBook rules;
    private boolean minimizingNode;
    private int turn;

    /**
     * Creates single tic-tac-toe node of game tree.
     *
     * @param board game board in its current, node specific state
     * @param rules game rules that determine for instance board size and
     * required row length to win
     * @param turn ordinal of player in turn, by default between 0 and 1
     * @param minimizingNode indicates whether player in turn is minimizing the
     * value of the outcome, otherwise player is maximizing the value
     */
    public TicTacToeNode(GameBoard board, RuleBook rules, int turn, boolean minimizingNode) {
        this.board = board;
        this.rules = rules;
        this.minimizingNode = minimizingNode;
        this.turn = turn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GameTreeNode> getChildNodes() {
        // double start = System.currentTimeMillis();
        List<GameTreeNode> childNodes = new ArrayList<>();
        int n = this.rules.getBoardsize();
        for (int x = 1; x <= n; x++) {
            for (int y = 1; y <= n; y++) {
                if (GameService.validMove(this.board, x, y)) {
                    GameBoard child = board.getCopy();
                    child.setMove(new Move(this.rules.getPlayerBasedOnTurn(turn), x, y));
                    childNodes.add(new TicTacToeNode(child, rules, (turn + 1) % 2, !minimizingNode));
                }
            }
        }
        //System.out.println("get childs took : " + (System.currentTimeMillis() - start));
        return childNodes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMinimizingNode() {
        return this.minimizingNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int value() {
        if (GameService.gameOver(board, rules)) {
            Player winner = GameService.getWinningPlayer(board, rules);
            if (winner != null) {
                return this.isMinimizingNode() ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            }
            return 0;
        }
        return heuristicValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEndState() {
        return GameService.gameOver(board, rules);
    }

    /**
     * {@inheritDoc}
     */
    public int heuristicValue() {
        int value = 0;
        int[] playerValues = calculatePlayerSpecificValues();
        for (int i = 0; i < playerValues.length; i++) {
            value = (i == turn) ? (value + playerValues[i]) : (value - playerValues[i]);
        }
        return this.isMinimizingNode() ? -value : value;
    }

    /**
     * Calculates player specific heuristic values. Idea is to go through the
     * game board simultaneously in all directions -horizontal, vertical,
     * diagonal (up-right, down-right)) - and evaluate the value of moves in
     * subsections with length equal to number of marks in a row needed to win.
     */
    private int[] calculatePlayerSpecificValues() {
        //double start = System.currentTimeMillis();
        int n = rules.getBoardsize();
        Player[] players = rules.getPlayers();
        int[] playerValues = new int[players.length];
        for (int i = 1; i <= n; i++) {
            int[][][] counters = new int[players.length][6][rules.getMarksToWin() + 1];
            int offset = 0;
            int[][] marksOnRange = new int[players.length][counters[0].length + 1];
            for (int j = 1; j <= n; j++) {
                int[] x = new int[]{i, j, (i + offset), (i + offset - n), j, j};
                int[] y = new int[]{j, i, j, j, i - offset, i - offset + n};
                Move[] moves = board.getMoves(x, y);
                for (int k = 0; k < counters[0].length; k++) {
                    if (x[k] < 1 || x[k] > n || y[k] < 1 || y[k] > n) {
                        continue;
                    }
                    for (int p = 0; p < players.length; p++) {
                        if (moves[k] != null) {
                            if (moves[k].getPlayer().equals(players[p])) {
                                if (marksOnRange[p][k] < rules.getMarksToWin()) {
                                    counters[p][k][++marksOnRange[p][k]] = rules.getMarksToWin();
                                }
                            }
                        }
                        if (marksOnRange[p][k] == rules.getMarksToWin()) {
                            playerValues[p] = Integer.MAX_VALUE;
                            break;
                        }
                        if (marksOnRange[p][k] > 0 && observedRangeFullyOnBoard(x, y, k) && marksOnRange[((p + 1) % 2)][k] == 0) {
                            playerValues[p] += Math.pow(10, marksOnRange[p][k] - 1);
                        }
                        counters[p][k] = reduceMarkCountersByOne(counters[p][k]);
                        if (marksOnRange[p][k] > 0 && counters[p][k][marksOnRange[p][k]] == 0) {
                            marksOnRange[p][k]--;
                        }

                    }
                }
                offset++;
            }
        }
        //System.out.println("heuristic time: " + (System.currentTimeMillis() - start));
        return playerValues;
    }

    /**
     * Reduces counter value in all indexes of an array unless value is already
     * 0.
     *
     * @param marks counter array
     * @return updated counter array
     */
    public int[] reduceMarkCountersByOne(int[] marks) {
        int[] result = new int[marks.length];
        int j = 0;
        for (int i = 1; i < marks.length; i++) {
            if (marks[i] - 1 == 0) {
                j++;
                continue;
            }
            if (marks[i] > 0) {
                result[i - j] = marks[i] - 1;
            }
        }
        return result;
    }

    /**
     * Checks if observed subsection of the board is fully on the game board. In
     * other words, all coordinates should refer to location on board.
     *
     * @param x x-coordinates in terms of tiles on game board (left to right)
     * @param y y-coordinates in terms of tiles on game board (bottom to up)
     * @param k index of coordinate in array
     * @return true if subsection is fully on board, otherwise, if it contains
     * illegal moves, false.
     */
    private boolean observedRangeFullyOnBoard(int[] x, int[] y, int k) {
        int n = rules.getBoardsize();
        int m = rules.getMarksToWin();
        return (k == 0 && y[k] >= m)
                || (k == 1 && x[k] >= m)
                || ((k == 2 || k == 3) && x[k] >= m && y[k] >= m)
                || ((k == 4 || k == 5) && x[k] >= m && y[k] <= n - m + 1);
    }

}
