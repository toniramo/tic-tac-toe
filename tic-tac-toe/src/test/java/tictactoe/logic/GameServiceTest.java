package tictactoe.logic;

import java.util.Arrays;
import javafx.scene.paint.Color;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import tictactoe.dao.Dao;
import tictactoe.dao.InMemoryDao;

/**
 * Tests of GameService class.
 */
public class GameServiceTest {

    private GameService gameService;
    private Dao mockedDao;
    private int n = 20;
    private int k = 5;
    private Player player1 = new Player("X", Color.BLACK);
    private Player player2 = new Player("O", Color.GREEN);
    private RuleBook rules = new RuleBook(n, k, new Player[]{player1, player2});
    private GameBoard board = new GameBoard(rules.getBoardsize());

    public GameServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        mockedDao = mock(InMemoryDao.class);
        gameService = new GameService(mockedDao);
        gameService.startNewGame(rules);

        when(mockedDao.getRules()).thenReturn(rules);

        when(mockedDao.getGameBoard()).thenReturn(board);;
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getGameRulesReturnsCorrectRuleBook() {
        assertTrue(gameService.getRules().equals(rules));
    }

    @Test
    public void getCurrentPlayerReturnsCorrectPlayerInDao() {
        assertTrue(gameService.getCurrentPlayer().equals(player1));
    }

    @Test
    public void makeMoveRequestsToChangeTurn() {
        gameService.makeMove(1, 1);
        verify(mockedDao).setTurn(anyInt());
    }

    @Test
    public void makingValidMoveRequestsToSetItInDao() {
        int x = 1;
        int y = 2;
        Move move = new Move(player1, x, y);
        when(mockedDao.getMoveAt(x, y)).thenReturn(null);
        gameService.makeMove(x, y);
        verify(mockedDao, times(1)).setMove(argThat(new MoveMatcher(move)));
    }

    @Test
    public void tryingToPlaceMarkOnReservedSpotDoesNotRequestSettingItInDao() {
        int x = n / 2;
        int y = n;
        when(mockedDao.getMoveAt(x, y)).thenReturn(new Move(player1, x, y));
        gameService.makeMove(x, y);
        verify(mockedDao, times(0)).setMove(any(Move.class));
    }

    @Test
    public void tryingToMakeMoveOutOfBoundsDoesNotRequestSettingItInDao() {
        gameService.makeMove(0, n / 2);
        gameService.makeMove(n / 3, -n);
        gameService.makeMove(n + 1, n / 4);
        gameService.makeMove(n, n + 2);
        verify(mockedDao, times(0)).setMove(any(Move.class));
    }

    @Test
    public void noWinnerWithEmptyBoard() {
        board = new GameBoard(rules.getBoardsize());
        assertTrue(gameService.getWinningPlayer() == null);
    }

    @Test
    public void whenBoardFilledWithSecondPlayersMarksSecondPlayerWins() {
        fillGameBoardWithMovesOfChosenPlayer(player2, board);
        assertTrue(gameService.getWinningPlayer().equals(player2));
    }

    @Test
    public void whenBoardFilledWithFirstPlayersMarksFirstPlayerWins() {
        fillGameBoardWithMovesOfChosenPlayer(player1, board);
        assertTrue(gameService.getWinningPlayer().equals(player1));
    }

    @Test
    public void fiveinRowHorizontallyReturnsCorrectWinner() {
        board.setMove(new Move(player1, 10, 10));
        board.setMove(new Move(player1, 11, 10));
        board.setMove(new Move(player1, 12, 10));
        board.setMove(new Move(player1, 13, 10));
        board.setMove(new Move(player1, 14, 10));
        assertTrue(gameService.getWinningPlayer().equals(player1));
    }

    @Test
    public void fiveinRowVerticallyReturnsCorrectWinner() {
        board.setMove(new Move(player1, 10, 11));
        board.setMove(new Move(player1, 10, 12));
        board.setMove(new Move(player1, 10, 13));
        board.setMove(new Move(player1, 10, 14));
        board.setMove(new Move(player1, 10, 15));
        assertTrue(gameService.getWinningPlayer().equals(player1));
    }

    @Test
    public void fiveinRowDiagonallyReturnsCorrectWinnerV1() {
        board.setMove(new Move(player1, 10, 11));
        board.setMove(new Move(player1, 11, 12));
        board.setMove(new Move(player1, 12, 13));
        board.setMove(new Move(player1, 13, 14));
        board.setMove(new Move(player1, 14, 15));
        assertTrue(gameService.getWinningPlayer().equals(player1));
    }

    @Test
    public void fiveinRowDiagonallyReturnsCorrectWinnerV2() {
        board.setMove(new Move(player1, 14, 11));
        board.setMove(new Move(player1, 13, 10));
        board.setMove(new Move(player1, 12, 9));
        board.setMove(new Move(player1, 11, 8));
        board.setMove(new Move(player1, 10, 7));
        assertTrue(gameService.getWinningPlayer().equals(player1));
    }

    @Test
    public void fiveinRowDiagonallyReturnsCorrectWinningRow() {
        Move[] moves = new Move[]{new Move(player1, 14, 11),
            new Move(player1, 13, 10), new Move(player1, 12, 9),
            new Move(player1, 11, 8), new Move(player1, 10, 7)};
        board.setMove(moves[0]);
        board.setMove(moves[1]);
        board.setMove(moves[2]);
        board.setMove(moves[3]);
        board.setMove(moves[4]);

        Move[] row = gameService.getWinningRow();
        for (Move move : moves) {
            assertTrue(Arrays.asList(row).contains(move));
        }
    }
    
    @Test
    public void winningRowIsNullWithEmptyBoard() {
        assertEquals(null, gameService.getWinningRow());
    }

    private GameBoard fillGameBoardWithMovesOfChosenPlayer(Player player, GameBoard board) {
        for (int x = 1; x <= n; x++) {
            for (int y = 1; y <= n; y++) {
                board.setMove(new Move(player, x, y));
            }
        }
        return board;
    }

}
