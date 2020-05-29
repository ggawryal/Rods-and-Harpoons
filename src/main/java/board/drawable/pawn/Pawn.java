package board.drawable.pawn;

import board.drawable.Drawable;

public class Pawn implements Drawable {
    private String[] imagePaths;
    private final int id;
    private int activeImage;

    public Pawn(int id) {
        this.id = id;
        this.imagePaths = new String[]{
                ("/hex_P" + id + ".png"),
                ("/hex_P" + id + "i.png")
        };
    }

    @Override
    public String getImagePath() {
        return imagePaths[activeImage];
    }
    public void switchImage() {
        activeImage = 1 - activeImage;
    }

    public int getId() {
        return id;
    }
}
