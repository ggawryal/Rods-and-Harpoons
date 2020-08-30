package game.controllers.strategies;

import board.Board;
import board.Move;
import board.MoveChecker;

/**
 * Represents strategy (which pawn to move and where) for AI player
 */
public interface BotStrategy {
    //call before getMoveValue
    void set(Board board, MoveChecker moveChecker);

    /**
     * Checks value for this strategy and given move. The bigger value is, the stronger move it is considered for AI.
     * @param move possible move
     * @return value of given move
     */
    long getMoveValue(Move move);
}
