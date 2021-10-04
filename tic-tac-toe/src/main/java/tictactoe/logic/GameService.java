package tictactoe.logic;

import tictactoe.dao.Dao;

/**
 * Game service responsible for running the main game logic and passing
 * information between different levels of the program.
 */
public class GameService {

    private final Dao gameData;

    /**
     * Starts new active game service with link to given data access object.
     *
     * @param dao data access object responsible for maintaining the game data
     */
    public GameService(Dao dao) {
        this.gameData = dao;
    }

    /**
     * Starts new game. Method invokes initialization of game board and adds
     * players to game data based on given rules.
     *
     * @param rules rules of the game
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

    /**
     * Gets rule book based on which the game is defined
     *
     * @return rules of the game in question
     */
    public RuleBook getRules() {
        return gameData.getRules();
    }

    /**
     * Gets game board stored in game data.
     *
     * @return stored game board
     */
    public GameBoard getGameBoard() {
        return gameData.getGameBoard();
    }

    /**
     * Gets index of player in turn.
     *
     * @return index of player in turn
     */
    public int getTurn() {
        return gameData.getTurn();
    }

    /**
     * Changes player in turn to the next one.
     */
    private void changeTurn() {
        int turn = getTurn();
        if (turn == gameData.getRules().getPlayers().length - 1) {
            turn = 0;
        } else {
            turn++;
        }
        gameData.setTurn(turn);
    }

    /**
     * Gets current player in turn.
     *
     * @return player in turn
     */
    public Player getCurrentPlayer() {
        return gameData.getRules().getPlayerBasedOnTurn(getTurn());
    }

    /**
     * Stores players move if valid and change turn if game continues.
     *
     * @param x X-coordinate of the move on game board, first valid starting
     * from 1.
     * @param y Y-coordinate of the move on game board, first valid starting
     * from 1.
     */
    public void makeMove(int x, int y) {
        if (!this.validMove(x, y)) {
            return;
        }
        gameData.setMove(new Move(this.getCurrentPlayer(), x, y));
        this.changeTurn();
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

    /**
     * Checks if game is over. That is, has some of the players won, or is the
     * board full.
     *
     * @return true if game is over, otherwise false
     */
    public boolean gameOver() {

        return gameOver(this.gameData.getGameBoard(), this.getRules());
    }

    /**
     * Gets winning player with this game setup.
     *
     * @return winner if found, otherwise null
     */
    public Player getWinningPlayer() {
        return getWinningPlayer(this.gameData.getGameBoard(), this.getRules());
    }

    /**
     * Gets winning row if any.
     *
     * @return winning row if any, otherwise null
     */
    public Move[] getWinningRow() {
        int n = gameData.getRules().getMarksToWin();
        Move lastMove = gameData.getGameBoard().getLastMove();
        Player p = lastMove.getPlayer();
        Move[][] row = new Move[4][n];
        int x0 = lastMove.getX();
        int y0 = lastMove.getY();
        int[] counters = new int[4];
        for (int i = 0; i < 2 * n - 1; i++) {
            int delta = i - (n - 1);
            int[] x = new int[]{x0 + delta, x0, x0 + delta, x0 + delta};
            int[] y = new int[]{y0, y0 - delta, y0 + delta, y0 - delta};
            Move[] moves = gameData.getGameBoard().getMoves(x, y);
            for (int k = 0; k < counters.length; k++) {
                if (moves[k] != null && moves[k].getPlayer().equals(p)) {
                    row[k][counters[k]++] = moves[k];
                    if (counters[k] >= gameData.getRules().getMarksToWin()) {
                        return row[k];
                    }
                } else {
                    counters[k] = 0;
                }
            }
        }
        return null;
    }

    /**
     * Checks if proposed move is valid on given game board.
     *
     * @param board game board from which to check the move
     * @param x x-coordinate of the move
     * @param y y-coordinate of the move
     * @return true if proposed move is valid, otherwise false
     */
    public static boolean validMove(GameBoard board, int x, int y) {
        int n = board.getSize();
        return (x > 0 && y > 0 && x <= n && y <= n && board.getMove(x, y) == null);
    }

    /**
     * Gets winning player (if any yet) with given game board and rule book.
     * Method checks the area around the latest move on the game board in all
     * possible directions (vertical, horizontal, diagonal) and terminates if
     * latest move resulted in win.
     *
     * @param board game board to analyse
     * @param rules rules used in determining the winner
     * @return winner if found, otherwise null
     */
    public static Player getWinningPlayer(GameBoard board, RuleBook rules) {
        if (board.getNumberOfPlayedMoves() < rules.getMarksToWin()) {
            return null;
        }
        int n = rules.getMarksToWin();
        Move lastMove = board.getLastMove();
        Player p = lastMove.getPlayer();
        int x0 = lastMove.getX();
        int y0 = lastMove.getY();
        int[] counters = new int[4];
        for (int i = 0; i < 2 * n; i++) {
            int delta = i - (n - 1);
            int[] x = new int[]{x0 + delta, x0, x0 + delta, x0 + delta};
            int[] y = new int[]{y0, y0 - delta, y0 + delta, y0 - delta};
            Move[] moves = board.getMoves(x, y);
            for (int k = 0; k < counters.length; k++) {
                if (moves[k] != null && moves[k].getPlayer().equals(p)) {
                    counters[k]++;
                    if (counters[k] >= rules.getMarksToWin()) {
                        return p;
                    }
                } else {
                    counters[k] = 0;
                }
            };
        }
        return null;
    }

    /**
     * Checks whether game is over in given game board based on given rules.
     * Game is over if one of the players have got enough marks in a row
     * (determined by rules) or if game board is full.
     *
     * @param board game board to analyse
     * @param rules rules used in determining the winner
     * @return true if game is over, otherwise false
     */
    public static boolean gameOver(GameBoard board, RuleBook rules) {
        return getWinningPlayer(board, rules) != null
                || board.getNumberOfPlayedMoves() == Math.pow(board.getSize(), 2);
    }
}
