package tictactoe.logic;

import java.util.Arrays;
import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
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

    @Before
    public void setUp() {
        mockedDao = mock(InMemoryDao.class);
        gameService = new GameService(mockedDao);
        gameService.startNewGame(rules);

        when(mockedDao.getRules()).thenReturn(rules);
        when(mockedDao.getGameBoard()).thenReturn(board);
    }

    @Test
    public void startNewGameWithoutParametersStartsGameWithDefaults() {
        Player player1 = new Player("X");
        Player player2 = new Player("O");
        RuleBook rules = new RuleBook(20, 5, new Player[]{player1, player2});
        gameService.startNewGame();
        assertTrue(gameService.getRules().equals(rules));
        assertTrue(gameService.getGameBoard().getSize() == 20
                && gameService.getGameBoard().getNumberOfPlayedMoves() == 0);
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
    public void makeMoveRequestsToChangeTurnFrom0To1() {
        when(mockedDao.getTurn()).thenReturn(0);
        gameService.makeMove(1, 1);
        verify(mockedDao).setTurn(1);
    }

    @Test
    public void makeMoveRequestsToChangeTurnFrom1To0() {
        when(mockedDao.getTurn()).thenReturn(1);
        gameService.makeMove(1, 1);
        verify(mockedDao).setTurn(0);
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
        gameService.makeMove(0, n / 2);      // x<0
        gameService.makeMove(n / 3, -n);     // y<0
        gameService.makeMove(n + 1, n / 4);  // x>n
        gameService.makeMove(n, n + 2);      // y>n
        verify(mockedDao, times(0)).setMove(any(Move.class));
    }

    @Test
    public void noWinnerWithEmptyBoard() {
        board = new GameBoard(rules.getBoardsize());
        assertTrue(gameService.getWinningPlayer() == null);
    }

    @Test
    public void gameIsOverWhenFirstPlayerHasWinningRow() {
        board.setMove(new Move(player1, 10, 1));
        board.setMove(new Move(player1, 11, 2));
        board.setMove(new Move(player1, 12, 3));
        board.setMove(new Move(player1, 13, 4));
        board.setMove(new Move(player1, 14, 5));
        assertTrue(gameService.gameOver());
    }

    @Test
    public void gameIsOverWhenSecondPlayerHasWinningRow() {
        board.setMove(new Move(player2, 1, 1));
        board.setMove(new Move(player2, 1, 2));
        board.setMove(new Move(player2, 1, 3));
        board.setMove(new Move(player2, 1, 4));
        board.setMove(new Move(player2, 1, 5));
        assertTrue(gameService.gameOver());
    }

    @Test
    public void fullBoardResultsInDraw() {
        /* 
           1 2 3
         3 O X O
         2 X O X
         1 X O X
         */
        GameBoard smallBoard = new GameBoard(3);
        smallBoard.setMove(new Move(player1, 2, 3));
        smallBoard.setMove(new Move(player1, 1, 2));
        smallBoard.setMove(new Move(player1, 1, 1));
        smallBoard.setMove(new Move(player1, 3, 2));
        smallBoard.setMove(new Move(player1, 3, 1));

        smallBoard.setMove(new Move(player2, 3, 1));
        smallBoard.setMove(new Move(player2, 3, 3));
        smallBoard.setMove(new Move(player2, 2, 2));
        smallBoard.setMove(new Move(player2, 2, 1));

        gameService.startNewGame(new RuleBook(3, 3, new Player[]{player1, player2}));
        when(mockedDao.getGameBoard()).thenReturn(smallBoard);
        assertTrue(gameService.gameOver()
                && gameService.getWinningPlayer() == null
                && gameService.getWinningRow() == null);
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
    public void validMoveReturnsTrueWhenMovesAreValid() {
        Move[] moves = new Move[]{new Move(player1, 14, 11),
            new Move(player1, 13, 10), new Move(player1, 12, 9),
            new Move(player1, 11, 8), new Move(player1, 10, 7)};
        board.setMove(moves[0]);
        board.setMove(moves[1]);
        board.setMove(moves[2]);
        board.setMove(moves[3]);
        board.setMove(moves[4]);
        for (Move move : moves) {
            when(mockedDao.getMoveAt(move.getX(), move.getY())).thenReturn(move);
        }
        assertTrue(gameService.validMove(20, 20));
        assertTrue(gameService.validMove(1, 1));
        assertTrue(gameService.validMove(1, 20));
        assertTrue(gameService.validMove(20, 1));
        assertTrue(gameService.validMove(10, 10));
    }

    @Test
    public void validMoveReturnsFalseWhenMovesAreInvalid() {
        Move[] moves = new Move[]{new Move(player1, 14, 11),
            new Move(player1, 13, 10), new Move(player1, 12, 9),
            new Move(player1, 11, 8), new Move(player1, 10, 7)};
        board.setMove(moves[0]);
        board.setMove(moves[1]);
        board.setMove(moves[2]);
        board.setMove(moves[3]);
        board.setMove(moves[4]);
        for (Move move : moves) {
            when(mockedDao.getMoveAt(move.getX(), move.getY())).thenReturn(move);
        }
        assertFalse(gameService.validMove(21, 20));
        assertFalse(gameService.validMove(20, 21));
        assertFalse(gameService.validMove(0, 1));
        assertFalse(gameService.validMove(1, 0));
        assertFalse(gameService.validMove(13, 10));
        assertFalse(gameService.validMove(10, 7));
        assertFalse(gameService.validMove(11, 8));
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
