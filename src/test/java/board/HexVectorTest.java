package board;

import org.bson.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HexVectorTest {
    @Test
    void testEquals() {
        assertEquals(new HexVector(0,4),new HexVector(Direction.SE,4));
        assertNotEquals(new HexVector(1,4), new HexVector(Direction.E,1));
        assertNotEquals(new HexVector(1,4), new HexVector(1,3));
    }

    @Test
    void testAdd() {
        HexVector v = new HexVector(0,0);
        v = v.add(new HexVector(1,0));
        v = v.add(new HexVector(Direction.SE,-2));
        v = v.add(new HexVector(Direction.NE,-1));

        assertEquals(new HexVector(Direction.SE,-1),v);
    }
    @Test
    void testSub() {
        HexVector v = new HexVector(4,0);
        HexVector w = new HexVector(1,2);

        v = v.sub(w);

        assertEquals(new HexVector(1,2),w);
        assertEquals(new HexVector(3,-2),v);
    }

    @Test
    void testScale() {
        HexVector v = new HexVector(3,1).scale(-3);
        assertEquals(new HexVector(-9,-3),v);
    }

    @Test
    void testNegate() {
        HexVector v = new HexVector(3,-4).negate();
        assertEquals(new HexVector(-3,4),v);
    }

    @Test
    void testFromAndToDocument() {
        HexVector v = new HexVector(3,4);
        Document document = v.toDocument();
        assertEquals(v, new HexVector(document));
    }

}