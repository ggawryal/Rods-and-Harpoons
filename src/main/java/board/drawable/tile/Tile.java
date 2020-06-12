package board.drawable.tile;

import board.drawable.Drawable;
import database.DBDocument;

public interface Tile extends Drawable, DBDocument {
    int getScore();
}
