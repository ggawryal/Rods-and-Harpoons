package board.arranger;

import board.Board;

/**
 * Interface for creating board with different shapes
 */
public interface TileArranger {
    void arrange(Board b, TileScoreChooser tileScoreChooser);
}
