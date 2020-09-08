package board;

import database.DBDocument;
import org.bson.Document;

public class HexVector implements DBDocument {
    public HexVector(Direction d, int length) {
        switch (d) {
            case NE:
                this.east = length;
                this.southeast = -length;
                break;
            case E:
                this.east = length;
                this.southeast = 0;
                break;
            case SE:
                this.east = 0;
                this.southeast = length;
                break;
            default:
                throw new RuntimeException("Wrong direction passed");
        }
    }
    public HexVector(int east, int southeast) {
        this.east = east;
        this.southeast = southeast;
    }

    public HexVector add(HexVector other) {
        return new HexVector(this.east + other.east, this.southeast + other.southeast);
    }

    public HexVector sub(HexVector other) {
        return new HexVector(this.east - other.east, this.southeast - other.southeast);
    }

    public HexVector scale(int scale) {
        return new HexVector(this.east * scale, this.southeast * scale);
    }
    public HexVector negate() {
        return scale(-1);
    }

    public HexVector copy() {
        return new HexVector(this.east, this.southeast);
    }

    public int getEast() {
        return east;
    }
    public int getSoutheast() {
        return southeast;
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

    @Override
    public Document toDocument() {
        return new Document("east", east)
                .append("southeast", southeast);
    }

    public HexVector(Document document) {
        this(document.getInteger("east"), document.getInteger("southeast"));
    }

    private final int east, southeast;
}
