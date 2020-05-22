package board.arranger;

import board.tile.ScoreTile;
import board.tile.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Chooses randomly new tiles with ratio r1:r2:r3, where ri means number of tiles with score i
 */
public class RatioTileChooser implements TileScoreChooser {
    Random random = new Random();

    public RatioTileChooser(int r1, int r2, int r3) {
        if(r1 < 0 || r2 < 0 || r3 < 0 || r1+r2+r3 == 0)
            throw new RuntimeException("wrong ratio chosen");
        ratios[1] = r1;
        ratios[2] = r2;
        ratios[3] = r3;
        ratioSum = r1+r2+r3;
    }

    @Override
    public Tile chooseTile() {
        int r = random.nextInt(ratioSum);
        for(int i=1;i<=3;i++) {
            r -= ratios[i];
            if (r < 0)
                return new ScoreTile(i);
        }
        return new ScoreTile(3);
    }


    private final int[] ratios = new int[4];
    private final int ratioSum;
}
