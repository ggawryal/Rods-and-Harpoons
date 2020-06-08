package game.controllers.strategies;

import board.Board;
import board.Move;
import board.MoveChecker;
import game.controllers.strategies.BotStrategy;

public class RandomMoveStrategy implements BotStrategy {
    @Override
    public void set(Board board, MoveChecker moveChecker) {}

    @Override
    public long getMoveValue(Move move) {
        return 0;
    }
}
