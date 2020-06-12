package board.drawable.tile;

import org.bson.Document;

public class EmptyTile implements Tile {
    @Override
    public int getScore() {
        return 0;
    }

    @Override
    public String getImagePath() {
        return null;
    }

    @Override
    public Document toDocument() {
        return null;
    }
}
