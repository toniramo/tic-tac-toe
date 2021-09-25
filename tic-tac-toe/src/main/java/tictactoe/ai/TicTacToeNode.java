/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.ai;

import java.util.ArrayList;
import java.util.List;
import tictactoe.domain.*;

/**
 *
 * @author toniramo
 */
public class TicTacToeNode implements GameTreeNode {

    private GameBoard board;
    private RuleBook rules;
    private boolean minimizingNode;
    private int maxSearchDepth;
    private int nodeDepth;
    private int turn;

    public TicTacToeNode(GameBoard board, RuleBook rules, int turn, boolean minimizingNode,
            int maxSearchDepth, int nodeDepth) {
        this.board = board;
        this.rules = rules;
        this.minimizingNode = minimizingNode;
        this.maxSearchDepth = maxSearchDepth;
        this.nodeDepth = nodeDepth;
        this.turn = turn;
    }

    @Override
    public List<GameTreeNode> getChildNodes() {
        List<GameTreeNode> childNodes = new ArrayList<>();
        int n = this.rules.getBoardsize();
        for (int x = 1; x <= n; x++) {
            for (int y = 1; y <= n; y++) {
                if (GameService.validMove(this.board, x, y)) {
                    GameBoard child = board.getCopy();
                    child.setMove(new Move(this.rules.getPlayerBasedOnTurn(turn), x, y));
                    childNodes.add(new TicTacToeNode(child, rules, (turn + 1) % 2, !minimizingNode, maxSearchDepth, nodeDepth + 1));
                }
            }
        }
        return childNodes;
    }

    @Override
    public boolean isMinimizingNode() {
        return minimizingNode;
    }

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

    @Override
    public boolean isEndState() {
        return GameService.gameOver(board, rules) || nodeDepth >= maxSearchDepth;
    }

    public int heuristicValue() {
        int value = 0;
        int[] playerValues = calculatePlayerSpecificValues();
        for (int i = 0; i < playerValues.length; i++) {
            value = (i == turn) ? (value + playerValues[i]) : (value - playerValues[i]);
        }
        return minimizingNode ? -value : value;
    }

    /**
     * Calculates player specific heuristic values. Method uses two-dimensional
     * "counters" array: first index is for player, second for direction: 0
     * vertical |, 1 horizontal -- , 2 diagonal1 / up-right (1/2) , 3 diagonal1
     * / upright (2/2) 4 diagonal2 \ down-right (1/2), 5 diagonal2 / down-right
     * (2/2).
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

    private boolean observedRangeFullyOnBoard(int[] x, int[] y, int k) {
        int n = rules.getBoardsize();
        int m = rules.getMarksToWin();
        return (k == 0 && y[k] >= m)
                || (k == 1 && x[k] >= m)
                || ((k == 2 || k == 3) && x[k] >= m && y[k] >= m)
                || ((k == 4 || k == 5) && x[k] >= m && y[k] <= n - m + 1);
    }

    private int updateCounterValue(int value, Move move, Player p) {
        if (move != null) {
            if (move.getPlayer().equals(p)) {
                return addDigitInFront(value, rules.getMarksToWin());
            }
            return 0;
        }
        return value;
    }

    private static int addDigitInFront(int number, int toFront) {
        if (number == 0) {
            return toFront;
        }
        return number + (toFront * (int) Math.pow(10, digitCounter(number)));
    }

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

    private static int digitCounter(int number) {
        return number > 10 ? ((int) (Math.log10(number) + 1)) : 1;
    }

    private static int removeTrailingZeros(int number) {
        return removeTrainingZeros(number, 10);
    }

    private static int removeTrainingZeros(int number, int divider) {
        if (number % divider == 0 && number > 0) {
            return removeTrainingZeros((number / divider), divider * 10);
        }
        return number;
    }

}
