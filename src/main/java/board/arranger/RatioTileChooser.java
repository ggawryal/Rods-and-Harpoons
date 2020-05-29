package board.arranger;

import board.drawable.tile.ScoreTile;
import board.drawable.tile.Tile;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Creates tileset with t[i] tiles of score i such that t[1] : t[2] : t[3] = r1 : r2 : r3
 * Given same ratios and totalTileNumber, number of tiles of each type is constant (but order is random)
 */
public class RatioTileChooser implements TileScoreChooser {
    public RatioTileChooser(int r1, int r2, int r3) {
        if(r1 < 0 || r2 < 0 || r3 < 0 || r1+r2+r3 == 0)
            throw new RuntimeException("wrong ratio chosen");
        ratios[1] = r1;
        ratios[2] = r2;
        ratios[3] = r3;
        ratioSum = r1+r2+r3;
    }

    @Override
    public void prepareTiles(int totalTileNumber) {
        tiles.clear();
        int[] tileCount = new int[4];
        int rest = totalTileNumber;
        for(int i=1;i<=3;i++) {
            tileCount[i] = ratios[i] * totalTileNumber / ratioSum;
            rest -= tileCount[i];
        }
        tileCount[1] += rest;
        for(int i=1;i<=3;i++) {
            for(int ct=0;ct<tileCount[i];ct++)
                tiles.add(i);
        }
        Collections.shuffle(tiles);
    }

    @Override
    public Tile chooseTile() {
        if(getTilesLeft() > 0) {
            int score = tiles.remove(0);
            return new ScoreTile(score);
        }
        throw new RuntimeException("chooseTile called, but no tiles left");
    }

    @Override
    public int getTilesLeft() {
        return tiles.size();
    }

    private ArrayList<Integer> tiles = new ArrayList<>();
    private final int[] ratios = new int[4];
    private final int ratioSum;
}
