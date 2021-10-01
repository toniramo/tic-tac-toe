package tictactoe.dao;

import tictactoe.logic.GameBoard;
import tictactoe.logic.Move;
import tictactoe.logic.RuleBook;

/**
 * Class to define data access object for storing game data in memory.
 */
public class InMemoryDao implements Dao {

    private GameBoard board;
    private RuleBook rules;
    private int turn;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeGameBoard(RuleBook rules) {
        this.rules = rules;
        this.board = new GameBoard(rules.getBoardsize());
        this.turn = 0;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public GameBoard getGameBoard() {
        return this.board;
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
    @Override
    public int getTurn() {
        return this.turn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTurn(int turn) {
        this.turn = turn;
    }
}
