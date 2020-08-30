package game.controllers.strategies;

import board.Board;
import board.Move;
import board.MoveChecker;
import game.controllers.strategies.BotStrategy;

/**
 * Gives the same value for all moves, causing choosing uniformly random move from all possible move list
 */
public class RandomMoveStrategy implements BotStrategy {
    @Override
    public void set(Board board, MoveChecker moveChecker) {}

    @Override
    public long getMoveValue(Move move) {
        return 0;
    }
}
