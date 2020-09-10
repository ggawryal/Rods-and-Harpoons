package board.views;

import board.HexVector;
import board.Move;
import board.drawable.Drawable;
import board.drawable.pawn.Pawn;
import board.drawable.tile.Tile;
import board.drawable.tile.TileImageLoader;
import javafx.animation.TranslateTransition;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class JavaFXBoardView implements BoardView {
    private static final double SIZE = 50;
    private static final double HEIGHT = SIZE*Math.sqrt(3)/2.0;

    private Pane pane;
    private TileImageLoader tileImageLoader = new TileImageLoader();
    private HashMap<HexVector,ImageView> hexagons = new HashMap<>();
    private HashMap<HexVector,ImageView> pawns = new HashMap<>();

    public JavaFXBoardView(Pane pane) {
        this.pane = pane;
    }


    private ImageView drawObject(Drawable object, HexVector position) {
        double x = position.getEast()*HEIGHT*2 + position.getSoutheast()*HEIGHT;
        double y = position.getSoutheast()*1.5*SIZE;

        ImageView imageView = new ImageView(tileImageLoader.get(object.getImagePath()));
        imageView.setFitHeight(2.01*SIZE);
        imageView.setFitWidth(2.01*SIZE);
        imageView.setX(x);
        imageView.setY(y);

        return imageView;
    }

    @Override
    public void setActionOnClickForExistingTiles(Consumer<HexVector> onMouseClick) {
        for(Map.Entry<HexVector, ImageView> entry : hexagons.entrySet()) {
            entry.getValue().setOnMouseClicked(mouseEvent -> {
                if (!mouseEvent.getButton().equals(MouseButton.MIDDLE))
                    onMouseClick.accept(entry.getKey());
            });
        }
    }

    @Override
    public void drawTile(Tile tile, HexVector position) {
        ImageView imageView = drawObject(tile, position);

        hexagons.put(position,imageView);
        pane.getChildren().add(imageView);
    }

    @Override
    public void drawPawn(Pawn pawn, HexVector position) {
        ImageView imageView = drawObject(pawn, position);
        pawns.put(position, imageView);
        pane.getChildren().add(imageView);
    }

    @Override
    public void switchPawnSelection(HexVector position) {
        ImageView imageView = pawns.get(position);
        if(imageView.getEffect() == null) {
            imageView.setViewOrder(-1);
            imageView.setEffect(new DropShadow());
        } else {
            imageView.setViewOrder(0);
            imageView.setEffect(null);
        }
    }

    @Override
    public void switchTileGlow(HexVector position) {
        ImageView imageView = hexagons.get(position);
        if(imageView.getEffect() == null) {
            imageView.setEffect(new Glow(0.5));
        } else {
            imageView.setEffect(null);
        }
    }

    @Override
    public void playMoveTransition(Pawn pawn, Move move) {
        pawns.get(move.getTo()).setVisible(false);
        ImageView transitionPawn = drawObject(pawn, move.getFrom());
        pane.getChildren().add(transitionPawn);
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(transitionPawn);
        transition.setToX(hexagons.get(move.getTo()).getX() - hexagons.get(move.getFrom()).getX());
        transition.setToY(hexagons.get(move.getTo()).getY() - hexagons.get(move.getFrom()).getY());
        transition.setDuration(Duration.millis(200+move.getDistance()*50));
        transition.setOnFinished(event -> {
            pawns.get(move.getTo()).setVisible(true);
            pane.getChildren().remove(transitionPawn);
        });
        transition.play();
    }

    @Override
    public void removeTile(HexVector position) {
        ImageView hex = hexagons.get(position);
        pane.getChildren().remove(hex);
        hexagons.remove(hex);
    }

    @Override
    public void removePawn(HexVector position) {
        ImageView pawn = pawns.get(position);
        pane.getChildren().remove(pawn);
        pawns.remove(pawn);
    }

}
