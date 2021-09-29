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
public class TicTacToeNodeTest {

    private RuleBook rules;
    private GameBoard board;
    private TicTacToeNode node;

    public TicTacToeNodeTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        //public TicTacToeNode(GameBoard board, RuleBook rules, int turn, boolean minimizingNode,
        this.rules = new RuleBook(20, 5, new Player[]{new Player("X"), new Player("O")});
        this.board = new GameBoard(rules.getBoardsize());

    }

    @After
    public void tearDown() {
    }

    @Test
    public void moveInMiddleReturnValueOf20() {
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 10, 10));
        node = new TicTacToeNode(board, rules, 0, false);
        assertEquals(node.value(), 20);
    }

    @Test
    public void twoMovesFromSamePlayerInTheMiddleReturnValueOf72v1() {
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 10, 10));
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 10, 11));
        node = new TicTacToeNode(board, rules, 0, false);
        assertEquals(node.value(), 72);
    }

    @Test
    public void twoMovesFromSamePlayerInTheMiddleReturnValueOf72v2() {
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 10, 10));
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 11, 10));
        node = new TicTacToeNode(board, rules, 0, false);
        assertEquals(node.value(), 72);
    }

    @Test
    public void moveInCornerReturnsValueOf3v1() {
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 1, 1));
        node = new TicTacToeNode(board, rules, 0, false);
        assertEquals(node.value(), 3);
    }

    @Test
    public void moveInCornerReturnsValueOf3v2() {
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 20, 1));
        node = new TicTacToeNode(board, rules, 0, false);
        assertEquals(node.value(), 3);
    }

    @Test
    public void moveInCornerReturnsValueOf3v3() {
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 1, 20));
        node = new TicTacToeNode(board, rules, 0, false);
        assertEquals(node.value(), 3);
    }

    @Test
    public void moveInCornerReturnsValueOf3v4() {
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 20, 20));
        node = new TicTacToeNode(board, rules, 0, false);
        assertEquals(node.value(), 3);
    }

    @Test
    public void moveInCenterNextToEdgeReturnsValueOf8v1() {
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 1, 10));
        node = new TicTacToeNode(board, rules, 0, false);
        assertEquals(node.value(), 8);
    }

    @Test
    public void moveInCenterNextToEdgeReturnsValueOf8v2() {
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 10, 1));
        node = new TicTacToeNode(board, rules, 0, false);
        assertEquals(node.value(), 8);
    }

    @Test
    public void moveInCenterNextToEdgeReturnsValueOf8v3() {
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 20, 10));
        node = new TicTacToeNode(board, rules, 0, false);
        assertEquals(node.value(), 8);
    }

    @Test
    public void moveInCenterNextToEdgeReturnsValueOf8v4() {
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 10, 20));
        node = new TicTacToeNode(board, rules, 0, false);
        assertEquals(node.value(), 8);
    }

    @Test
    public void winningHorizontalRowOfMaximizingPlayerReturnsMaxValue() {
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 9, 10));
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 10, 10));
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 11, 10));
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 12, 10));
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 13, 10));

        node = new TicTacToeNode(board, rules, 0, false);
        assertEquals(node.value(), Integer.MAX_VALUE);
    }

    @Test
    public void winningHorizontalRowOfMinimizingPlayerReturnsMinValue() {
        board.setMove(new Move(rules.getPlayerBasedOnTurn(1), 9, 10));
        board.setMove(new Move(rules.getPlayerBasedOnTurn(1), 10, 10));
        board.setMove(new Move(rules.getPlayerBasedOnTurn(1), 11, 10));
        board.setMove(new Move(rules.getPlayerBasedOnTurn(1), 12, 10));
        board.setMove(new Move(rules.getPlayerBasedOnTurn(1), 13, 10));

        node = new TicTacToeNode(board, rules, 1, true);
        assertEquals(node.value(), Integer.MIN_VALUE);
    }

    @Test
    public void winningVerticalRowOfMaximizingPlayerReturnsMaxValue() {
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 9, 10));
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 9, 11));
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 9, 12));
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 9, 13));
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 9, 14));

        node = new TicTacToeNode(board, rules, 0, false);
        assertEquals(node.value(), Integer.MAX_VALUE);
    }

    @Test
    public void winningUpRightDiagonalRowOfMaximizingPlayerReturnsMaxValue() {
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 9, 10));
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 10, 11));
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 11, 12));
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 12, 13));
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 13, 14));

        node = new TicTacToeNode(board, rules, 0, false);
        assertEquals(node.value(), Integer.MAX_VALUE);
    }

    @Test
    public void winningDownRightDiagonalRowOfMaximizingPlayerReturnsMaxValue() {
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 9, 14));
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 10, 13));
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 11, 12));
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 12, 11));
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 13, 10));

        node = new TicTacToeNode(board, rules, 0, false);
        assertEquals(Integer.MAX_VALUE, node.value());
    }
}
