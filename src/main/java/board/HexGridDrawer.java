package board;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class HexGridDrawer {
    public static final double SIZE = 40;
    public static final double HEIGHT = SIZE*Math.sqrt(3)/2.0;

    private Pane pane;
    public HexGridDrawer(Pane pane) {
        this.pane = pane;
    }

    private Polygon createHexagonAt(double x, double y, double a) {
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(0.0,a, HEIGHT, a/2, HEIGHT, -a/2, 0.0,-a, -HEIGHT, -a/2, -HEIGHT, a/2);
        polygon.relocate(x,y);
        return polygon;
    }

    public void addHex(HexVector position, Color color) {
        double x = position.getCoordinate(Direction.E)*HEIGHT*2 + position.getCoordinate(Direction.SE)*HEIGHT;
        double y = position.getCoordinate(Direction.SE)*1.5*SIZE;
        Polygon hex = createHexagonAt(x,y,SIZE);
        hex.setFill(color);
        pane.getChildren().add(hex);
    }

    public void clearHexes() {
        pane.getChildren().clear();
    }

}
