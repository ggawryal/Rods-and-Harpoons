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
}