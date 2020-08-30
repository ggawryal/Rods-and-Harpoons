package board.arranger;

import board.drawable.tile.ScoreTile;
import board.drawable.tile.Tile;

/**
 Always creates tiles with 1 score
 */
public class SameScoreTileChooser implements TileScoreChooser {
    @Override
    public void prepareTiles(int totalTileNumber) {
        this.tiles = totalTileNumber;
    }

    @Override
    public Tile chooseTile() {
        tiles--;
        return new ScoreTile(1);
    }

    @Override
    public int getTilesLeft() {
        return tiles;
    }

    private int tiles;
}
