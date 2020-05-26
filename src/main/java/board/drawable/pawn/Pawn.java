package board.drawable.pawn;

import board.drawable.Drawable;
import javafx.scene.image.Image;

public class Pawn implements Drawable {
    private final Image image;
    private final int id;

    public Pawn(int id) {
        this.id = id;
        this.image = new Image("/hex_P" + id + ".png");
    }

    @Override
    public Image getImage() {
        return image;
    }

    public int getId() {
        return id;
    }
}
