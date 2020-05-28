package board.drawable.tile;

public class ScoreTile implements Tile {
    private final int score;
    private final String imagePath;

    public ScoreTile(int score) {
        this.score = score;
        if(score < 1 || score >= 4)
            throw new RuntimeException("Tile with wrong score chosen");
        imagePath = "/hex_" + score + ".png";
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public String getImagePath() { return imagePath; }
}
