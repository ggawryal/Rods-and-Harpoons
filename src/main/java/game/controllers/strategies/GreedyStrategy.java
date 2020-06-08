package game.controllers.strategies;

import board.Board;
import board.Move;
import board.MoveChecker;

public class GreedyStrategy implements BotStrategy {
    private Board board;

    @Override
    public void set(Board board, MoveChecker moveChecker) {
        this.board = board;
    }

    @Override
    public long getMoveValue(Move move) {
        return board.getTileAt(move.getTo()).getScore();
    }
}
