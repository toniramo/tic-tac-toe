/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.ai;

import tictactoe.domain.GameBoard;
import tictactoe.domain.GameService;
import tictactoe.domain.Move;
import tictactoe.domain.Player;
import tictactoe.domain.RuleBook;

/**
 *
 * @author toniramo
 */
public class AI {

    private GameService service;
    private Player player;
    private boolean minimizingPlayer;

    public AI(GameService service, Player player, boolean minimizingPlayer) {
        this.service = service;
        this.player = player;
        this.minimizingPlayer = minimizingPlayer;
    }

    public Move chooseMove() {
        //double startTime = System.currentTimeMillis();
        GameBoard board = service.getGameBoard();
        RuleBook rules = service.getRules();
        int turn = service.getTurn();
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
                    int alphaBetaValue = AlphaBetaPruner.getNodeValue(node, Integer.MIN_VALUE, Integer.MAX_VALUE, 2);
                    if (minimizingPlayer) {
                        if (value > alphaBetaValue) {
                            move = potentialMove;
                            value = alphaBetaValue;
                            if (value == Integer.MIN_VALUE) {
                            return move;
                        }
                        }
                    } else {
                        if (value < alphaBetaValue) {
                            move = potentialMove;
                            value = alphaBetaValue;
                            if (value == Integer.MAX_VALUE) {
                                return move;
                            }
                        }
                    }
                }
            }
        }
        //double endTime = System.currentTimeMillis();
        //System.out.println("it took: " + (endTime-startTime) + " ms");
        return move;
    }

    public Player getPlayer() {
        return this.player;
    }
}
