package board;

import java.util.ArrayList;
import java.util.List;

public interface MoveChecker {
    List<Move> getPossibleMoves(HexVector from);
    default boolean isValidMove(Move move) {
        return getPossibleMoves(move.getFrom()).contains(move);
    }

    default List<HexVector> getNeighbours(HexVector from) {
        List<Move> possibleMoves = getPossibleMoves(from);
        List<HexVector> neighbours = new ArrayList<>();
        for(Direction direction : Direction.values()) {
            for(int turn = -1; turn <= 1; turn +=2) {
                HexVector to = from.copy().add(new HexVector(direction,turn));
                if(possibleMoves.contains(new Move(from, to)))
                    neighbours.add(to);
            }
        }
        return neighbours;
    }

}
