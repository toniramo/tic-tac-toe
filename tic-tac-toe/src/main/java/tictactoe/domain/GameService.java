package tictactoe.domain;

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
    
    public GameBoard getGameBoard() {
        return gameData.getGameBoard();
    }
    
    public int getTurn() {
        return gameData.getNumberOfPlayedMoves() % gameData.getPlayers().length;
    }

    /**
     * Changes player in turn to the next one.
     */
    private void changeTurn() {
        gameData.changeTurn();
    }

    /**
     * Gets current player in turn.
     *
     * @return player in turn
     */
    public Player getCurrentPlayer() {
        return gameData.getCurrentPlayer();
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
     * Method goes simulatenously through the game board in all possible
     * directions (vertical, horizontal, diagonal) and terminates if player with
     * needed marks in row to win is found.
     *
     * @param board game board to analyze
     * @param rules rules used in determining the winner
     * @return winner if found, otherwise null
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

    /**
     * Checks wheter game is over in given game board based on given rules. Game
     * is over if one of the players have got enough marks in a row (determined
     * by rules) or if game board is full.
     *
     * @param board game board to analyze
     * @param rules rules used in determining the winner
     * @return true if game is over, otherwise false
     */
    public static boolean gameOver(GameBoard board, RuleBook rules) {
        return getWinningPlayer(board, rules) != null
                || board.getNumberOfPlayedMoves() == Math.pow(board.getSize(), 2);
    }
}
