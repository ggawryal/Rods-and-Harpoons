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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move)) return false;

        Move move = (Move) o;
        return from.equals(move.from) && to.equals(move.getTo());
    }

    @Override
    public int hashCode() {
        int result = getFrom().hashCode();
        result = 31 * result + getTo().hashCode();
        return result;
    }

    private HexVector from, to;
}
