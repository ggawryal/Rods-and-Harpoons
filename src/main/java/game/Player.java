package game;

import board.HexVector;
import board.Move;
import database.DBDocument;
import javafx.scene.paint.Color;
import org.bson.Document;

import java.util.ArrayList;

public class Player implements DBDocument {
    private final int id;
    private final String nickname;
    private ArrayList<HexVector> pawnsPositions = new ArrayList<>();
    private int points;

    public Player(int id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public int getId() { return id; }

    public String getNickname() {
        return nickname;
    }

    public void addPawn(HexVector position) {
        pawnsPositions.add(position);
    }

    public int getPawnsCount() {
        return pawnsPositions.size();
    }

    public HexVector getPawnPosition(int index) {
        return pawnsPositions.get(index);
    }

    public boolean hasPawnAt(HexVector position) {
        return pawnsPositions.contains(position);
    }

    public void changePawnPosition(Move move) {
        for(int i = 0; i < pawnsPositions.size(); i++) {
            if(pawnsPositions.get(i).equals(move.getFrom())) {
                pawnsPositions.set(i, move.getTo());
                return;
            }
        }
        throw new RuntimeException("Given player doesn't have a pawn at given position");
    }

    public int getPoints() {
        return points;
    }

    public Color getColor() {
        switch(id) {
            case 0: return new Color(1,0,0,1);
            case 1: return new Color(0,0.5,0,1);
            case 2: return new Color(0,0,1,1);
            case 3: return new Color(1,1,0,1);
            default: return new Color(0, 0, 0, 0);
        }
    }

    public void addPoints(int value) {
        points += value;
    }

    @Override
    public Document toDocument() {
        return new Document("id", id)
                .append("nickname", nickname)
                .append("points", points);
    }

    public Player(Document document) {
        this(document.getInteger("id"),document.getString("nickname"));
        this.points = document.getInteger("points");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.getId() && nickname.equals(player.getNickname());
    }
}
