package tictactoe.ai;

import tictactoe.annotations.ExcludeFromJacocoGeneratedReport;

/**
 * Implements minimax algorithm with alpha beta pruning and gets move with
 * highest estimated value
 */
public class AlphaBetaMoveChooser {

    /**
     * Gets most optimal move based on alpha beta pruning and heuristic value if
     * search depth is not enough to find desired end state. Algorithm expects
     * both players to play most optimal moves (that algorithm can find).
     *
     * @param node state of game board with -1 indicating free tile,
     * non-negative referring to player placed the mark
     * @param playArea area within which marks are at given moment located, used
     * to optimize search around already played marks
     * @param playedMoves number of moves already placed on board, used to check
     * if board is full
     * @param turn number of player in turn (0 or 1)
     * @param rowLenght number of marks in row needed to win
     * @param alpha alpha value, in the beginning usually very low value such as
     * -1e9
     * @param beta beta value, in the beginning usually very high value such as
     * 1e9
     * @param maxSearchDepth limits the search depth to chosen depth and
     * avoiding too exhaustive search. With current implementation already with
     * depth of 2 has notable effect on running time. Though, even with 1,
     * competitive moves can be found.
     * @return potentially most optimal move
     */
    public static int[] getMove(int[][] node, int[] playArea, int playedMoves,
            int turn, int rowLenght, int alpha, int beta, int maxSearchDepth) {
        int[] move = new int[2];
        int value = turn == 0 ? -(int) 1e9 : (int) 1e9;
        for (int x = playArea[0]; x <= playArea[2]; x++) {
            for (int y = playArea[1]; y <= playArea[3]; y++) {
                if (x > 0 && x < node.length && y > 0 && y < node[0].length && node[x][y] < 0) {
                    if (noNeighbours(node, playArea, x, y) && playedMoves > 0) {
                        continue;
                    }
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

    /**
     * Gets move based on optimized maximum search depth depending on available
     * free tiles. Method calls {@link #optimizedMaxSearchDepth(int[], int)} to
     * determine optmized depth and then
     * {@link #getMove(int[][], int[], int, int, int, int, int, int)} to get the
     * actual move.
     *
     * @param node state of game board with -1 indicating free tile,
     * non-negative referring to player placed the mark
     * @param playArea area within which marks are at given moment located, used
     * to optimize search around already played marks
     * @param playedMoves number of moves already placed on board, used to check
     * if board is full
     * @param turn number of player in turn (0 or 1)
     * @param rowLenght number of marks in row needed to win
     * @param alpha alpha value, in the beginning usually very low value such as
     * -1e9
     * @param beta beta value, in the beginning usually very high value such as
     * 1e9
     * @return potentially most optimal move
     */
    public static int[] getMoveWithOptimizedSearchDepth(int[][] node, int[] playArea, int playedMoves,
            int turn, int rowLenght, int alpha, int beta) {
        return getMove(node, playArea, playedMoves, turn, rowLenght, alpha, beta,
                optimizedMaxSearchDepth(playArea, playedMoves));
    }

    /**
     * Checks if chosen tile has neighbouring marks.
     *
     * @param node state of game board
     * @param area play area within which marks are located
     * @param x x-coordinate of observed tile
     * @param y y-coordinate of observed tile
     * @return true if tile has no neighbour, otherwise false
     */
    private static boolean noNeighbours(int[][] node, int[] area, int x, int y) {
        boolean result = true;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (x + dx < area[0] || x + dx > area[2] || y + dy < area[1] || y + dy > area[3]) {
                    continue;
                }
                if (node[x + dx][y + dy] >= 0) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Gets optimized maximum search depth in game tree based on free tiles on
     * play area.
     *
     * @param area Play area within which marks are located
     * @param playedMoves Number of played moves
     * @return maximum search depth based on free tiles on play area
     */
    private static int optimizedMaxSearchDepth(int[] area, int playedMoves) {
        int freeTiles = (area[3] - area[1] + 1) * (area[2] - area[0] + 1) - playedMoves;
        if (freeTiles < 9) {
            return 4;
        }
        if (freeTiles < 37) {
            return 3;
        }
        if (freeTiles < 77) {
            return 2;
        }
        return 1;
    }

    /**
     * Gets node value either based on end state if reached or heuristic, see
     * {@link #heuristicBasedOnPlayArea(int[][], int[], int, int, int)}.
     *
     * @param node state of game board with -1 indicating free tile,
     * non-negative referring to player placed the mark
     * @param playArea area within which marks are at given moment located, used
     * to optimize search around already played marks
     * @param playedMoves number of moves already placed on board, used to check
     * if board is full
     * @param turn number of player in turn (0 or 1)
     * @param rowLenght number of marks in row needed to win
     * @param alpha alpha value, in the beginning usually very low value such as
     * -1e9
     * @param beta beta value, in the beginning usually very high value such as
     * 1e9
     * @param maxSearchDepth limits the search depth to chosen depth and
     * avoiding too exhaustive search. With current implementation already with
     * depth of 2 has notable effect on running time. Though, even with 1,
     * competitive moves can be found.
     * @return alphabeta / heuristic value of given node
     */
    private static int value(int[][] node, int[] playArea, int[] move, int playedMoves,
            int turn, int rowLenght, int alpha, int beta, int nodeDepth, int maxSearchDepth) {
        int maxValue = (int) 1e9 - nodeDepth;
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
                    if (noNeighbours(node, playArea, x, y)) {
                        continue;
                    }
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

    /**
     * Increases given play area if needed (i.e. last move was placed next to on
     * of the area borders).
     *
     * @param playArea
     * @param x X-coordinate of given move
     * @param y Y-coordinate of given move
     * @return array of boolean values each representing on of the boards
     * indicating if board is moved by one (true) or not (false).
     */
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

    /**
     * Reverts play area back to previous state according to given boolean
     * array.
     *
     * @param playArea play area to revert
     * @param areaChanged array indicating which of the borders of game area was
     * moved
     */
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

    /**
     * Checks if game board is full (i.e. is game draW).
     *
     * @param node node to analyse
     * @param playedMoves number of played moves.
     * @return result of test wheter number of played moves matches with the
     * number of tiles on game board
     */
    private static boolean gameBoardFull(int[][] node, int playedMoves) {
        return Math.pow(node.length - 1, 2) == playedMoves;
    }

    /**
     * Checks if last move resulted to win in which case node is end state.
     *
     * @param node node to analyse
     * @param move latest move as an array in which integer in first index
     * represents x-coordinate of the move, and second y-coordinate
     * @param m number of marks in row needed to win
     * @return true if given move resulted in win, false otherwise
     */
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

    /**
     * Analyses play area with given node by going through it in 4 directions
     * (horizontal, vertical, diagonal) with moving window with length equal to
     * winning row length. Heuristic pursues to value higher - in folds of ten -
     * sections that have more marks (i.e. closer to win) and on the other hand
     * devalue (very little though) moves that have been achieved later in the
     * game (i.e. with deeper search depths). If observation window contains
     * marks of other player, its value is 0 for both players since there is no
     * chance of getting needed row with that section of the board. Value of the
     * whole play area is in the end calculated based on difference of both
     * player's values.
     *
     * @param node node to analyse
     * @param playArea play area within which played moves are found
     * @param turn player in turn (0 for first, 1 for second)
     * @param rowLenght number of marks in row needed to win
     * @param nodeDepth depth of analysed node, starting from 0
     * @return heuristic value of given node
     */
    private static int heuristicBasedOnPlayArea(
            int[][] node, int[] playArea, int turn, int rowLenght, int nodeDepth) {
        int n = node.length - 1;
        //int n = Math.max(playArea[2], playArea[3]);
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
                            playerValues[p] += Math.pow(10, marksOnRange[p][k] - 1) - nodeDepth; //- ((p + turn) % 2);
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
    @ExcludeFromJacocoGeneratedReport
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
