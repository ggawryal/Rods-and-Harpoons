package board.arranger;

import board.drawable.tile.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RatioTileChooserTest {

    @Test
    void testGetTilesLeft() {
        RatioTileChooser tileChooser = new RatioTileChooser(1,0,1);
        tileChooser.prepareTiles(10);
        for(int i=10;i>0;i--) {
            assertEquals(i,tileChooser.getTilesLeft());
            tileChooser.chooseTile();
        }
    }

    @Test
    void testPrepareTiles() {
        RatioTileChooser tileChooser = new RatioTileChooser(4,3,1);
        tileChooser.prepareTiles(16);

        int[] typeCount = new int[4];
        while(tileChooser.getTilesLeft() > 0) {
            Tile t = tileChooser.chooseTile();
            typeCount[t.getScore()]++;
        }
        assertEquals(8,typeCount[1]);
        assertEquals(6,typeCount[2]);
        assertEquals(2,typeCount[3]);
    }
}