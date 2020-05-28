package board.drawable.pawn;

import board.drawable.Drawable;
import javafx.scene.image.Image;

public class Pawn implements Drawable {
    private final String imagePath;
    private final int id;

    public Pawn(int id) {
        this.id = id;
        this.imagePath ="/hex_P" + id + ".png";
    }

    @Override
    public String getImagePath() {
        return imagePath;
    }

    public int getId() {
        return id;
    }
}
