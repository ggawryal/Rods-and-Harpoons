package board;

import javafx.util.Pair;

public class StandardMoveChecker implements MoveChecker {
    private Board board;
    public StandardMoveChecker(Board board) {
        this.board = board;
    }

    //returns null if from and to aren't in same line
    private Pair<Direction,Integer> getDirectionAndTurnOrNull(HexVector from, HexVector to) {
        HexVector diff = to.copy().sub(from);
        if(diff.getEast() == 0)
            return new Pair<>(Direction.SE, diff.getSoutheast()  > 0 ? 1 : -1);
        if(diff.getSoutheast() == 0)
            return new Pair<>(Direction.E, diff.getEast() > 0 ? 1 : -1);
        if(diff.getEast() + diff.getSoutheast() == 0)
            return new Pair<>(Direction.NE, diff.getEast() > 0 ? 1 : -1);
        return null;
    }

    @Override
    public boolean isValidMove(Move move) {
        Pair<Direction,Integer> directionAndTurn = getDirectionAndTurnOrNull(move.getFrom(),move.getTo());
        if(directionAndTurn == null)
            return false;

        HexVector oneVector = new HexVector(directionAndTurn.getKey(), directionAndTurn.getValue());

        for(HexVector v = move.getFrom().copy(); !v.equals(move.getTo()); v = v.add(oneVector)){
            if(!board.hasTileAt(v))
                return false;
        }
        //TODO: check collisions with other players
        return true;

    }
}
