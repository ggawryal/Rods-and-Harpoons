package board.arranger;

import board.drawable.tile.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SameScoreTileChooserTest {

    @Test
    void testGetTilesLeft() {
        SameScoreTileChooser tileChooser = new SameScoreTileChooser();
        tileChooser.prepareTiles(10);
        for(int i=10;i>0;i--) {
            assertEquals(i,tileChooser.getTilesLeft());
            tileChooser.chooseTile();
        }
    }

    @Test
    void testPrepareTiles() {
        SameScoreTileChooser tileChooser = new SameScoreTileChooser();
        tileChooser.prepareTiles(10);

        while(tileChooser.getTilesLeft() > 0) {
            Tile t = tileChooser.chooseTile();
            assertEquals(1,t.getScore());
        }
    }
}