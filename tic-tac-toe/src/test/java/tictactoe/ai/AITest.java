/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.ai;

import javafx.scene.paint.Color;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyObject;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import tictactoe.logic.GameBoard;
import tictactoe.logic.GameService;
import tictactoe.logic.Move;
import tictactoe.logic.Player;
import tictactoe.logic.Player.PlayerType;
import tictactoe.logic.RuleBook;

/**
 *
 * @author toniramo
 */
public class AITest {

    private AI ai;
    private MockedStatic<AlphaBetaMoveChooser> alphaBeta;
    private GameService mockedService;

    public AITest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

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
    
}
