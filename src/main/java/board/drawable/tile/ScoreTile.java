package board.drawable.tile;

import javafx.scene.image.Image;

public class ScoreTile implements Tile {
    private final int score;
    private final Image image;

    public ScoreTile(int score) {
        this.score = score;
        if(score < 1 || score >= 4)
            throw new RuntimeException("Tile with wrong score chosen");
        image = new Image("/hex_" + score + ".png");
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public Image getImage() { return image; }
}
