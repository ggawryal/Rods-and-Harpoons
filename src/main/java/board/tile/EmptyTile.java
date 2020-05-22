package board.tile;

import javafx.scene.paint.Color;

public class EmptyTile implements Tile {
    @Override
    public Color getFill() {
        return Color.TRANSPARENT;
    }

    @Override
    public int getScore() {
        return 0;
    }
}
