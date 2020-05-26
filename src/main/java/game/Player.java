package game;

import board.HexVector;

public class Player {
    private HexVector position;
    private int points;

    public Player(HexVector position) {
        this.position = position;
    }

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
