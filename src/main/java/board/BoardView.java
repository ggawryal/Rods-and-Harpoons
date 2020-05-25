package board;

import board.tile.Tile;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.util.HashMap;
import java.util.function.Consumer;

public class BoardView {
    private static final double SIZE = 50;
    private static final double HEIGHT = SIZE*Math.sqrt(3)/2.0;

    private Pane pane;

    private HashMap<HexVector,ImageView> hexagons = new HashMap<>();

    public BoardView(Pane pane) {
        this.pane = pane;
    }

    private Consumer<HexVector> onMouseClick;
    public void setActionOnClick(Consumer<HexVector> onMouseClick) {
        this.onMouseClick = onMouseClick;
    }

    void drawTile(Tile tile, HexVector position) {
        double x = position.getEast()*HEIGHT*2 + position.getSoutheast()*HEIGHT;
        double y = position.getSoutheast()*1.5*SIZE;

        ImageView imageView = new ImageView(tile.getImage());
        imageView.setFitHeight(2.2*SIZE);
        imageView.setFitWidth(2.2*SIZE);
        imageView.setX(x);
        imageView.setY(y);

        if(onMouseClick != null)
            imageView.setOnMouseClicked(e -> onMouseClick.accept(position));

        hexagons.put(position,imageView);
        pane.getChildren().add(imageView);
    }

    void removeTile(HexVector position) {
        ImageView hex = hexagons.get(position);
        pane.getChildren().remove(hex);
        hexagons.remove(hex);
    }

}
