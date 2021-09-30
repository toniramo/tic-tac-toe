/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.ai;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import tictactoe.domain.GameBoard;
import tictactoe.domain.Move;
import tictactoe.domain.Player;
import tictactoe.domain.RuleBook;

/**
 *
 * @author toniramo
 */
public class AlphaBetaPrunerTest {

    private RuleBook rules;
    private GameBoard board;
    private GameTreeNode node;
    private int alpha = Integer.MIN_VALUE;
    private int beta = Integer.MAX_VALUE;

    public AlphaBetaPrunerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        this.rules = new RuleBook(20, 5, new Player[]{new Player("X"), new Player("O")});
        this.board = new GameBoard(rules.getBoardsize());
    }

    @After
    public void tearDown() {
    }

    @Test
    public void oneMarkMiddleOfBoardWithMaxDepth0Returns20() {
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 10, 10));
        node = new TicTacToeNode(board, rules, 1, true);
        assertEquals(AlphaBetaPruner.getNodeValue(node, alpha, beta, 0), 20);
    }

    @Test
    public void oneMarkMiddleOfBoardWithMaxDepth1Returns0() {
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 10, 10));
        node = new TicTacToeNode(board, rules, 1, true);
        assertEquals(AlphaBetaPruner.getNodeValue(node, alpha, beta, 1), 0);
    }

    @Test
    public void emptyBoardWithMaxDepth1Returns20() {
        node = new TicTacToeNode(board, rules, 0, false);
        assertEquals(AlphaBetaPruner.getNodeValue(node, alpha, beta, 1), 20);
    }

    @Test
    public void emptyBoardWithMaxDepth2Returns0() {
        node = new TicTacToeNode(board, rules, 0, false);
        assertEquals(AlphaBetaPruner.getNodeValue(node, alpha, beta, 2), 0);
    }

    /*@Test
    public void emptyBoardWithMaxDepth3Returns0() {
        node = new TicTacToeNode(board, rules, 0, false);
        assertEquals(AlphaBetaPruner.getNodeValue(node, alpha, beta, 3), 20);
    }*/

    @Test
    public void oneMarkCornerOfBoardWithMaxDepth0Returns3() {
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 1, 1));
        node = new TicTacToeNode(board, rules, 0, false);
        assertEquals(AlphaBetaPruner.getNodeValue(node, alpha, beta, 0), 3);
    }

    @Test
    public void oneMarkCornerOfBoardWithMaxDepth1ReturnsNeg17() {
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 1, 1));
        node = new TicTacToeNode(board, rules, 1, true);
        assertEquals(AlphaBetaPruner.getNodeValue(node, alpha, beta, 1), -17);
    }

}
