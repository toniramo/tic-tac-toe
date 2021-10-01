package tictactoe.ai;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import tictactoe.logic.GameBoard;
import tictactoe.logic.Move;
import tictactoe.logic.Player;
import tictactoe.logic.RuleBook;

/**
 * Performs unit tests for GameTreeNodePruner class. 
 * However, since particular class in no longer developed further, tests are correspondingly not maintained.
 */
@Deprecated
public class GameTreeNodePrunerTest {

    private RuleBook rules;
    private GameBoard board;
    private GameTreeNode node;
    private int alpha = Integer.MIN_VALUE;
    private int beta = Integer.MAX_VALUE;

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
        assertEquals(GameTreeNodePruner.getNodeValue(node, alpha, beta, 0), 20);
    }

    @Test
    public void oneMarkMiddleOfBoardWithMaxDepth1Returns0() {
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 10, 10));
        node = new TicTacToeNode(board, rules, 1, true);
        assertEquals(GameTreeNodePruner.getNodeValue(node, alpha, beta, 1), 0);
    }

    @Test
    public void emptyBoardWithMaxDepth1Returns20() {
        node = new TicTacToeNode(board, rules, 0, false);
        assertEquals(GameTreeNodePruner.getNodeValue(node, alpha, beta, 1), 20);
    }

    @Test
    public void emptyBoardWithMaxDepth2Returns0() {
        node = new TicTacToeNode(board, rules, 0, false);
        assertEquals(GameTreeNodePruner.getNodeValue(node, alpha, beta, 2), 0);
    }

    /*
     * Too slow to search with depth 3. Comment test out.
     */
    /*
    @Test
    public void emptyBoardWithMaxDepth3Returns0() {
        node = new TicTacToeNode(board, rules, 0, false);
        assertEquals(AlphaBetaPruner.getNodeValue(node, alpha, beta, 3), 20);
    }*/
    
    @Test
    public void oneMarkCornerOfBoardWithMaxDepth0Returns3() {
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 1, 1));
        node = new TicTacToeNode(board, rules, 0, false);
        assertEquals(GameTreeNodePruner.getNodeValue(node, alpha, beta, 0), 3);
    }

    @Test
    public void oneMarkCornerOfBoardWithMaxDepth1ReturnsNeg17() {
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 1, 1));
        node = new TicTacToeNode(board, rules, 1, true);
        assertEquals(GameTreeNodePruner.getNodeValue(node, alpha, beta, 1), -17);
    }

    @Test
    public void oneMarkCornerOfBoardAndWithMaxDepth1ReturnsNegative31() {
        board.setMove(new Move(rules.getPlayerBasedOnTurn(0), 20, 1));
        node = new TicTacToeNode(board, rules, 0, true);
        assertEquals(GameTreeNodePruner.getNodeValue(node, alpha, beta, 1), -31);
    }

}
