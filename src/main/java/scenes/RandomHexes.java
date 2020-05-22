package scenes;

import board.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import static application.Program.MainApp.mainMenu;
import static application.Program.MainApp.primaryStage;

public class RandomHexes extends Scene {
    private final static Pane root = new Pane();
    private final static Pane hexes = new Pane();
    private final static VBox vb = new VBox();

    public void load() {
        BoardView view = new BoardView(hexes);
        Board board = new Board(view);
        view.setActionOnClick(board::removeTile);

        TileArranger ts = new RectangleTileArranger(8,8);
        ts.arrange(board);


        Button btnBack = new Button();
        btnBack.setText("Go back to main menu.");
        btnBack.setOnAction(event -> primaryStage.setScene(mainMenu));

        Button btnRearrange = new Button();
        btnRearrange.setText("Reset tiles.");
        btnRearrange.setOnAction(event -> {ts.arrange(board);});

        vb.getChildren().addAll(btnRearrange,btnBack);
        vb.setAlignment(Pos.CENTER);
        root.getChildren().addAll(hexes,vb);
    }

    public RandomHexes(int width, int height) {
        super(root, width, height);
    }
}
