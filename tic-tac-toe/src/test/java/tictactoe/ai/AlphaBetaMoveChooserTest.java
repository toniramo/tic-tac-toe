package tictactoe.ai;

import java.util.Random;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * AlphaBetaMoveChooser tests.
 */
public class AlphaBetaMoveChooserTest {

    /*
     * Since AlphaBetaMoveChooser has only static methods,
     * report considers that class declaration line is not covered without this.
     */
    @Test
    public void testClassDeclaration() {
        AlphaBetaMoveChooser chooser = new AlphaBetaMoveChooser();
        assertEquals(chooser.getClass(), AlphaBetaMoveChooser.class);
    }

    @Test
    public void winningHorizontalMoveIsChosenV1() {
        int[][] node = initializeNode(20);
        node[10][10] = 0;
        node[11][10] = 0;
        node[12][10] = 0;
        node[13][10] = 0;

        node[9][10] = 1;
        node[10][11] = 1;
        node[13][11] = 1;
        node[10][12] = 1;

        int[] move = AlphaBetaMoveChooser.getMove(node, new int[]{8, 9, 14, 13}, 8, 0, 5, (int) -1e9, (int) 1e9, 1);
        assertTrue(move[0] == 14 && move[1] == 10);
    }

    @Test
    public void winningHorizontalMoveIsChosenV2() {
        int[][] node = initializeNode(20);
        node[10][10] = 1;
        node[11][10] = 1;
        node[12][10] = 1;
        node[13][10] = 1;

        node[9][10] = 0;
        node[10][11] = 0;
        node[13][11] = 0;
        node[10][12] = 0;

        int[] move = AlphaBetaMoveChooser.getMove(node, new int[]{8, 9, 14, 13}, 8, 1, 5, (int) -1e9, (int) 1e9, 1);
        assertTrue(move[0] == 14 && move[1] == 10);
    }

    @Test
    public void winningHorizontalMoveIsChosenV3() {
        int[][] node = initializeNode(20);
        int x0 = 1;
        int y0 = 1;
        int direction = 0;
        int player = 0;
        createKInRow(node, player, direction, 20, 4, x0, y0, 11);

        int[] move = AlphaBetaMoveChooser.getMove(node, new int[]{1, 1, 20, 20}, 8, 0, 5, (int) -1e9, (int) 1e9, 1);
        assertTrue(move[0] == (x0 + 4) && move[1] == y0);
    }

    @Test
    public void winningHorizontalMoveIsChosenV4() {
        int[][] node = initializeNode(20);
        int x0 = 15;
        int y0 = 20;
        int direction = 0;
        int player = 0;
        createKInRow(node, player, direction, 20, 4, x0, y0, 13);

        int[] move = AlphaBetaMoveChooser.getMove(node, new int[]{1, 1, 20, 20}, 8, 0, 5, (int) -1e9, (int) 1e9, 1);
        assertTrue((move[0] == (x0 + 4) && move[1] == y0) || (move[0] == (x0 - 1) && move[1] == (y0)));
    }

    @Test
    public void winningVerticalMoveIsChosenV1() {
        int[][] node = initializeNode(20);
        int x0 = 9;
        int y0 = 6;
        int direction = 1;
        int player = 1;
        createKInRow(node, player, direction, 20, 4, x0, y0, 13);

        int[] move = AlphaBetaMoveChooser.getMove(node, new int[]{1, 1, 20, 20}, 8, 1, 5, (int) -1e9, (int) 1e9, 1);
        assertTrue((move[0] == x0 && move[1] == y0 + 4) || (move[0] == x0 && move[1] == y0 - 1));
    }

    @Test
    public void winningVerticalMoveIsChosenV2() {
        int[][] node = initializeNode(20);
        int x0 = 20;
        int y0 = 14;
        int direction = 1;
        int player = 1;
        createKInRow(node, player, direction, 20, 4, x0, y0, 14);

        int[] move = AlphaBetaMoveChooser.getMove(node, new int[]{1, 1, 20, 20}, 8, 1, 5, (int) -1e9, (int) 1e9, 1);
        assertTrue((move[0] == x0 && move[1] == y0 + 4) || (move[0] == x0 && move[1] == y0 - 1));
    }

    @Test
    public void winningDiagonalMoveIsChosenV1() {
        int[][] node = initializeNode(20);
        int x0 = 10;
        int y0 = 10;
        int direction = 2;
        int player = 1;
        createKInRow(node, player, direction, 20, 4, x0, y0, 13);

        int[] move = AlphaBetaMoveChooser.getMoveWithOptimizedSearchDepth(node, new int[]{1, 1, 20, 20}, 8, 1, 5, (int) -1e9, (int) 1e9);
        assertTrue((move[0] == x0 + 4 && move[1] == y0 + 4) || (move[0] == x0 - 1 && move[1] == y0 - 1));
    }

