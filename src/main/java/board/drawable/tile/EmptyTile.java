package board.drawable.tile;

import board.drawable.tile.Tile;
import javafx.scene.image.Image;

public class EmptyTile implements Tile {
    @Override
    public int getScore() {
        return 0;
    }

    @Override
    public Image getImage() {
        return null;
    }
}
