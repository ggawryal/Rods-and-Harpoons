package board.arranger;

import board.drawable.tile.Tile;

/**
Interface for choosing score of each tile when creating new board
 */
public interface TileScoreChooser {
    //Should be called before chooseTile
    void prepareTiles(int totalTileNumber);
    Tile chooseTile();
    int getTilesLeft();
}