    @Test
    public void winningDiagonalMoveIsChosenV2() {
        int[][] node = initializeNode(20);
        int x0 = 10;
        int y0 = 10;
        int direction = 3;
        int player = 1;
        createKInRow(node, player, direction, 20, 4, x0, y0, 12);

        int[] move = AlphaBetaMoveChooser.getMoveWithOptimizedSearchDepth(node, new int[]{1, 1, 20, 20}, 8, 1, 5, (int) -1e9, (int) 1e9);
        assertTrue((move[0] == x0 + 4 && move[1] == y0 - 4) || (move[0] == x0 - 1 && move[1] == y0 + 1));
    }

    @Test
    public void winningMoveIsCounteredV1() {
        int[][] node = initializeNode(20);
        node[10][10] = 0;
        node[11][10] = 0;
        node[12][10] = 0;
        node[13][10] = 0;

        node[9][10] = 1;
        node[10][11] = 1;
        node[13][11] = 1;
        node[10][12] = 1;

        int[] move = AlphaBetaMoveChooser.getMoveWithOptimizedSearchDepth(node, new int[]{8, 9, 14, 13}, 8, 1, 5, (int) -1e9, (int) 1e9);
        assertTrue(move[0] == 14 && move[1] == 10);
    }

    @Test
    public void winningMoveIsCounteredV2() {
        int[][] node = initializeNode(20);
        node[10][10] = 1;
        node[11][10] = 1;
        node[12][10] = 1;
        node[13][10] = 1;

        node[9][10] = 0;
        node[10][11] = 0;
        node[13][11] = 0;
        node[10][12] = 0;

        int[] move = AlphaBetaMoveChooser.getMoveWithOptimizedSearchDepth(node, new int[]{8, 9, 14, 13}, 8, 0, 5, (int) -1e9, (int) 1e9);
        assertTrue(move[0] == 14 && move[1] == 10);
    }

    @Test
    public void winningHorizontalMoveIsCountered() {
        int[][] node = initializeNode(20);
        int x0 = 8;
        int y0 = 11;
        int direction = 0;
        int player = 0;
        int turn = 1;
        int k = 3;
        createKInRow(node, player, direction, 20, k, x0, y0, 27);

        int[] move = AlphaBetaMoveChooser.getMoveWithOptimizedSearchDepth(node, new int[]{1, 1, 20, 20}, 8, turn, 5, (int) -1e9, (int) 1e9);
        assertTrue((move[0] == x0 + k && move[1] == y0) || (move[0] == x0 - 1 && move[1] == y0));
    }

    @Test
    public void winningVerticalMoveIsCountered() {
        int[][] node = initializeNode(20);
        int x0 = 10;
        int y0 = 10;
        int direction = 1;
        int player = 0;
        int turn = 1;
        int k = 3;
        createKInRow(node, player, direction, 20, k, x0, y0, 27);

        int[] move = AlphaBetaMoveChooser.getMoveWithOptimizedSearchDepth(node, new int[]{1, 1, 20, 20}, 8, turn, 5, (int) -1e9, (int) 1e9);
        assertTrue((move[0] == x0 && move[1] == y0 + k) || (move[0] == x0 && move[1] == y0 - 1));
    }

    @Test
    public void winningDiagonalMoveIsCounteredV1() {
        int[][] node = initializeNode(20);
        int x0 = 5;
        int y0 = 5;
        int direction = 2;
        int player = 0;
        int turn = 1;
        int k = 3;
        createKInRow(node, player, direction, 20, k, x0, y0, 27);

        int[] move = AlphaBetaMoveChooser.getMoveWithOptimizedSearchDepth(node, new int[]{1, 1, 20, 20}, 8, turn, 5, (int) -1e9, (int) 1e9);
        assertTrue((move[0] == x0 + k && move[1] == y0 + k) || (move[0] == x0 - 1 && move[1] == y0 - 1));
    }

    @Test
    public void winningDiagonalMoveIsCounteredV2() {
        int[][] node = initializeNode(20);
        int x0 = 5;
        int y0 = 13;
        int direction = 3;
        int player = 1;
        int turn = 0;
        int k = 3;
        createKInRow(node, player, direction, 20, k, x0, y0, 27);

        int[] move = AlphaBetaMoveChooser.getMoveWithOptimizedSearchDepth(node, new int[]{1, 1, 20, 20}, 8, turn, 5, (int) -1e9, (int) 1e9);
        assertTrue((move[0] == x0 + k && move[1] == y0 - k) || (move[0] == x0 - 1 && move[1] == y0 + 1));
    }

    @Test
    public void optimizedSearchDepthWithPlayAreaOf3x3With1MoveReturns4() {
        assertEquals(AlphaBetaMoveChooser.optimizedMaxSearchDepth(new int[]{3, 3, 5, 5}, 1), 4);
    }

