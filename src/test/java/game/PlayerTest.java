package game;

import board.HexVector;
import board.Move;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void testGetIdAndNickname() {
        Player player = new Player(1,"Bob");
        assertEquals(1,player.getId());
        assertEquals("Bob",player.getNickname());
    }

    @Test
    void testGetPawnsCount() {
        Player player = new Player(1,"Bob");
        assertEquals(0, player.getPawnsCount());

        player.addPawn(new HexVector(0,0));
        assertEquals(1, player.getPawnsCount());
    }

    @Test
    void testHasPawnAt() {
        Player player = new Player(1,"Bob");
        player.addPawn(new HexVector(0,0));
        player.addPawn(new HexVector(1,2));

        assertTrue(player.hasPawnAt(new HexVector(0,0)));
        assertTrue(player.hasPawnAt(new HexVector(1,2)));
        assertFalse(player.hasPawnAt(new HexVector(0,2)));
    }

    @Test
    void testGetPawnPosition() {
        Player player = new Player(1,"Bob");
        player.addPawn(new HexVector(0,0));
        player.addPawn(new HexVector(1,2));

        assertEquals(new HexVector(0,0),player.getPawnPosition(0));
        assertEquals(new HexVector(1,2),player.getPawnPosition(1));
    }

    @Test
    void testChangePawnPosition() {
        Player player = new Player(1,"Bob");
        player.addPawn(new HexVector(0,0));
        player.addPawn(new HexVector(1,2));

        player.changePawnPosition(new Move(new HexVector(0,0),new HexVector(4,4)));
        player.changePawnPosition(new Move(new HexVector(4,4),new HexVector(3,3)));

        assertEquals(player.getPawnPosition(0),new HexVector(3,3));
    }

    @Test
    void testChangePawnPositionWhenInvalidMove() {
        Player player = new Player(1,"Bob");
        player.addPawn(new HexVector(0,0));

        assertThrows(RuntimeException.class, () ->
                player.changePawnPosition(new Move(new HexVector(1,0),new HexVector(4,4)))
        );
    }


    @Test
    void testAddPoints() {
        Player player = new Player(1,"Bob");
        player.addPoints(4);
        player.addPoints(3);

        assertEquals(7,player.getPoints());
    }

}