package board.drawable.pawn;

import board.drawable.Drawable;
import database.DBDocument;
import org.bson.Document;

public class Pawn implements Drawable, DBDocument {
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

    @Override
    public Document toDocument() {
        return new Document("id", id);
    }
}
