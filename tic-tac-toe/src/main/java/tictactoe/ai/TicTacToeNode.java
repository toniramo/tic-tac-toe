package tictactoe.ai;

import java.util.ArrayList;
import java.util.List;
import tictactoe.domain.*;

/**
 * Represents tic-tac-toe specific node of game tree that is used to find most
 * optimal move by AI.
 */
public class TicTacToeNode implements GameTreeNode {

    private GameBoard board;
    private RuleBook rules;
    private boolean minimizingNode;
    private int maxSearchDepth;
    private int nodeDepth;
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
        return childNodes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMinimizingNode() {
        return minimizingNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int value() {
        if (GameService.gameOver(board, rules)) {
            Player winner = GameService.getWinningPlayer(board, rules);
            if (winner != null) {
                return minimizingNode ? Integer.MIN_VALUE : Integer.MAX_VALUE;
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
        return GameService.gameOver(board, rules) || nodeDepth >= maxSearchDepth;
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
        return minimizingNode ? -value : value;
    }

    /**
     * Calculates player specific heuristic values. Idea is to go through the
     * game board simultaneously in all directions -horizontal, vertical,
     * diagonal (up-right, down-right)) - and evaluate the value of moves in
     * subsections with length equal to number of marks in a row needed to win.
     */
    private int[] calculatePlayerSpecificValues() {
        int n = rules.getBoardsize();
        Player[] players = rules.getPlayers();
        int[] playerValues = new int[players.length];
        for (int i = 1; i <= n; i++) {
            int[][] counters = new int[players.length][6];
            int offset = 0;
            for (int j = 1; j <= n; j++) {
                int[] x = new int[]{i, j, (i + offset), (i + offset - n), j, j};
                int[] y = new int[]{j, i, j, j, i - offset, i - offset + n};
                Move[] moves = board.getMoves(x, y);
                for (int k = 0; k < counters[0].length; k++) {
                    if (x[k] < 1 || x[k] > n || y[k] < 1 || y[k] > n) {
                        continue;
                    }
                    for (int p = 0; p < players.length; p++) {
                        counters[p][k] = updateCounterValue(counters[p][k], moves[k], players[p]);
                        if (counters[p][k] > 0 && observedRangeFullyOnBoard(x, y, k)) {
                            playerValues[p] += Math.pow(10, digitCounter(counters[p][k]) - 1);
                        }
                        counters[p][k] = reduceAllDigitsByOne(counters[p][k]);
                    }
                }
                offset++;
            }
        }
        return playerValues;
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

    /**
     * Updates counter value with observed move. Counter is used to determine
     * heuristic value of the node. Counter value indicates, based on number of
     * digits, the number of marks there are within the range of observed
     * subsection hinting the likelyhood of getting full winning row.
     *
     * @param value Counters current state
     * @param move move on board to be analyzed
     * @param p player for whom value will be evaluated
     * @return updated counter value (reverts counter to 0 if opponents mark).
     */
    private int updateCounterValue(int value, Move move, Player p) {
        if (move != null) {
            if (move.getPlayer().equals(p)) {
                return addDigitInFront(value, rules.getMarksToWin());
            }
            return 0;
        }
        return value;
    }

    /**
     * Adds digit in front of given number. Used to update counter value.
     *
     * @param number
     * @param toFront
     * @return
     */
    private static int addDigitInFront(int number, int toFront) {
        if (number == 0) {
            return toFront;
        }
        return number + (toFront * (int) Math.pow(10, digitCounter(number)));
    }

    /**
     * Reduces all digits of given number by one, unless number is 0.
     *
     * @param number number, or counter value, to be reduced.
     * @return reduced number, 0 if number is 0.
     */
    private static int reduceAllDigitsByOne(int number) {
        if (number == 0) {
            return 0;
        }
        int digits = digitCounter(number);
        for (int i = 0; i < digits; i++) {
            number -= (int) Math.pow(10, i);
        }
        return removeTrailingZeros(number);
    }

    /**
     * Calculates length of given numbers.
     *
     * @param number to be measured
     * @return result of measurement
     */
    private static int digitCounter(int number) {
        return number > 10 ? ((int) (Math.log10(number) + 1)) : 1;
    }

    /**
     * Removes any trailing zeros of given number, that is zeros from left to
     * right until other number is encounter. Uses {@link #removeTrainingZeros(int, int)
     * method to perform actual operation.
     *
     * @param number to be reduced
     * @return reduced number without trailing zeros
     */
    private static int removeTrailingZeros(int number) {
        return removeTrainingZeros(number, 10);
    }

    /**
     * Removes any trailing zeros of given number, that is zeros from left to
     * right until other number is encounter.
     *
     * @param number to be reduced
     * @return reduced number without trailing zeros
     */
    private static int removeTrainingZeros(int number, int divider) {
        if (number % divider == 0 && number > 0) {
            return removeTrainingZeros((number / divider), divider * 10);
        }
        return number;
    }

}
