package game;

import board.HexVector;
import board.Move;

import java.util.ArrayList;

public class Player {
    private int id;
    private ArrayList<HexVector> pawnsPositions = new ArrayList<>();
    private int points;

    public Player(int id) {
        this.id = id;
    }

    public int getId() { return id; }

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
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int value) {
        points += value;
    }
}
