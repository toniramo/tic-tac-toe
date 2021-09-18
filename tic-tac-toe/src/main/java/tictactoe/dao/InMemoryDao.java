package tictactoe.dao;

import java.util.ArrayList;
import java.util.List;
import tictactoe.domain.Move;
import tictactoe.domain.Player;

/**
 * Class to define data access object for storing game data in memory.
 */
public class InMemoryDao implements Dao {

    private int n; //Game board size
    private int k; //Number of marks to win
    private Move[][] moves;
    private int numberOfMoves;
    private int turn;
    private List<Player> players;

    /** {@inheritDoc} */
    @Override
    public void initializeGameBoard(int n, int k) {
        this.n = n;
        this.k = k;
        this.moves = new Move[n + 1][n + 1];
        this.numberOfMoves = 0;
        this.turn = 0;
        this.players = new ArrayList<>();
    }

    /** {@inheritDoc} */
    @Override
    public int getGameBoardSize() {
        return this.n;
    }

    /** {@inheritDoc} */
    @Override
    public int getNumberOfMarksToWin() {
        return this.k;
    }
    
    /** {@inheritDoc} */
    @Override
    public void setMove(Move move) {
        this.moves[move.getX()][move.getY()] = move;
    }

    /** {@inheritDoc} */
    @Override
    public Move getMove(int x, int y) {
        return this.moves[x][y];
    }

    /** {@inheritDoc} */
    @Override
    public int getNumberOfPlayedMoves() {
        return this.numberOfMoves;
    }
    
    /** {@inheritDoc} */
    @Override
    public void addPlayer(Player player) {
        this.players.add(player);
    }
    
    /** {@inheritDoc} */
    @Override
    public Player getCurrentPlayer() {
        return players.get(turn);
    }
    
    /** {@inheritDoc} */
    @Override
    public void changeTurn() {
        if (turn == players.size() - 1) {
            turn = 0;
        } else {
            turn++;
        }
    }
}
