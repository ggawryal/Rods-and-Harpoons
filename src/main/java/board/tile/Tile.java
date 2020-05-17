package board.tile;


import javafx.scene.paint.Color;

public class Tile {
    static javafx.scene.paint.Color[] colors = {javafx.scene.paint.Color.RED, javafx.scene.paint.Color.BLUE, javafx.scene.paint.Color.MAGENTA, javafx.scene.paint.Color.BLACK, javafx.scene.paint.Color.GREEN, javafx.scene.paint.Color.PINK, javafx.scene.paint.Color.BROWN};
    public Color color;

    public Color getFill() {
        return color;
    }
    public Tile(){
        this.color = colors[(int)(Math.random()*colors.length)];
    }
}
