package board;

import board.drawable.tile.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StandardMoveCheckerTest {
    @Test
    public void testIsValidMoveWhenNoMissingTiles() {
        HexVector a = new HexVector(0,0);
        HexVector b = new HexVector(Direction.SE,5);
        HexVector c = new HexVector(Direction.NE,4);
        HexVector d = new HexVector(Direction.E,-3).add(c);

        Board board = mock(Board.class);
        Tile tile = mock(Tile.class);
        when(board.hasTileAt(any())).thenReturn(true);
        when(board.getTileAt(any())).thenReturn(tile);
        when(tile.getScore()).thenReturn(1);

        MoveChecker mc = new StandardMoveChecker(board);


        assertTrue(mc.isValidMove(new Move(a,b)));
        assertTrue(mc.isValidMove(new Move(b,a)));
        assertTrue(mc.isValidMove(new Move(a,c)));
        assertTrue(mc.isValidMove(new Move(c,d)));

        assertFalse(mc.isValidMove(new Move(b,c)));
        assertFalse(mc.isValidMove(new Move(d,b)));
    }

    @Test
    public void testIsValidMoveWhenSomeTilesAreMissing() {
        HexVector a = new HexVector(0,0);
        HexVector b = new HexVector(Direction.SE,5);
        HexVector c = new HexVector(Direction.NE,4);
        HexVector d = new HexVector(Direction.E,-1);

        Board board = mock(Board.class);
        Tile tile = mock(Tile.class);
        HexVector[] missingTiles = {new HexVector(Direction.E,1),new HexVector(Direction.SE,2), new HexVector(Direction.NE,5)};

        when(board.hasTileAt(any())).thenReturn(true);
        when(board.getTileAt(any())).thenReturn(tile);
        when(tile.getScore()).thenReturn(1);
        for(HexVector v : missingTiles)
            when(board.hasTileAt(v)).thenReturn(false);


        MoveChecker mc = new StandardMoveChecker(board);

        assertFalse(mc.isValidMove(new Move(a,b)));
        assertFalse(mc.isValidMove(new Move(b,a)));

        assertTrue(mc.isValidMove(new Move(a,c)));
        assertTrue(mc.isValidMove(new Move(c,a)));
        assertTrue(mc.isValidMove(new Move(a,d)));
        assertTrue(mc.isValidMove(new Move(d,a)));
    }
}