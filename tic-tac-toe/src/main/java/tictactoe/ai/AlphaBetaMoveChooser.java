package tictactoe.ai;

/**
 * Implements minimax algorithm with alpha beta pruning and gets move with
 * highest estimated value
 */
public class AlphaBetaMoveChooser {

    public static int[] getMove(int[][] node, int[] playArea, int playedMoves,
            int turn, int rowLenght, int alpha, int beta, int maxSearchDepth) {
        int[] move = new int[2];
        int value = turn == 0 ? -(int) 1e9 : (int) 1e9;
        for (int x = playArea[0]; x <= playArea[2]; x++) {
            for (int y = playArea[1]; y <= playArea[3]; y++) {
                if (x > 0 && x < node.length && y > 0 && y < node[0].length && node[x][y] < 0) {
                    node[x][y] = turn;
                    int alphaBetaValue = value(node, playArea, new int[]{x, y},
                            playedMoves + 1, (turn + 1) % 2, rowLenght, alpha, beta, 0, maxSearchDepth);
                    node[x][y] = -1;
                    if (turn == 0) {
                        if (value <= alphaBetaValue) {
                            value = alphaBetaValue;
                            move[0] = x;
                            move[1] = y;
                        }
                        alpha = Math.max(alpha, value);
                    } else {
                        if (value >= alphaBetaValue) {
                            value = alphaBetaValue;
                            move[0] = x;
                            move[1] = y;
                        }
                        beta = Math.min(beta, value);
                    }
                    if (alpha >= beta) {
                        return move;
                    }
                }
            }
        }
        return move;
    }

    private static int value(int[][] node, int[] playArea, int[] move, int playedMoves,
            int turn, int rowLenght, int alpha, int beta, int nodeDepth, int maxSearchDepth) {
        int maxValue = (int) 1e9 - nodeDepth - (turn + 1) % 2;
        if (lastMoveWinning(node, move, rowLenght)) {
            return (turn + 1) % 2 == 0 ? maxValue : -maxValue;
        }
        if (gameBoardFull(node, playedMoves)) {
            return 0;
        }
        if (nodeDepth >= maxSearchDepth) {
            return heuristicBasedOnPlayArea(node, playArea, (turn + 1 % 2), rowLenght, nodeDepth);
        }
        int value = (turn == 0) ? -maxValue : maxValue;
        for (int x = playArea[0] - 1; x <= playArea[2] + 1; x++) {
            for (int y = playArea[1] - 1; y <= playArea[3] + 1; y++) {
                if (x > 0 && x < node.length && y > 0 && y < node[0].length && node[x][y] < 0) {
                    node[x][y] = turn;
                    boolean[] areaChanged = increasePlayArea(playArea, x, y);
                    int alphaBetaValue = value(node, playArea, new int[]{x, y},
                            playedMoves + 1, (turn + 1) % 2, rowLenght, alpha, beta,
                            nodeDepth + 1, maxSearchDepth);
                    if (turn == 0) {
                        value = Math.max(value, alphaBetaValue);
                        alpha = Math.max(alpha, value);
                    } else {
                        value = Math.min(value, alphaBetaValue);
                        beta = Math.min(beta, value);
                    }
                    node[x][y] = -1;
                    decreasePlayArea(playArea, areaChanged);
                }
            }
            if (alpha >= beta) {
                return value;
            }
        }
        return value;
    }

    private static boolean[] increasePlayArea(int[] playArea, int x, int y) {
        boolean[] areaChanged = new boolean[4];
        if (x < playArea[0]) {
            playArea[0]--;
            areaChanged[0] = true;
        }
        if (x > playArea[2]) {
            playArea[2]++;
            areaChanged[2] = true;
        }
        if (y < playArea[1]) {
            playArea[1]--;
            areaChanged[1] = true;
        }
        if (y > playArea[3]) {
            playArea[3]++;
            areaChanged[3] = true;
        }
        return areaChanged;
    }

    private static void decreasePlayArea(int[] playArea, boolean[] areaChanged) {
        if (areaChanged[0]) {
            playArea[0]++;
        }
        if (areaChanged[2]) {
            playArea[2]--;
        }
        if (areaChanged[1]) {
            playArea[1]++;
        }
        if (areaChanged[3]) {
            playArea[3]--;
        }
    }

    private static boolean gameBoardFull(int[][] node, int playedMoves) {
        return Math.pow(node.length - 1, 2) == playedMoves;
    }

