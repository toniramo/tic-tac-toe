package tictactoe.ai;

import javafx.scene.paint.Color;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import tictactoe.logic.GameBoard;
import tictactoe.logic.GameService;
import tictactoe.logic.Move;
import tictactoe.logic.Player;
import tictactoe.logic.Player.PlayerType;
import tictactoe.logic.RuleBook;

/**
 * Tests for AI class.
 */
public class AITest {

    private AI ai;
    private MockedStatic<AlphaBetaMoveChooser> alphaBeta;
    private GameService mockedService;

    @Before
    public void setUp() {

        alphaBeta = mockStatic(AlphaBetaMoveChooser.class);

        Player player1 = new Player("X", Color.BEIGE, PlayerType.AI);
        Player player2 = new Player("O");
        RuleBook rules = new RuleBook(20, 5, new Player[]{player1, player2});
        GameBoard emptyBoard = new GameBoard(rules.getBoardsize());
        mockedService = mock(GameService.class);
        when(mockedService.getRules()).thenReturn(rules);
        when(mockedService.getGameBoard()).thenReturn(emptyBoard);
        when(mockedService.getTurn()).thenReturn(0);

        this.ai = new AI(mockedService, player1);
    }

    @After
    public void tearDown() {
        alphaBeta.close();
    }

    @Test
    public void chooseMoveCallsAlphaBeta() {
        int[] move = new int[]{2, 2};
        alphaBeta.when(() -> AlphaBetaMoveChooser.getMoveWithOptimizedSearchDepth(any(int[][].class),
                any(int[].class), anyInt(), anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(move);
        ai.chooseMove();
        alphaBeta.verify(() -> AlphaBetaMoveChooser.getMoveWithOptimizedSearchDepth(
                any(int[][].class), any(int[].class), anyInt(), anyInt(), anyInt(), anyInt(), anyInt()), times(1));
    }

    @Test
    public void chooseMoveGetsMoveChosenByAlphaBeta() {
        int[] move = new int[]{2, 2};
        alphaBeta.when(() -> AlphaBetaMoveChooser.getMoveWithOptimizedSearchDepth(any(int[][].class),
                any(int[].class), anyInt(), anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(move);
        assertTrue(ai.chooseMove().equals(new Move(ai.getPlayer(), move[0], move[1])));
    }

    @Test
    public void duringFirstMoveAICallsChooserWithPlayAreaOfx10y10tox10y10() {
        int n = 20;
        int[] move = new int[]{2, 2};
        Move aisChoice;

        alphaBeta.when(() -> AlphaBetaMoveChooser.getMoveWithOptimizedSearchDepth(any(int[][].class),
                aryEq(new int[]{10, 10, 10, 10}), anyInt(), anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(move);
        try {
            aisChoice = ai.chooseMove();
        } catch (NullPointerException e) {
            aisChoice = null;
        }
        assertTrue(aisChoice != null && aisChoice.equals(new Move(ai.getPlayer(), move[0], move[1])));
    }

    @Test
    public void aiCallsMoveChooserWithUpdatedPlayAreaWhenItsNotFirstMove() {
        int n = 20;
        int[] move = new int[]{2, 2};
        Move aisChoice;

        GameBoard board = new GameBoard(20);
        board.setMove(new Move(new Player("O"), 3, 3));
        when(mockedService.getGameBoard()).thenReturn(board);

        alphaBeta.when(() -> AlphaBetaMoveChooser.getMoveWithOptimizedSearchDepth(any(int[][].class),
                aryEq(new int[]{2, 2, 4, 4}), anyInt(), anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(move);
        try {
            aisChoice = ai.chooseMove();
        } catch (NullPointerException e) {
            aisChoice = null;
        }
        assertTrue(aisChoice != null && aisChoice.equals(new Move(ai.getPlayer(), move[0], move[1])));
    }

    @Test
    public void playAreaCoordinatesAreInitiallyZero() {
        Assert.assertArrayEquals(ai.getPlayArea(), new int[]{0, 0, 0, 0});
    }

    @Test
    public void playAreaIsUpdatedCorrectlyWhenUpdatePlayAreaIsCalled() {
        GameBoard board = new GameBoard(20);
        board.setMove(new Move(ai.getPlayer(), 10, 10));
        when(mockedService.getGameBoard()).thenReturn(board);
        ai.updateStateBasedOnLastMove();
        Assert.assertArrayEquals(ai.getPlayArea(), new int[]{9, 9, 11, 11});
    }

    @Test
    public void playAreaIsUpdatedCorrectlyBasedOnChosenMove() {
        int[] move = new int[]{9, 13};
        alphaBeta.when(() -> AlphaBetaMoveChooser.getMoveWithOptimizedSearchDepth(any(int[][].class),
                any(int[].class), anyInt(), anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(move);
        ai.chooseMove();
        Assert.assertArrayEquals(ai.getPlayArea(), new int[]{8, 10, 10, 14});
    }

    @Test
    public void playAreaIsUpdatedCorrectlyBasedOnChosenMoveV2() {
        int[] move = new int[]{1, 1};
        alphaBeta.when(() -> AlphaBetaMoveChooser.getMoveWithOptimizedSearchDepth(any(int[][].class),
                any(int[].class), anyInt(), anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(move);
        ai.chooseMove();
        Assert.assertArrayEquals(ai.getPlayArea(), new int[]{1, 1, 10, 10});
    }

    @Test
    public void playAreaIsUpdatedCorrectlyBasedOnChosenMoveV3() {
        int[] move = new int[]{20, 20};
        alphaBeta.when(() -> AlphaBetaMoveChooser.getMoveWithOptimizedSearchDepth(any(int[][].class),
                any(int[].class), anyInt(), anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(move);
        ai.chooseMove();
        Assert.assertArrayEquals(ai.getPlayArea(), new int[]{10, 10, 20, 20});
    }
}
