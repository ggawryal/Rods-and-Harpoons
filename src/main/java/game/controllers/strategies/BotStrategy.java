package game.controllers.strategies;

import board.Board;
import board.Move;
import board.MoveChecker;

public interface BotStrategy {
    void set(Board board, MoveChecker moveChecker);
    long getMoveValue(Move move);
}
