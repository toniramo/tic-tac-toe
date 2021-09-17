package domain;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javafx.scene.paint.Color;
import org.junit.After;
import org.junit.AfterClass;
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
import tictactoe.domain.GameService;
import tictactoe.domain.Move;
import tictactoe.domain.Player;

/**
 *
 * @author toniramo
 */
public class GameServiceTest {

    private GameService gameService;
    private Dao mockedDao;
    private int n = 20;
    private int k = 5;
    private Player player1 = new Player("X", Color.BLACK);
    private Player player2 = new Player("O", Color.GREEN);

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
        gameService.startNewGame(n, k);

        when(mockedDao.getGameBoardSize()).thenReturn(n);
        setUpCurrentPlayer(player1);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void initializeGameBoardMethodOfDaoIsCalledWhenStartingGame() {
        verify(mockedDao).initializeGameBoard(n, k);
    }

    @Test
    public void getGameBoardSizeReturnsCorrectSize() {
        assertTrue(gameService.getGameBoardSize() == n);
    }

    @Test
    public void getCurrentPlayerReturnsCorrectPlayerInDao() {
        assertTrue(gameService.getCurrentPlayer().equals(player1));
    }

    @Test
    public void makeMoveRequestsToChangeTurn() {
        gameService.makeMove(1, 1);
        verify(mockedDao).changeTurn();
    }

    @Test
    public void makingValidMoveRequestsToSetItInDao() {
        int x = 1;
        int y = 2;
        Move move = new Move(player1, x, y);
        when(mockedDao.getMove(x, y)).thenReturn(null);
        when(mockedDao.getCurrentPlayer()).thenReturn(player1);
        gameService.makeMove(x, y);
        verify(mockedDao, times(1)).setMove(argThat(new MoveMatcher(move)));
    }

    @Test
    public void tryingToPlaceMarkOnReservedSpotDoesNotRequestSettingItInDao() {
        int x = n / 2;
        int y = n;
        when(mockedDao.getMove(x, y)).thenReturn(new Move(player1, x, y));
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
    public void currentPlayerWinReturnsFalseWithEmptyBoard() {
        when(mockedDao.getMove(anyInt(), anyInt())).thenReturn(null);
        assertFalse(gameService.currentPlayerWin());
    }

    @Test
    public void currentPlayerWinReturnsFalseIfBoardIsFilledWithMovesOfOtherPlayer() {
        fillGameBoardMockWithMovesOfChosenPlayer(player2);
        assertFalse(gameService.currentPlayerWin());
    }
    
    @Test
    public void currentPlayerWinsReturnsTrueIfBoardIsFilledWithMovesOfCurrentPlayer() {
        fillGameBoardMockWithMovesOfChosenPlayer(player1);
        assertTrue(gameService.currentPlayerWin());
    }

    private void fillGameBoardMockWithMovesOfChosenPlayer(Player player) {
        for (int x = 1; x <= n; x++) {
            for (int y = 1; y <= n; y++) {
                when(mockedDao.getMove(x, y)).thenReturn(new Move(player, x, y));
            }
        }
    }
    
    private void setUpCurrentPlayer(Player player) {
        when(mockedDao.getCurrentPlayer()).thenReturn(player);
    }
}
