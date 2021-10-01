package tictactoe.logic;

/**
 * Tic-tac-toe game board maintaining the moves made during the game.
 */
public class GameBoard {

    private Player[][] boardState;
    private Move lastMove;
    private int playedMoves;

    /**
     * Creates empty square game board with chosen size, usually checked from
     * rule book.
     *
     * @param boardSize width of game board
     */
    public GameBoard(int boardSize) {
        this.boardState = new Player[boardSize + 1][boardSize + 1];
        lastMove = null;
        playedMoves = 0;
    }

    /**
     * Create game board based on known state.
     *
     * @param state predefined state based on which game board is generated
     */
    public GameBoard(Player[][] state) {
        this.boardState = state;
    }

    /**
     * Set move on game board.
     *
     * @param move player made move to be stored.
     */
    public void setMove(Move move) {
        boardState[move.getX()][move.getY()] = move.getPlayer();
        lastMove = move;
        playedMoves++;
    }

    /**
     * Get move from game board based on x,y coordinate.
     *
     * @param x x-coordinate of the move to be get
     * @param y y-coordinate of the move to be get
     * @return move if found, otherwise null
     */
    public Move getMove(int x, int y) {
        int n = boardState.length - 1;
        if (x > 0 && x <= n && y > 0 && y <= n) {
            if (boardState[x][y] != null) {
                return new Move(boardState[x][y], x, y);
            }
        }
        return null;
    }

    public Move getLastMove() {
        return lastMove;
    }

    /**
     * Get an array of moves based on given arrays of x,y-coordinates
     *
     * @param x x-coordinates
     * @param y y-coordinates
     * @return array of moves if found, otherwise null;
     */
    public Move[] getMoves(int[] x, int[] y) {
        int n = x.length;
        Move[] moves = new Move[n];
        for (int i = 0; i < n; i++) {
            if (i < n && i < y.length) {
                if (x[i] > 0 && x[i] < boardState.length 
                        && y[i] > 0 && y[i] < boardState.length) {
                    moves[i] = this.getMove(x[i], y[i]);
                }
            }
        }
        return moves;
    }

    /**
     * Gets game board size, that is width of the edges (number of tiles per
     * edge).
     *
     * @return width of game board edge
     */
    public int getSize() {
        return this.boardState.length - 1;
    }

    /**
     * Gets number of moves on board.
     *
     * @return number of played moves
     */
    public int getNumberOfPlayedMoves() {
        return playedMoves;
    }

    /**
     * Get copy of this game board.
     *
     * @return copy of this game board
     */
    public GameBoard getCopy() {
        int n = boardState.length - 1;
        Player[][] board = new Player[n + 1][n + 1];
        for (int x = 1; x <= n; x++) {
            for (int y = 1; y <= n; y++) {
                board[x][y] = this.boardState[x][y];
            }
        }
        return new GameBoard(board);
    }

}
