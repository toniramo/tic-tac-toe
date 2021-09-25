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

    public boolean isEndState() {
        return GameService.gameOver(board, rules) || nodeDepth >= maxSearchDepth;
    }

    private int heuristicValue() {
        int value = 0;
        int[] playerValues = calculatePlayerSpecificValues();
        for (int i = 0; i < playerValues.length; i++) {
            value = (i == turn) ? (value + playerValues[i]) : (value - playerValues[i]);
        }
        return minimizingNode ? -value : value;
    }

    private int[] calculatePlayerSpecificValues() {
        int n = rules.getBoardsize();
        Player[] players = rules.getPlayers();
        int[] playerValues = new int[players.length];
        for (int i = 1; i <= n; i++) {
            /*
             * Counters: first index is for player, second for direction: 
             * 0 vertical |, 1 horizontal -- , 
             * 2 diagonal1 / up-right (1/2) , 3 diagonal1 / upright (2/2)
             * 4 diagonal2 \ down-right (1/2), 4 diagonal2 / down-right (2/2)
             */
            int[][] counters = new int[players.length][6]; //[][]
            //int offset1 = 20;
            int offset = 0;
            //int offset2 = n;

            for (int j = 1; j <= n; j++) {
                Move[] moves = new Move[]{board.getMove(i, j), board.getMove(j, i),
                    board.getMove(i + offset, j), board.getMove(i + offset - n, j), board.getMove(j, i - offset), board.getMove(j, i - offset + n)};
                for (int k = 0; k < counters[0].length; k++) {
                    //if (k==5) System.out.println("x:" + j + " y " +(i - offset + n)+ " " + (i - offset + n <= n));
                    //if (k==5) System.out.println("x:" + j + " y " +(i - offset+n) + " " + (i - offset <= n) + " i " + i + " j " + j + " offset " + offset);
                    // if (k==2) System.out.println("x:" + (i+offset-n)  + " y " + j + " " );
                    //if (k==3) System.out.println((i + offset - n ));
                    if ((k == 2 && i + offset > n) || (k == 3 && i + offset - n < 1) || (k == 4 && i - offset < 1) || (k == 5 && i - offset + n > n)) {
                        // if (k == 2) {
                        //System.out.println("SKip\\");
                        // }
                        continue;
                    }
                    for (int p = 0; p < players.length; p++) {
                        if (moves[k] != null && moves[k].getPlayer().equals(players[p])) {
                            if (moves[k] != null) {
                                //System.out.println("i " + i + " j " + j + " offset " + offset + " k " + k);
                                counters[p][k] = addDigitInFront(counters[p][k], rules.getMarksToWin());
                                //System.out.println("c "  + counters[p][k] + " add "  + addDigitInFront(counters[p][k], rules.getMarksToWin()));
                                //counters[p][k] = addDigitInFront(counters[p][k], rules.getMarksToWin());
                                // System.out.println(counters[p][k]);
                                //System.out.println("x " + moves[k].getX() + " y " + moves[k].getY() + " counters" + counters[p][k]);
                            } else {
                                counters[p][k] = 0;
                            }
                        }
                        //if (k==5) System.out.println(counters[p][k]);
                        int m = rules.getMarksToWin();
                        if (((k == 0 && j >= m)
                                || (k == 1 && j >= m)
                                || (k == 2 && i + offset >= m && j >= m)
                                || (k == 3 && (i + offset - n) >= m && j >= m)
                                || (k == 4 && j >= m && (i - offset) <= n - m + 1)
                                || (k == 5 && j >= m && (i - offset + n) <= n - m + 1))
                                && counters[p][k] > 0) {
                            //if (k==5) System.out.println("x:" + j + " y " +(i - offset + n)+ " " + (i - offset + n <= n));
                            //System.out.println("i " + i + " j " + j + " offset " + offset + " k " + k);
                            //if (k==3) System.out.println("x " + (i+offset-n) + " y " + j + " i " + i + " j " + j + " offset " + (offset-n) + " k " + k);
                            //if(moves[k] != null) {
                            //    System.out.println(moves[k].getX() + " " + moves[k].getY());
                            //}
                            System.out.println("countervalue: " + counters[p][k] + " digits " + digitCounter(counters[p][k]));
                            //Tested with full 20x20 board of X, got 115200000.
                            playerValues[p] += Math.pow(10, digitCounter(counters[p][k]));

                            if (playerValues[p] < 0) {
                                System.out.println("out of range");
                            }
                        }
                        //if (counters[p][k] >0 ) System.out.print("Before reduction: "  +counters[p][k]);
                        counters[p][k] = reduceAllDigitsByOne(counters[p][k]);

                        //if (counters[p][k] >0 ) System.out.println(" After: " + counters[p][k]);
                    }
                }
                //System.out.println("Playervalue: " + playerValues[0]);
                offset++;
            }
        }
        return playerValues;
    }

    public int getHeuristicValueFromRange(Player p, int x, int y, int x0, int xn, int c, int value) {
        if (x == xn) {
            return value;
        }

        Move move = board.getMove(x, y);
        if (move != null) {
            if (move.getPlayer().equals(p)) {
                c = addDigitInFront(c, rules.getMarksToWin());
            } else {
                c = 0;
            }
        }

        int newValue = value;
        if (x - 4 >= x0 && c > 0) {
            newValue += digitCounter(c);
        }
        System.out.println("x:" + x + " y: " + y + " c:" + c + " value: " + newValue + " ");

        return getHeuristicValueFromRange(p, x + 1, y, x0, xn, reduceAllDigitsByOne(c), newValue);
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
        //System.out.println( (int) (Math.log10(number) + 1));
        return number > 10 ? ((int) (Math.log10(number) + 1)) : 1;
        //return digitCounter(number, 1, 1);
    }

    private static int digitCounter(int number, int digits, int divider) {
        //System.out.println("number: " + number + " digit: " +digits + " dividier " + divider + " " + (number%divider));
        if (number % divider != 0 && number > 0) {
            return digitCounter(number, digits + 1, divider * 10);
        }
        // System.out.println(digits);
        return digits;
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
