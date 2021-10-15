package tictactoe.ai;

import tictactoe.annotations.ExcludeFromJacocoGeneratedReport;
import tictactoe.logic.GameBoard;
import tictactoe.logic.GameService;
import tictactoe.logic.Move;
import tictactoe.logic.Player;
import tictactoe.logic.RuleBook;

/**
 * Provides optimal move for AI player. AI object maintains own data structure,
 * simple integer array for performance reasons instead of using game logic
 * objects.
 */
public class AI {

    private GameService service;
    private Player player;
    private int[][] state;
    private int[] playArea;

    /**
     * Create AI object.
     *
     * @param service AI reads and updates its status based on data provided by
     * service
     * @param player Links chosen player to AI
     */
    public AI(GameService service, Player player) {
        this.service = service;
        this.player = player;
        int n = this.service.getRules().getBoardsize();
        state = new int[n + 1][n + 1];
        for (int x = 0; x <= n; x++) {
            for (int y = 0; y <= n; y++) {
                state[x][y] = -1;
            }
        }
        playArea = new int[4];
    }

    /**
     * Gets potentially the most optimal move. Method is faster alternative for
     * {@link #chooseMoveUsingGameTreeObject()} due to used simple, integer
     * arrays to store node state instead of GameTreeNode objects.
     *
     * @return
     */
    public Move chooseMove() {
        updateStateBasedOnLastMove();
        int turn = service.getTurn();
        int[] move = AlphaBetaMoveChooser.getMoveWithOptimizedSearchDepth(state,
                playArea, service.getGameBoard().getNumberOfPlayedMoves(),
                turn, service.getRules().getMarksToWin(), (int) -1e9, (int) 1e9);
        state[move[0]][move[1]] = turn;
        updatePlayAreaBasedOnMove(move[0], move[1]);
        return new Move(player, move[0], move[1]);
    }

    /**
     * Updates play area depending on given move. If move is on the edge of play
     * area or outside it, play area is increased so that it covers given move
     * and one extra row.
     *
     * @param x x-coordinate of the latest move
     * @param y y-coordinate of the latest move
     */
    private void updatePlayAreaBasedOnMove(int x, int y) {
        if (playArea[0] == 0) {
            if (x > 0 && y > 0) {
                playArea[0] = x - 1;
                playArea[1] = y - 1;
                playArea[2] = x + 1;
                playArea[3] = y + 1;
                return;
            }
            if (x == 0 || y == 0) {
                int n = service.getRules().getBoardsize();
                playArea = new int[]{n / 2, n / 2, n / 2, n / 2};
                return;
            }

        }
        if (x <= playArea[0] && x > 1) {
            playArea[0] = x - 1;
        }
        if (x >= playArea[2] && x < service.getRules().getBoardsize()) {
            playArea[2] = x + 1;
        }
        if (y <= playArea[1] && y > 1) {
            playArea[1] = y - 1;
        }
        if (y >= playArea[3] && y < service.getRules().getBoardsize()) {
            playArea[3] = y + 1;
        }
    }

    /**
     * Gets player linked to this AI.
     *
     * @return player linked to this AI
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Updates AIs state based on last registered move. Can be used for instance
     * AI vs. AI games in which user chooses the first move.
     */
    public void updateStateBasedOnLastMove() {
        Move lastMove = service.getGameBoard().getLastMove();
        int xLast = 0;
        int yLast = 0;
        if (lastMove != null) {
            xLast = lastMove.getX();
            yLast = lastMove.getY();
            state[xLast][yLast] = (service.getTurn() + 1) % 2;
        }
        updatePlayAreaBasedOnMove(xLast, yLast);
    }

    /**
     * Gets optimal move using object that implements GameTreeNode interface.
     * During the development it was noticed to cause performance issues to rely
     * on object based arrays. Therefore is is not recommended to be used by
     * default. See alternative {@link #chooseMove()} which is used by default.
     *
     * @return possibly optimal move
     */
    //CHECKSTYLE:OFF
    @Deprecated
    @ExcludeFromJacocoGeneratedReport
    public Move chooseMoveUsingGameTreeObject() {
        GameBoard board = service.getGameBoard();
        RuleBook rules = service.getRules();
        int turn = service.getTurn();
        boolean minimizingPlayer = turn == 1;
        int n = service.getRules().getBoardsize();
        int value = minimizingPlayer ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        Move move = null;
        for (int x = 1; x <= n; x++) {
            for (int y = 1; y <= n; y++) {
                if (service.validMove(x, y)) {
                    Move potentialMove = new Move(player, x, y);
                    GameBoard child = board.getCopy();
                    child.setMove(potentialMove);
                    GameTreeNode node = new TicTacToeNode(child, rules, turn, minimizingPlayer);
                    int alphaBetaValue = GameTreeNodePruner.getNodeValue(node, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
                    if (minimizingPlayer) {
                        if (value > alphaBetaValue) {
                            move = potentialMove;
                            value = alphaBetaValue;
                        }
                        if (value == Integer.MIN_VALUE) {
                            return move;
                        }
                    } else {
                        if (value < alphaBetaValue) {
                            move = potentialMove;
                            value = alphaBetaValue;
                        }
                        if (value == Integer.MAX_VALUE) {
                            return move;
                        }
                    }
                }
            }
        }
        return move;
    }
    //CHECKSTYLE:ON
}
