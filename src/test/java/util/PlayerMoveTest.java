package util;

import board.Move;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class PlayerMoveTest {
    @Test
    void testGetters() {
        Move move = mock(Move.class);
        PlayerMove playerMove = new PlayerMove(0, 1, move);

        assertEquals(0, playerMove.getPlayerId());

        assertEquals(1, playerMove.getPoints());

        assertEquals(move, playerMove.getMove());
    }
}