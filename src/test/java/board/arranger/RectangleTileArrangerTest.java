package board.arranger;

import board.Board;
import board.views.BoardView;
import board.HexVector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RectangleTileArrangerTest {
    @Test
    void testGetTotalTilesNeeded() {
        RectangleTileArranger t1 = new RectangleTileArranger(3,3);
        RectangleTileArranger t2 = new RectangleTileArranger(2,2);
        assertEquals(8,t1.getTotalTilesNeeded());
        assertEquals(3,t2.getTotalTilesNeeded());
    }

    @Test
    void testArrange() {
        BoardView boardView = mock(BoardView.class);
        Board board = new Board(boardView, 3);
        RectangleTileArranger tileArranger = new RectangleTileArranger(2,3);
        TileScoreChooser tileScoreChooser = new SameScoreTileChooser();
        tileArranger.arrange(board, tileScoreChooser);

        assertEquals(0,tileScoreChooser.getTilesLeft());
        assertTrue(board.hasTileAt(new HexVector(0,0)));
        assertTrue(board.hasTileAt(new HexVector(1,0)));
        assertTrue(board.hasTileAt(new HexVector(0,1)));
        assertTrue(board.hasTileAt(new HexVector(0,2)));
        assertTrue(board.hasTileAt(new HexVector(-1,2)));

    }


}