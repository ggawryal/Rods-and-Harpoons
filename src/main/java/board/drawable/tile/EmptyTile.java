package board.drawable.tile;

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
