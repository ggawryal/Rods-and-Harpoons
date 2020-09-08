package board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {
    @Test
    void testGetFromAndTo() {
        Move move = new Move(new HexVector(1,2), new HexVector(3,4));
        assertEquals(new HexVector(1,2), move.getFrom());
        assertEquals(new HexVector(3,4), move.getTo());
        assertNotEquals(new HexVector(1,2), move.getTo());
    }

    @Test
    void testEquals() {
        Move move = new Move(new HexVector(1,2), new HexVector(3,4));
        assertEquals(new Move(new HexVector(1,2), new HexVector(3,4)), move);
        assertNotEquals(new Move(new HexVector(3,4), new HexVector(1,2)), move);
    }

    @Test
    void testGetDistance() {
        HexVector v = new HexVector(4,4);
        Move move = new Move(v, v.add(new HexVector(Direction.E,5)));
        Move move2 = new Move(v,v.add(new HexVector(Direction.SE, -4)));
        Move move3 = new Move(v, v.add(new HexVector(Direction.NE, -3 )));

        assertEquals(5, move.getDistance());
        assertEquals(4, move2.getDistance());
        assertEquals(3, move3.getDistance());
    }

    @Test
    void testHashCode() {
        Move move1 = new Move(new HexVector(4,4), new HexVector(5,6));
        Move move2 = new Move(new HexVector(4,4), new HexVector(5,6));
        assertEquals(move1.hashCode(), move2.hashCode());
    }
}