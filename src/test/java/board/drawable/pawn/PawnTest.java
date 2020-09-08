package board.drawable.pawn;

import org.bson.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {
    @Test
    public void TestCorrectId() {
        Pawn pawn = new Pawn(1);

        assertEquals(1, pawn.getId());

        assertEquals("/hex_P1.png", pawn.getImagePath());
    }

    @Test
    public void TestIncorrectId() {
        Throwable exceptionTooHigh = assertThrows(RuntimeException.class, () -> new Pawn(5));
        assertEquals("Pawn with wrong id chosen", exceptionTooHigh.getMessage());

        Throwable exceptionTooLow = assertThrows(RuntimeException.class, () -> new Pawn(0));
        assertEquals("Pawn with wrong id chosen", exceptionTooLow.getMessage());
    }

    @Test
    public void testFromAndToDocument() {
        Pawn pawn = new Pawn(1);
        Document document = pawn.toDocument();

        assertEquals(pawn.getId(), new Pawn(document).getId());
    }

}