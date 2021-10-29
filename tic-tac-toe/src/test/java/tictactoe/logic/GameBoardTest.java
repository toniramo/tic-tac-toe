/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.logic;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author toniramo
 */
public class GameBoardTest {

    public GameBoardTest() {
    }

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
    public void getSizeReturnsSameSizeAsUsedInConstructor() {
        GameBoard board = new GameBoard(20);
        assertEquals(board.getSize(), 20);
    }

    @Test
    public void everyMoveIsNullIfBoardIsEmpty() {
        GameBoard board = new GameBoard(10);
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                assertNull(board.getMove(i, j));
            }
        }
    }

    @Test
    public void lastMoveIsNullIfBoardIsEmpty() {
        GameBoard board = new GameBoard(20);
        assertNull(board.getLastMove());
    }

    @Test
    public void numberOfPlayedMovesIsInitiallyZero() {
        GameBoard board = new GameBoard(20);
        assertEquals(board.getNumberOfPlayedMoves(), 0);
    }

    @Test
    public void getMoveReturnsCorrectMove() {
        GameBoard board = new GameBoard(20);
        Move move = new Move(new Player("O"), 4, 8);
        board.setMove(move);
        assertEquals(board.getMove(4, 8), move);
    }

    @Test
    public void getMovesReturnsCorrectMoves() {
        GameBoard board = new GameBoard(20);
        Move move1 = new Move(new Player("O"), 4, 8);
        Move move2 = new Move(new Player("X"), 6, 20);
        board.setMove(move1);
        board.setMove(move2);
        Assert.assertArrayEquals(board.getMoves(new int[]{4, 6}, new int[]{8, 20}),
                new Move[]{move1, move2});
    }

    @Test
    public void getCopyEqualsBoardFromWhichItIsCreated() {
        GameBoard board = new GameBoard(21);
        board.setMove(new Move(new Player("X"), 2, 20));
        board.setMove(new Move(new Player("O"), 1, 10));
        GameBoard copy = board.getCopy();
        for (int i = 1; i <= 21; i++) {
            for (int j = 1; j <= 21; j++) {
                if (copy.getMove(i, j) == null) {
                    assertNull(board.getMove(i, j));
                } else {
                    assertTrue(copy.getMove(i, j).equals(board.getMove(i, j)));
                }
            }
        }
    }

    @Test
    public void settingMovesIncreasesNumberOfPlayedMoves() {
        GameBoard board = new GameBoard(20);
        Move move1 = new Move(new Player("O"), 4, 8);
        Move move2 = new Move(new Player("X"), 6, 20);

        assertEquals(board.getNumberOfPlayedMoves(), 0);

        board.setMove(move1);
        assertEquals(board.getNumberOfPlayedMoves(), 1);

        board.setMove(move2);
        assertEquals(board.getNumberOfPlayedMoves(), 2);
    }

    @Test
    public void gameBoardIsCreatedCorrectlyUsingExistingState() {
        Player[][] state = new Player[21][21];
        Player player1 = new Player("X");
        Player player2 = new Player("O");

        state[1][1] = player1;
        state[2][2] = player1;
        state[10][10] = player2;
        state[20][20] = player2;

        GameBoard board = new GameBoard(state, 4, new Move(player2, 20, 20));
        assertEquals(board.getNumberOfPlayedMoves(), 4);
        for (int i = 1; i <= 20; i++) {
            for (int j = 1; j <= 20; j++) {
                if (state[i][j] == null) {
                    assertNull(board.getMove(i, j));
                } else {
                    assertEquals(board.getMove(i, j).getPlayer(), state[i][j]);
                }
            }
        }
    }
}
