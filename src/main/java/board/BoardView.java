package board;

import board.drawable.Drawable;
import board.drawable.pawn.Pawn;
import board.drawable.tile.Tile;
import board.drawable.tile.TileImageLoader;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.function.Consumer;

public class BoardView {
    private static final double SIZE = 50;
    private static final double HEIGHT = SIZE*Math.sqrt(3)/2.0;

    private Pane pane;
    private TileImageLoader tileImageLoader = new TileImageLoader();
    private HashMap<HexVector,ImageView> hexagons = new HashMap<>();
    private HashMap<HexVector,ImageView> pawns = new HashMap<>();

    public BoardView(Pane pane) {
        this.pane = pane;
    }

    private Consumer<HexVector> onMouseClick;
    public void setActionOnClick(Consumer<HexVector> onMouseClick) {
        this.onMouseClick = onMouseClick;
    }

    private ImageView drawObject(Drawable object, HexVector position) {
        double x = position.getEast()*HEIGHT*2 + position.getSoutheast()*HEIGHT;
        double y = position.getSoutheast()*1.5*SIZE;

        ImageView imageView = new ImageView(tileImageLoader.get(object.getImagePath()));
        imageView.setFitHeight(2.2*SIZE);
        imageView.setFitWidth(2.2*SIZE);
        imageView.setX(x);
        imageView.setY(y);

        if(onMouseClick != null)
            imageView.setOnMouseClicked(mouseEvent -> {
                if (!mouseEvent.getButton().equals(MouseButton.MIDDLE))
                    onMouseClick.accept(position);
            });

        return imageView;
    }

    void drawTile(Tile tile, HexVector position) {
        ImageView imageView = drawObject(tile, position);

        hexagons.put(position,imageView);
        pane.getChildren().add(imageView);
    }

    void drawPawn(Pawn pawn, HexVector position) {
        ImageView imageView = drawObject(pawn, position);
        imageView.setViewOrder(-2);

        pawns.put(position, imageView);
        pane.getChildren().add(imageView);
    }

    void switchPawnSelection(HexVector position) {
        ImageView imageView = pawns.get(position);
        if(imageView.getEffect() == null) {
            imageView.setViewOrder(-3);
            imageView.setEffect(new DropShadow());
        } else {
            imageView.setViewOrder(-2);
            imageView.setEffect(null);
        }
    }

    void switchTileGlow(HexVector position) {
        ImageView imageView = hexagons.get(position);
        if(imageView.getEffect() == null) {
            imageView.setViewOrder(-1);
            imageView.setEffect(new Glow(0.5));
        } else {
            imageView.setViewOrder(0);
            imageView.setEffect(null);
        }
    }

    void removeTile(HexVector position) {
        ImageView hex = hexagons.get(position);
        pane.getChildren().remove(hex);
        hexagons.remove(hex);
    }

    void removePawn(HexVector position) {
        ImageView pawn = pawns.get(position);
        pane.getChildren().remove(pawn);
        pawns.remove(pawn);
    }

}
