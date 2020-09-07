package game.controllers;

import board.Board;
import board.Move;
import board.MoveChecker;
import game.Player;

import java.util.function.Function;

/**
 * Abstract class for different types of players
 */
public abstract class PlayerController {
    public void set(Player player, Board board, MoveChecker moveChecker) {
        this.player = player;
        this.board = board;
        this.moveChecker = moveChecker;
    }

    public void setActionOnMove(Function<Move, Boolean> action) {
        this.actionOnMove = action;
    }

    public abstract void nextTurn();
    public void shutdown() {}

    public final Player getPlayer() {
        return player;
    }

    protected final Board getBoard() {
        return board;
    }

    protected final MoveChecker getMoveChecker() {
        return moveChecker;
    }

    protected final Function<Move, Boolean> getActionOnMove() {
        return actionOnMove;
    }

    private Player player;
    private Board board;
    private MoveChecker moveChecker;
    private Function<Move,Boolean> actionOnMove;


}
