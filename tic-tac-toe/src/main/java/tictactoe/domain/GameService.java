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

    /*
     * Start new game. Method invokes initialization of game board and adds
     * players to game data.
     * @param gameBoardSize Number of tiles in an edge of square game board. 
     * Resulting board will have n*n tiles.
     * @param marksToWin Number of marks player has to get in a row 
     * (horizontally, vertically or diagonally) to win. marksToWin is 
     * intended to be used as 5 but may as well be smaller or larger than that.
     */
 /* public void startNewGame(int gameBoardSize, int marksToWin) {
        this.gameData.initializeGameBoard(gameBoardSize, marksToWin);
        this.gameData.addPlayer(new Player("X", Color.STEELBLUE));
        this.gameData.addPlayer(new Player("O", Color.TOMATO));
        /* Notice that one could add even more players if wish. It would be 
         * logical to take players as an input as well just like board size 
         * and marks to win.
     */
    //}
    public void startNewGame(RuleBook rules) {
        this.gameData.initializeGameBoard(rules);
        //dao.setGameBoard(new GameBoard(rules.getBoardsize()));
    }

    /**
     * Starts the game with default values (game board 20*20 in size, 5-in-row
     * needed to win)
     */
    public void startNewGame() {
        this.startNewGame(new RuleBook(20, 5,
                new Player[]{new Player("X"), new Player("O")}));
    }

    /*
     * Get number of tiles in an edge of the square game board.
     *
     * @return Number of tiles in an edge of game board.
     */
    /*public int getGameBoardSize() {
        return gameData.getGameBoardSize();
    }*/

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
        int n = gameData.getGameBoardSize();
        if (x < 1 || y < 1 || x > n || y > n) {
            return false;
        }
        return gameData.getMoveAt(x, y) == null;
    }

    /* Notice that also UI needs to call same methods as makeMove
     * in order to know if move was valid or not and if game continues
     * after this. This could be improved by communicating the game state
     * to ui after makeMove() is called delivering the same info
     * I.e., was move valid? is game over? Was it a draw 
     * or did player 1 or 2 win?
     */
 /*
     * Store players move if valid and change turn if game continues.
     *
     * @param x X-coordinate of the move on game board, first valid starting
     * from 1.
     * @param y Y-coordinate of the move on game board, first valid starting
     * from 1.
     */
 /*public void makeMove(int x, int y) {
        if (this.validMove(x, y)) {
            gameData.setMove(new Move(this.getCurrentPlayer(), x, y));
            if (!this.gameOver()) {
                this.changeTurn();
            }
        }
    }*/
    public void makeMove(int x, int y) {
        if (this.validMove(x, y)) {
            gameData.setMove(new Move(this.getCurrentPlayer(), x, y));
        }
        if (!gameOver(this.gameData.getGameBoard(), this.getRules())) {
            this.changeTurn();
        }
    }

    /* This method violates checkstyle rule of max 20 lines (->split?).
     * Additionally, efficiency of the method could be improved
     * by limiting the checks focus only area with k-radius from the latest move.
     * Works good enough for now.
     */
    /*
     * Check if current player has won or not. In other words, does she have
     * k-in-row (k=1,2,3,...) horizontally, vertically or diagonally.
     *
     * @return True if current player in turn has won.
     */
    /*public boolean currentPlayerWin() {
        int gameBoardSize = gameData.getGameBoardSize();

        for (int i = 1; i <= gameBoardSize; i++) {
            int markCountRow = 0, markCountCol = 0, markCountDiag1 = 0,
                    markCountDiag2 = 0, diagOffset = 0;

            for (int j = 1; j <= gameBoardSize; j++) {
                Move moveRow = gameData.getMoveAt(i, j);
                Move moveCol = gameData.getMoveAt(j, i);
                markCountRow = updateMarkCounter(markCountRow, moveRow);
                markCountCol = updateMarkCounter(markCountCol, moveCol);

                if (i + diagOffset <= gameBoardSize) {
                    Move moveDiag1 = gameData.getMoveAt(i + diagOffset, j);
                    markCountDiag1 = updateMarkCounter(markCountDiag1, moveDiag1);
                }
                if (i - diagOffset > 0) {
                    Move moveDiag2 = gameData.getMoveAt(j, i - diagOffset);
                    markCountDiag2 = updateMarkCounter(markCountDiag2, moveDiag2);
                }
                diagOffset++;

                if (markCountRow >= 5 || markCountCol >= 5
                        || markCountDiag1 >= 5 || markCountDiag2 >= 5) {
                    return true;
                }
            }
        }
        return false;
    }*/
    public static boolean validMove(GameBoard board, int x, int y) {
        int n = board.getSize();
        return (x > 0 && y > 0 && x <= n && y <= n && board.getMove(x, y) == null);
    }

    public static Player getWinningPlayer(GameBoard board, RuleBook rules) {
        int n = board.getSize();
        Player[] players = rules.getPlayers();
        for (int i = 1; i <= n; i++) {
            /*
             * Counters: first index is for player second for direction: 
             * 0 horizontal, 1 vertical, 2 diagonal1, 3 diagonal2
             */
            int[][] counters = new int[players.length][4]; //[][]
            int offset = 0;
            for (int j = 1; j <= n; j++) {
                Move[] moves = new Move[]{board.getMove(i, j), board.getMove(j, i),
                    board.getMove(i + offset, j), board.getMove(j, i - offset)};
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

    /*private int updateMarkCounter(int currentValue, Move move) {
        if (move != null && move.getPlayer().equals(this.getCurrentPlayer())) {
            return currentValue + 1;
        }
        return 0;
    }*/

    /**
     * Checks if game is over (has current player win or is the game board full
     * (i.e. draw)
     *
     * @return Returns true if game is over, otherwise true
     */
    public boolean gameOver() {
        return gameOver(this.gameData.getGameBoard(), this.getRules());
        /*if (this.currentPlayerWin()) {
            return true;
        }
        return this.gameBoardFull();*/
    }
    
    public Player getWinningPlayer() {
        return getWinningPlayer(this.gameData.getGameBoard(), this.getRules());
    }

}
