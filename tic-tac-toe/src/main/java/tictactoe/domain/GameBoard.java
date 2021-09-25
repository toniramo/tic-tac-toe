/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.domain;

/**
 *
 * @author toniramo
 */
public class GameBoard {

    private Player[][] boardStatus;

    public GameBoard(int boardSize) {
        this.boardStatus = new Player[boardSize + 1][boardSize + 1];
    }

    public GameBoard(Player[][] boardStatus) {
        this.boardStatus = boardStatus;
    }

    public void setMove(Move move) {
        boardStatus[move.getX()][move.getY()] = move.getPlayer();
    }

    public Move getMove(int x, int y) {
        int n = boardStatus.length - 1;
        if (x > 0 && x <= n && y > 0 && y <= n) {
            if (boardStatus[x][y] != null) {
                return new Move(boardStatus[x][y], x, y);
            }
        }
        return null;
    }

    public Move[] getMoves(int[] x, int[] y) {
        int n = x.length;
        Move[] moves = new Move[n];
        for (int i = 0; i < n; i++) {
            if (i < n && i < y.length) {
                moves[i] = this.getMove(x[i], y[i]);
            }
        }
        return moves;
    }

    public int getSize() {
        return this.boardStatus.length - 1;
    }

    public int getNumberOfPlayedMoves() {
        int moves = 0;
        int n = this.getSize();
        for (int x = 1; x <= n; x++) {
            for (int y = 1; y <= n; y++) {
                if (this.boardStatus[x][y] != null) {
                    moves++;
                }
            }
        }
        return moves;
    }

    public GameBoard getCopy() {
        int n = boardStatus.length - 1;
        Player[][] board = new Player[n + 1][n + 1];
        for (int x = 1; x <= n; x++) {
            for (int y = 1; y <= n; y++) {
                board[x][y] = this.boardStatus[x][y];
            }
        }
        return new GameBoard(board);
    }

}
