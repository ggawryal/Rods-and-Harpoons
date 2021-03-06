package board.drawable.pawn;

import board.drawable.Drawable;
import database.DBDocument;
import org.bson.Document;

public class Pawn implements Drawable, DBDocument {
    private final int id;
    private String imagePath;

    public Pawn(int id) {
        this.id = id;
        if(id < 1 || id >= 5)
            throw new RuntimeException("Pawn with wrong id chosen");
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

    public Pawn(Document document) {
        this(document.getInteger("id"));
    }
}
