/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.ai;

import java.util.Random;
import javafx.scene.paint.Color;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import tictactoe.dao.InMemoryDao;
import tictactoe.logic.GameService;
import tictactoe.logic.Player;
import tictactoe.logic.RuleBook;

/**
 * AlphaBetaMoveChooser tests
 */
public class AlphaBetaMoveChooserTest {

    /* private Player humanPlayer1 = new Player("X", Color.TOMATO, Player.PlayerType.HUMAN);
    private Player humanPlayer2 = new Player("O", Color.STEELBLUE, Player.PlayerType.HUMAN);
    private Player aiPlayer1 = new Player("X", Color.TOMATO, Player.PlayerType.AI);
    private Player aiPlayer2 = new Player("O", Color.STEELBLUE, Player.PlayerType.AI);

    private RuleBook humanAndAI = new RuleBook(20, 5, new Player[]{humanPlayer1, aiPlayer2});
    private RuleBook AIandHuman = new RuleBook(20, 5, new Player[]{aiPlayer1, humanPlayer2});
    private RuleBook onlyAIs = new RuleBook(20, 5, new Player[]{aiPlayer1, aiPlayer2});
     
    private GameService gameService  = new GameService(new InMemoryDao());*/
    //this.gameService.startNewGame (AIandHuman);
    //public static int[] getMove(int[][] node, int[] playArea, int playedMoves, int turn, int rowLenght, int alpha, int beta, int maxSearchDepth)
    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    @Test
    public void winningMoveIsChosenWhenPossibleV1() {
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
    public void winningMoveIsChosenWhenPossibleV2() {
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
    public void winningMoveIsCounteredWhenPossibleV1() {
        int[][] node = initializeNode(20);
        node[10][10] = 0;
        node[11][10] = 0;
        node[12][10] = 0;
        node[13][10] = 0;

        node[9][10] = 1;
        node[10][11] = 1;
        node[13][11] = 1;
        node[10][12] = 1;

        int[] move = AlphaBetaMoveChooser.getMove(node, new int[]{8, 9, 14, 13}, 8, 1, 5, (int) -1e9, (int) 1e9, 1);
        assertTrue(move[0] == 14 && move[1] == 10);
    }

    @Test
    public void winningMoveIsCounteredWhenPossibleV2() {
        int[][] node = initializeNode(20);
        node[10][10] = 1;
        node[11][10] = 1;
        node[12][10] = 1;
        node[13][10] = 1;

        node[9][10] = 0;
        node[10][11] = 0;
        node[13][11] = 0;
        node[10][12] = 0;

        int[] move = AlphaBetaMoveChooser.getMove(node, new int[]{8, 9, 14, 13}, 8, 0, 5, (int) -1e9, (int) 1e9, 1);
        assertTrue(move[0] == 14 && move[1] == 10);
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
     * marks in various corners of board. Experimental, not yet finished. WIP-
     */
    private int[][] createAlmostWinnigNode(int player, int direction, int n, int x0, int y0) {
        int[][] node = new int[n + 1][n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                node[i][j] = -1;
            }
        }
        for (int i = 0; i < 4; i++) {
            int x = 0;
            int y = 0;
            if (direction == 0) {
                x = x0 + i;
                y = y0;
            }
            if (direction == 1) {
                x = x0;
                y = y0 + i;
            }
            if (direction == 2) {
                x = x0 + i;
                y = y0 + i;
            }
            if (direction == 3) {
                x = x0 + i;
                y = y0 - i;
            }
            node[x][y] = player;
        }
        int j = 0;
        int k = 0;
        while (j <= 5) {
            if (node[1 + k][1 + k] < 0) {
                node[1 + k][1 + k] = (player + 1) % 2;
                j++;
            }
            if (node[n - k][n - k] < 0) {
                node[n - k][n - k] = (player + 1) % 2;
                j++;
            }
            if (node[1 + k][n - k] < 0) {
                node[1 + k][n - k] = (player + 1) % 2;
                j++;
            }
            k++;
        }
        return node;
    }
}
