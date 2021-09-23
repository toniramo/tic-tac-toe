package tictactoe.dao;

import java.util.ArrayList;
import java.util.List;
import tictactoe.domain.GameBoard;
import tictactoe.domain.Move;
import tictactoe.domain.Player;
import tictactoe.domain.RuleBook;

/**
 * Class to define data access object for storing game data in memory.
 */
public class InMemoryDao implements Dao {

    private GameBoard board;
    private RuleBook rules;
    private int turn;

    @Override
    public void initializeGameBoard(RuleBook rules) {
        this.rules = rules;
        this.board = new GameBoard(rules.getBoardsize());
        this.turn = 0;
        //this.players = new ArrayList<>();
    }

    @Override
    public RuleBook getRules() {
        return this.rules;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGameBoard(GameBoard gameBoard) {
        this.board = gameBoard;
    }

    @Override
    public GameBoard getGameBoard() {
        return this.board;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getGameBoardSize() {
        return this.board.getSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMove(Move move) {
        this.board.setMove(move);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Move getMoveAt(int x, int y) {
        return this.board.getMove(x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfPlayedMoves() {
        return this.board.getNumberOfPlayedMoves();
    }

    /**
     * {@inheritDoc}
     */
    //@Override
    //public void addPlayer(Player player) {
    //    this.players.add(player);
    //}

    /**
     * {@inheritDoc}
     */
    @Override
    public Player getCurrentPlayer() {
        return rules.getPlayers()[turn];
    }

    public Player[] getPlayers() {
        return this.rules.getPlayers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeTurn() {
        if (turn == rules.getPlayers().length - 1) {
            turn = 0;
        } else {
            turn++;
        }
    }

    /*@Override
    public Dao getClone() {
        InMemoryDao clone = new InMemoryDao();
        clone.k = this.k;
        clone.n = this.n;
        clone.numberOfMoves = this.numberOfMoves;
        clone.turn = this.turn;
        clone.players = new ArrayList();
        for (Player player : players) {
            clone.addPlayer(player);
        }
        clone.gameBoard = new Move[n + 1][n + 1];
        for (int x = 1; x <= n; x++) {
            for (int y = 1; y <= n; y++) {
                if (this.gameBoard[x][y] != null) {
                    clone.gameBoard[x][y] = new Move(this.gameBoard[x][y].getPlayer(), x, y);
                }
            }
        }
        return clone;
    }*/

}
