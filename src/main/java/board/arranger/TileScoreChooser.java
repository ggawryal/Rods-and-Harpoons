package board.arranger;

import board.drawable.tile.Tile;


public interface TileScoreChooser {
    //Should be called before chooseTile
    void prepareTiles(int totalTileNumber);
    Tile chooseTile();
    int getTilesLeft();
}