    private static boolean lastMoveWinning(int[][] node, int[] move, int m) {
        int n = node.length - 1;
        int x0 = move[0];
        int y0 = move[1];
        int[] counters = new int[4];
        for (int i = 0; i < 2 * m - 1; i++) {
            int delta = i - (m - 1);
            int[] x = new int[]{x0 + delta, x0, x0 + delta, x0 + delta};
            int[] y = new int[]{y0, y0 + delta, y0 + delta, y0 - delta};
            for (int k = 0; k < counters.length; k++) {
                if ((x[k] < 1 || x[k] > n || y[k] < 1 || y[k] > n)) {
                    continue;
                }
                if (node[x[k]][y[k]] == node[x0][y0]) {
                    counters[k]++;
                    if (counters[k] >= m) {
                        return true;
                    }
                } else {
                    counters[k] = 0;
                }
            }
        }
        return false;
    }

    private static int heuristicBasedOnPlayArea(
            int[][] node, int[] playArea, int turn, int rowLenght, int nodeDepth) {
        int n = node.length - 1;
        int[] playerValues = new int[2];
        for (int i = 1; i <= n; i++) {
            int[][][] counters = new int[2][6][rowLenght + 1];
            int offset = 0;
            int[][] marksOnRange = new int[2][n];
            for (int j = 1; j <= n; j++) {
                int[] x = new int[]{i, j, (i + offset), (i + offset - n), j, j};
                int[] y = new int[]{j, i, j, j, i - offset, i - offset + n};
                for (int k = 0; k < x.length; k++) {
                    if (x[k] < 1 || x[k] > n || y[k] < 1 || y[k] > n
                            || x[k] < playArea[0] || x[k] > playArea[2]
                            || y[k] < playArea[1] || y[k] > playArea[3]) {
                        continue;
                    }
                    for (int p = 0; p < 2; p++) {
                        if (node[x[k]][y[k]] >= 0) {
                            if (node[x[k]][y[k]] == p) {
                                if (marksOnRange[p][k] < rowLenght) {
                                    counters[p][k][++marksOnRange[p][k]] = rowLenght;
                                }
                            }
                        }
                        if (marksOnRange[p][k] > 0
                                && observedRangeFullyOnBoard(x, y, k, n, rowLenght)
                                && marksOnRange[((p + 1) % 2)][k] == 0) {
                            playerValues[p] += Math.pow(10, marksOnRange[p][k] - 1) - nodeDepth - ((p + turn) % 2);

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
        return playerValues[0] - playerValues[1];
    }

    /**
     * Reduces counter value in all indexes of an array unless value is already
     * 0.
     *
     * @param marks counter array
     * @return updated counter array
     */
    public static int[] reduceMarkCountersByOne(int[] marks) {
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
    private static boolean observedRangeFullyOnBoard(int[] x, int[] y, int k, int n, int rowLenght) {
        return (k == 0 && y[k] >= rowLenght)
                || (k == 1 && x[k] >= rowLenght)
                || ((k == 2 || k == 3) && x[k] >= rowLenght && y[k] >= rowLenght)
                || ((k == 4 || k == 5) && x[k] >= rowLenght && y[k] <= n - rowLenght + 1);
    }
 
    /**
     * Experimental, alternative method to calculate heuristic value.
     */
    //CHECKSTYLE:OFF
    @Deprecated
    private static int heuristicBasedOnLastMove(
            int[][] node, int[] playArea, int[] move, int turn, int rowLenght) {
        int n = node.length - 1;
        int x0 = move[0];
        int y0 = move[1];
        int[][][] counters = new int[2][4][rowLenght + 1];
        int[][] marksOnRange = new int[2][n];
        int[] playerValues = new int[2];
        for (int i = 0; i < n; i++) {
            int delta = i - (n - 1);
            int[] x = new int[]{x0 + delta, x0, x0 + delta, x0 + delta};
            int[] y = new int[]{y0, y0 - delta, y0 + delta, y0 - delta};
            for (int k = 0; k < counters[0].length; k++) {
                if ((x[k] < 1 || x[k] > n || y[k] < 1 || y[k] > n)
                        || (x[k] < playArea[0] || x[k] > playArea[2]
                        || y[k] < playArea[1] || y[k] > playArea[3])) {
                    continue;
                }
                int p = turn;
                if (node[x[k]][y[k]] == turn) {
                    if (marksOnRange[p][k] < rowLenght) {
                        counters[p][k][++marksOnRange[p][k]] = rowLenght;
                    }
                }
                if (marksOnRange[p][k] == rowLenght) {
                    playerValues[p] = (int) 1e9;
                    break;
                }
                if (marksOnRange[p][k] > 0 && observedRangeFullyOnBoard(x, y, k, n, rowLenght)
                        && marksOnRange[((p + 1) % 2)][k] == 0) {
                    playerValues[p] += Math.pow(10, marksOnRange[p][k] - 1);
                }
                counters[p][k] = reduceMarkCountersByOne(counters[p][k]);
                if (marksOnRange[p][k] > 0 && counters[p][k][marksOnRange[p][k]] == 0) {
                    marksOnRange[p][k]--;
                }
            }
        }
        int value = playerValues[turn];
        return (turn == 0) ? value : -value;
    }
    //CHECKSTYLE:ON
}
