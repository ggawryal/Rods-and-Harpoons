package game;

import board.HexVector;

public class Player {
    private int id;
    private HexVector position;
    private int points;

    public Player(int id, HexVector position) {
        this.id = id;
        this.position = position;
    }

    public int getId() { return id; }

    public HexVector getPosition() {
        return position;
    }

    public void setPosition(HexVector position) {
        this.position = position;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int value) {
        points += value;
    }
}
