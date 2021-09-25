package tictactoe.domain;

import javafx.scene.paint.Color;
import tictactoe.dao.Dao;

/**
 * Class is responsible for running the main game logic and passing information
 * between different levels of the program.
 */
public class GameService {

    private final Dao gameData;

    /**
     * Generate GameService object.
     *
     * @param dao data access object responsible for maintaining the game data.
     */
    public GameService(Dao dao) {
        this.gameData = dao;
    }

    /**
     * Start new game. Method invokes initialization of game board and adds
     * players to game data based on given rules.
     */
    public void startNewGame(RuleBook rules) {
        this.gameData.initializeGameBoard(rules);
    }

    /**
     * Starts the game with default rules (game board 20*20 in size, 5-in-row
     * needed to win, X and O players)
     */
    public void startNewGame() {
        this.startNewGame(new RuleBook(20, 5,
                new Player[]{new Player("X"), new Player("O")}));
    }

    public RuleBook getRules() {
        return gameData.getRules();
    }

    /**
     * Changes player in turn to the next one.
     */
    private void changeTurn() {
        gameData.changeTurn();
    }

    /**
     * Method to get current player in turn.
     *
     * @return Return Player object related to current player in turn.
     */
    public Player getCurrentPlayer() {
        return gameData.getCurrentPlayer();
    }

    /**
     * Store players move if valid and change turn if game continues.
     *
     * @param x X-coordinate of the move on game board, first valid starting
     * from 1.
     * @param y Y-coordinate of the move on game board, first valid starting
     * from 1.
     */
    public void makeMove(int x, int y) {
        if (this.validMove(x, y)) {
            gameData.setMove(new Move(this.getCurrentPlayer(), x, y));
        }
        if (!gameOver(this.gameData.getGameBoard(), this.getRules())) {
            this.changeTurn();
        }
    }

    /**
     * Checks if proposed move (x,y) is valid. In other words, is it within the
     * boundaries of the game board and is the chosen tile free.
     *
     * @param x X-coordinate of the move on game board, first valid starting
     * from 1.
     * @param y Y-coordinate of the move on game board, first valid starting
     * from 1.
     * @return Returns true if move is valid, otherwise false.
     */
    public boolean validMove(int x, int y) {
        int n = gameData.getRules().getBoardsize();
        if (x < 1 || y < 1 || x > n || y > n) {
            return false;
        }
        return gameData.getMoveAt(x, y) == null;
    }

    public boolean gameOver() {
        return gameOver(this.gameData.getGameBoard(), this.getRules());
    }

    public Player getWinningPlayer() {
        return getWinningPlayer(this.gameData.getGameBoard(), this.getRules());
    }

    public static boolean validMove(GameBoard board, int x, int y) {
        int n = board.getSize();
        return (x > 0 && y > 0 && x <= n && y <= n && board.getMove(x, y) == null);
    }

    /*
     * Method uses two-dimensional
     * "counters" array: first index is for player, second for direction: 
     * 0 vertical |, 1 horizontal -- , 
     * 2 diagonal1 / up-right (1/2), 3 diagonal1 / upright (2/2), 
     * 4 diagonal2 \ down-right (1/2),  5 diagonal2 / down-right (2/2).
     * This is same as in TicTacToeNode (consider ways to eliminate duplicate code)
     */
    public static Player getWinningPlayer(GameBoard board, RuleBook rules) {
        int n = board.getSize();
        Player[] players = rules.getPlayers();
        for (int i = 1; i <= n; i++) {
            int[][] counters = new int[players.length][6];
            int offset = 0;
            for (int j = 1; j <= n; j++) {
                int[] x = new int[]{i, j, (i + offset), (i + offset - n), j, j};
                int[] y = new int[]{j, i, j, j, i - offset, i - offset + n};
                Move[] moves = board.getMoves(x, y);
                for (int k = 0; k < counters[0].length; k++) {
                    for (int p = 0; p < players.length; p++) {
                        if (moves[k] != null && moves[k].getPlayer().equals(players[p])) {
                            counters[p][k]++;
                            if (counters[p][k] >= rules.getMarksToWin()) {
                                return players[p];
                            }
                        } else {
                            counters[p][k] = 0;
                        }
                    }
                }
                offset++;
            }
        }
        return null;
    }

    public static boolean gameOver(GameBoard board, RuleBook rules) {
        return getWinningPlayer(board, rules) != null
                || board.getNumberOfPlayedMoves() == Math.pow(board.getSize(), 2);
    }
}
