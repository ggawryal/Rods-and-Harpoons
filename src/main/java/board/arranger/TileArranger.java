package board.arranger;

import board.Board;

public interface TileArranger {
    void arrange(Board b, TileScoreChooser tileScoreChooser);
}
