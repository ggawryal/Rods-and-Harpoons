package board;

import board.drawable.tile.EmptyTile;
import board.drawable.tile.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BoardTest {
    @Test
    public void testAddTile() {
        BoardView view = mock(BoardView.class);
        Board board = new Board(view);

        Tile tile = new EmptyTile();
        assertTrue(board.addTile(tile,new HexVector(1,3)));
        verify(view).drawTile(tile,new HexVector(1,3));

        assertTrue(board.addTile(tile,new HexVector(2,3)));
        verify(view).drawTile(tile,new HexVector(2,3));

        assertFalse(board.addTile(tile,new HexVector(1,3)));

        verifyNoMoreInteractions(view);


    }
    @Test
    public void testHasTile() {
        BoardView view = mock(BoardView.class);
        Board board = new Board(view);

        assertFalse(board.hasTileAt(new HexVector(1,3)));

        board.addTile(new EmptyTile(),new HexVector(1,3));
        assertTrue(board.hasTileAt(new HexVector(1,3)));

        board.removeTile(new HexVector(1,3));
        assertFalse(board.hasTileAt(new HexVector(1,4)));
    }

    @Test
    public void testGetTileAt() {
        BoardView view = mock(BoardView.class);
        Board board = new Board(view);
        Tile tile1 = new EmptyTile(), tile2 = new EmptyTile();

        board.addTile(tile1,new HexVector(0,0));
        board.addTile(tile2,new HexVector(1,0));

        assertEquals(tile1,board.getTileAt(new HexVector(0,0)));
        assertEquals(tile2,board.getTileAt(new HexVector(1,0)));
    }

    @Test
    public void testClear() {
        BoardView view = mock(BoardView.class);
        Board board = new Board(view);

        board.addTile(new EmptyTile(),new HexVector(0,1));
        board.addTile(new EmptyTile(),new HexVector(5,3));
        board.addTile(new EmptyTile(),new HexVector(3,7));
        board.removeTile(new HexVector(5,3));

        board.clear();

        assertFalse(board.hasTileAt(new HexVector(0,1)));
        assertFalse(board.hasTileAt(new HexVector(5,3)));
        assertFalse(board.hasTileAt(new HexVector(3,7)));
    }


}