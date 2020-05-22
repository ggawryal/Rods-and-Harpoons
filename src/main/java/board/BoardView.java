package board;

import board.tile.Tile;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class BoardView {
    private static final double SIZE = 40;
    private static final double HEIGHT = SIZE*Math.sqrt(3)/2.0;

    private Pane pane;

    private HashMap<HexVector,Polygon> hexagons = new HashMap<>();

    public BoardView(Pane pane) {
        this.pane = pane;
    }


    private Polygon createHexagonAt(double x, double y, double a) {
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(0.0,a, HEIGHT, a/2, HEIGHT, -a/2, 0.0,-a, -HEIGHT, -a/2, -HEIGHT, a/2);
        polygon.relocate(x,y);
        return polygon;
    }

    private Consumer<HexVector> onMouseClick;
    public void setActionOnClick(Consumer<HexVector> onMouseClick) {
        this.onMouseClick = onMouseClick;
    }

    public void drawTile(Tile tile, HexVector position) {
        double x = position.getCoordinate(Direction.E)*HEIGHT*2 + position.getCoordinate(Direction.SE)*HEIGHT;
        double y = position.getCoordinate(Direction.SE)*1.5*SIZE;
        Polygon hex = createHexagonAt(x,y,SIZE);

        hex.setFill(tile.getFill());

        if(onMouseClick != null)
            hex.setOnMouseClicked(e -> onMouseClick.accept(position));

        hexagons.put(position,hex);
        pane.getChildren().add(hex);
    }

    public void removeTile(HexVector position) {
        Polygon hex = hexagons.get(position);
        pane.getChildren().remove(hex);
        hexagons.remove(hex);
    }

}
