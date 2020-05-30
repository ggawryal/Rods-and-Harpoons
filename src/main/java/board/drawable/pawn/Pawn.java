package board.drawable.pawn;

import board.drawable.Drawable;

public class Pawn implements Drawable {
    private final int id;
    private String imagePath;

    public Pawn(int id) {
        this.id = id;
        this.imagePath = "/hex_P" + id + ".png";
    }

    @Override
    public String getImagePath() {
        return imagePath;
    }

    public int getId() {
        return id;
    }
}
