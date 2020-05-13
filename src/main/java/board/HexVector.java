package board;

public class HexVector {
    public HexVector(Direction d, int length) {
        switch (d) {
            case NE:
                set(length,-length);
                break;
            case E:
                set(length,0);
                break;
            case SE:
                set(0,length);
                break;
            default:
                throw new RuntimeException("Wrong direction passed");
        }
    }
    public HexVector(int east, int southeast) {
        set(east,southeast);
    }

    public HexVector add(HexVector other) {
        this.east += other.east;
        this.southeast += other.southeast;
        return this;
    }

    public HexVector sub(HexVector other) {
        this.east -= other.east;
        this.southeast -= other.southeast;
        return this;
    }

    public HexVector scale(int scale) {
        this.east *= scale;
        this.southeast *= scale;
        return this;
    }
    public HexVector negate() {
        return scale(-1);
    }

    public int getCoordinate(Direction d) {
        if(d == Direction.E)
            return east;
        else if(d == Direction.SE)
            return southeast;
        else
            return east-southeast;
    }

    @Override
    public String toString() {
        return "[" + east + ", "+southeast+"]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HexVector hexVector = (HexVector) o;

        return east == hexVector.east && southeast == hexVector.southeast;
    }

    @Override
    public int hashCode() {
        return east*31 + southeast;
    }

    private int east,southeast;

    private HexVector set(int east, int southeast) {
        this.east = east;
        this.southeast = southeast;
        return this;
    }

}
