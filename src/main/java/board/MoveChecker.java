package board;

import java.util.List;

public interface MoveChecker {
    List<Move> getPossibleMoves(HexVector from);
    default boolean isValidMove(Move move) {
        return getPossibleMoves(move.getFrom()).contains(move);
    }
}
