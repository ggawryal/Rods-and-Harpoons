package board;

public class Move {
    public Move(HexVector from, HexVector to) {
        this.from = from;
        this.to = to;
    }

    public HexVector getFrom() {
        return from;
    }
    public HexVector getTo() {
        return to;
    }

    private HexVector from, to;
}
