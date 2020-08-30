package game.controllers.strategies;

import board.Board;
import board.HexVector;
import board.Move;
import board.MoveChecker;

/**
 * Considers values of start and end tile of each move, number of direct neighbouring tiles and number of all achievable tiles
 */
public class MixedStrategy implements BotStrategy {
    private Board board;
    private MoveChecker moveChecker;
    @Override
    public void set(Board board, MoveChecker moveChecker) {
        this.board = board;
        this.moveChecker = moveChecker;
    }

    @Override
    public long getMoveValue(Move move) {
        int score = board.getTileAt(move.getTo()).getScore();
        int scoreFromTileStandingAt = board.getTileAt(move.getFrom()).getScore();

        return (score*5 + scoreFromTileStandingAt*2)*5 + getPositionalScore(move.getTo())*2 - getPositionalScore(move.getFrom());
    }

    int getPositionalScore(HexVector position) {
        int neighbours = moveChecker.getNeighbours(position).size();
        int totalMoves = moveChecker.getPossibleMoves(position).size();
        if(neighbours == 0)
            return -1000;
        if(neighbours == 1)
            return totalMoves-10;
        return totalMoves;
    }
}
