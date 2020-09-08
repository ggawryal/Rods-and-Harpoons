package board.drawable.tile;

import org.bson.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTileTest {
    @Test
    public void TestCorrectScore() {
        ScoreTile scoreTile = new ScoreTile(2);

        assertEquals(2, scoreTile.getScore());
        assertEquals("/hex_2.png", scoreTile.getImagePath());
    }

    @Test
    public void TestIncorrectScore() {
        Throwable exceptionTooHigh = assertThrows(RuntimeException.class, () -> new ScoreTile(4));
        assertEquals("Tile with wrong score chosen", exceptionTooHigh.getMessage());

        Throwable exceptionTooLow = assertThrows(RuntimeException.class, () -> new ScoreTile(0));
        assertEquals("Tile with wrong score chosen", exceptionTooLow.getMessage());
    }

    @Test
    public void testFromAndToDocument() {
        ScoreTile scoreTile = new ScoreTile(3);
        Document document = scoreTile.toDocument();

        assertEquals(scoreTile.getScore(), new ScoreTile(document).getScore());
    }

}