    @Test
    public void optimizedSearchDepthWithPlayAreaOf3x4With3MoveReturns3() {
        assertEquals(AlphaBetaMoveChooser.optimizedMaxSearchDepth(new int[]{3, 3, 6, 6}, 3), 3);
    }

    @Test
    public void optimizedSearchDepthWithPlayAreaOf6x7ith6MoveReturns3() {
        assertEquals(AlphaBetaMoveChooser.optimizedMaxSearchDepth(new int[]{3, 3, 8, 9}, 6), 3);
    }

    @Test
    public void optimizedSearchDepthWithPlayAreaOf6x7ith5MoveReturns2() {
        assertEquals(AlphaBetaMoveChooser.optimizedMaxSearchDepth(new int[]{3, 3, 8, 9}, 5), 2);
    }

    @Test
    public void optimizedSearchDepthWithPlayAreaOf7x13ith14MoveReturns2() {
        assertEquals(AlphaBetaMoveChooser.optimizedMaxSearchDepth(new int[]{3, 3, 9, 15}, 15), 2);
    }

    @Test
    public void optimizedSearchDepthWithPlayAreaOf7x13ith13MoveReturns1() {
        assertEquals(AlphaBetaMoveChooser.optimizedMaxSearchDepth(new int[]{3, 3, 9, 15}, 14), 1);
    }

    @Test
    public void ifGameBoardIsFullChooserReturnsZeros() {
        int[][] node = initializeNode(3);
        Assert.assertArrayEquals(AlphaBetaMoveChooser.getMove(
                node, new int[]{1, 1, 3, 3}, 4, 0, 3, (int) -1e9, (int) 1e9, 3), new int[]{0, 0});
    }

    @Test
    public void moveLeadsToDraw() {
        int[][] node = initializeNode(2);
        node[1][1] = 0;
        node[1][2] = 0;
        node[2][1] = 1;
        Assert.assertArrayEquals(AlphaBetaMoveChooser.getMove(
                node, new int[]{1, 1, 3, 3}, 3, 1, 3, (int) -1e9, (int) 1e9, 3), new int[]{2, 2});
    }

    @Test
    public void chooserChoosesFirstMove() {
        int[][] node = initializeNode(3);
        Assert.assertArrayEquals(AlphaBetaMoveChooser.getMove(
                node, new int[]{2, 2, 2, 2}, 0, 0, 3, (int) -1e9, (int) 1e9, 9), new int[]{2, 2});
    }

    private int[][] initializeNode(int n) {
        int[][] node = new int[n + 1][n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                node[i][j] = -1;
            }
        }
        return node;
    }

    /**
     * Creates test node in which one player is about to win and another has
     * marks in various corners of board.
     */
    private void createKInRow(int[][] node, int player, int direction, int n, int k, int x0, int y0, int seed) {
        for (int i = 0; i < k; i++) {
            int x = getNextX(x0, direction, i);
            int y = getNextY(y0, direction, i);
            node[x][y] = player;
        }
        generateRandomMoves(node, (player + 1) % 2, n, k - 1, seed);
    }

    /**
     * Gets next x-coordinate when moving on line in chosen direction.
     *
     * @param x0 x-coordinate of starting point
     * @param direction direction in which row is formed ( 0: horizontal / right
     * to left 1: vertical / down to up 2: diagonal / down left to up right 3:
     * diagonal / up left to down right)
     * @param i index in row
     * @return next x-coordinate
     */
    private int getNextX(int x0, int direction, int i) {
        if (direction == 1) {
            return x0;
        }
        return x0 + i;
    }

    /**
     * Gets next y-coordinate when moving on line in chosen direction.
     *
     * @param y0 y-coordinate of starting point
     * @param direction direction in which row is formed ( 0: horizontal / right
     * to left 1: vertical / down to up 2: diagonal / down left to up right 3:
     * diagonal / up left to down right)
     * @param i index in row
     * @return next y-coordinate
     */
    private int getNextY(int y0, int direction, int i) {
        if (direction == 0) {
            return y0;
        }
        if (direction == 1 || direction == 2) {
            return y0 + i;
        }
        return y0 - i;
    }

    /**
     * Generates m random moves on board (uses random seed to get repeatable
     * results).
     *
     * @param node status of game board
     * @param player player whose marks are located on board
     * @param n board size (width / length)
     * @param m number generated moves
     */
    private void generateRandomMoves(int[][] node, int player, int n, int m, int seed) {
        Random random = new Random(seed);
        int j = 0;
        while (j <= m) {
            int x = random.nextInt(n) + 1;
            int y = random.nextInt(n) + 1;
            if (node[x][y] < 0) {
                node[x][y] = player;
                j++;
            }
        }
    }
}
