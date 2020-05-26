package board.tile;

import javafx.scene.image.Image;

public class PlayerTile implements Tile {
    private Image image;

    public PlayerTile(String imageName) {
        image = new Image(imageName);
    }

    @Override
    public int getScore() {
        return 0;
    }

    @Override
    public Image getImage() { return image; }
}
