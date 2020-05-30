package board;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class StandardMoveChecker implements MoveChecker {
    private Board board;
    public StandardMoveChecker(Board board) {
        this.board = board;
    }

    @Override
    public List<Move> getPossibleMoves(HexVector from) {
        List<Move> possibleMoves = new ArrayList<>();
        for(Direction direction : Direction.values()) {
            for(int turn = -1; turn <= 1; turn +=2) {
                HexVector oneVector = new HexVector(direction,turn);
                for(HexVector v = oneVector.copy().add(from); board.hasTileAt(v) && !board.hasPawnAt(v); v = v.add(oneVector)) {
                    possibleMoves.add(new Move(from.copy(),v.copy()));
                }
            }
        }
        return possibleMoves;
    }
}
