package board;

import board.tile.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class BoardTest {
    @Test
    public void testAddTile() {
        BoardView view = mock(BoardView.class);
        Board board = new Board(view);
        assertTrue(board.addTile(new Tile(),new HexVector(1,3)));
        assertTrue(board.addTile(new Tile(),new HexVector(2,3)));
        assertFalse(board.addTile(new Tile(),new HexVector(1,3)));
    }
    @Test
    public void testHasTile() {
        BoardView view = mock(BoardView.class);
        Board board = new Board(view);

        assertFalse(board.hasTileAt(new HexVector(1,3)));
        assertTrue(board.addTile(new Tile(),new HexVector(1,3)));
        assertTrue(board.hasTileAt(new HexVector(1,3)));
        assertFalse(board.hasTileAt(new HexVector(1,4)));
    }


}