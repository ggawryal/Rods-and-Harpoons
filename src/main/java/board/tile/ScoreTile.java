package board.tile;

import javafx.scene.paint.Color;

public class ScoreTile implements Tile {
    static javafx.scene.paint.Color[] colors = {Color.RED, Color.BLUE, Color.GREEN};
    private final int score;

    public ScoreTile(int score) {
        this.score = score;
        if(score < 1 || score >= 4)
            throw new RuntimeException("Tile with wrong score chosen");

    }
    public Color getFill() {
        return colors[score-1];
    }

    @Override
    public int getScore() {
        return score;
    }
}
