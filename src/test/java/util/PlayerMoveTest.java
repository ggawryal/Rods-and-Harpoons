package util;

import board.HexVector;
import board.Move;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;

class PlayerMoveTest {
    @Test
    void testGetters() {
        Move move = new Move(new HexVector(0,1), new HexVector(1,0));
        PlayerMove playerMove = new PlayerMove(0, 1, move);

        assertEquals(0, playerMove.getPlayerId());
        assertEquals(1, playerMove.getPoints());
        assertEquals(move, playerMove.getMove());
    }

    @Test
    void testEquals() {
        Move move = new Move(new HexVector(0,1), new HexVector(1,0));
        Move move2 = new Move(new HexVector(0,1), new HexVector(1,1));
        Move move3 = new Move(new HexVector(1,1), new HexVector(1,0));
        PlayerMove playerMove = new PlayerMove(0,1,move);

        assertEquals(playerMove, new PlayerMove(0,1,move));
        assertNotEquals(playerMove, new PlayerMove(0,1,move2));
        assertNotEquals(playerMove, new PlayerMove(0,1,move3));
        assertNotEquals(playerMove, new PlayerMove(1,1,move));
        assertNotEquals(playerMove, new PlayerMove(1,2,move));
    }

    @Test
    void testFromAndToDocument() {
        Move move = new Move(new HexVector(0,1), new HexVector(1,0));
        PlayerMove playerMove = new PlayerMove(0,1,move);

        Document document = playerMove.toDocument();
        PlayerMove playerMoveFromDocument = new PlayerMove(document);

        assertEquals(playerMove, playerMoveFromDocument);
    }
}