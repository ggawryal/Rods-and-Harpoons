package board.drawable.tile;

public class EmptyTile implements Tile {
    @Override
    public int getScore() {
        return 0;
    }

    @Override
    public String getImagePath() {
        return null;
    }
}
