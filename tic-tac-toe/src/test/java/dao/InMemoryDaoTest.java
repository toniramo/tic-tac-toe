/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import javafx.scene.paint.Color;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import tictactoe.dao.Dao;
import tictactoe.dao.InMemoryDao;
import tictactoe.domain.Move;
import tictactoe.domain.Player;

/**
 *
 * @author toniramo
 */
public class InMemoryDaoTest {

    private int n1 = 20;
    private int n2 = 15;
    private int n3 = 30;
    private int k = 5;

    private int x1 = 4;
    private int y1 = 15;

    private Player player1 = new Player("X", Color.ALICEBLUE);
    private Player player2 = new Player("O");
    private Move move1 = new Move(player1, x1, y1);

    private Dao dao = new InMemoryDao();

    public InMemoryDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        dao.initializeGameBoard(n1, k);
        dao.addPlayer(player1);
        dao.addPlayer(player2);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getGameBoardSizeReturnsCorrectValue() {
        assertEquals(dao.getGameBoardSize(), n1);
        dao.initializeGameBoard(n2, k);
        assertEquals(dao.getGameBoardSize(), n2);
        dao.initializeGameBoard(n3, k);
        assertEquals(dao.getGameBoardSize(), n3);
    }

    @Test
    public void getMarksToWinReturnsCorrectValue() {
        assertEquals(dao.getNumberOfMarksToWin(), k);
        dao.initializeGameBoard(n2, ++k);
        assertEquals(dao.getNumberOfMarksToWin(), k);
        dao.initializeGameBoard(n3, 2 * k);
        assertEquals(dao.getNumberOfMarksToWin(), 2 * k);
    }

    @Test
    public void getMoveReturnsSetMove() {
        dao.setMove(move1);
        assertEquals(dao.getMove(x1, y1), move1);
    }

    @Test
    public void getMoveDoesNotReturnMoveThatIsNotSet() {
        assertEquals(dao.getMove(x1, y1), null);
    }

    @Test
    public void numberOfPlayedMovesIsInitiallyZero() {
        assertEquals(dao.getNumberOfPlayedMoves(), 0);
    }

    @Test
    public void getCurrentPlayerReturnsInitiallyFirstAddedPlayer() {
        assertEquals(dao.getCurrentPlayer(), player1);
    }

    @Test
    public void getCurrentPlayerReturnsSecondAddedAfterChangeTurnIsCalled() {
        dao.changeTurn();
        assertEquals(dao.getCurrentPlayer(), player2);
    }

}
