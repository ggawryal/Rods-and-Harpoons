package scenes;

import board.HexGridDrawer;
import board.HexVector;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import static application.Program.MainApp.mainMenu;
import static application.Program.MainApp.primaryStage;

public class RandomHexes extends Scene {
    static Color[] colors = {Color.RED,Color.BLUE,Color.MAGENTA,Color.BLACK,Color.GREEN,Color.PINK,Color.BROWN};

    private final static Pane root = new Pane();
    private final static Pane hexes = new Pane();
    private final static VBox vb = new VBox();

    public void load() {
        HexGridDrawer drawer = new HexGridDrawer(hexes);
        Button btnMore = new Button();
        btnMore.setText("We need more hexes!");
        btnMore.setOnAction(event -> drawer.addHex(
                new HexVector((int)(Math.random()*20-10),(int)(Math.random()*10)),colors[(int)(Math.random()*colors.length)]));
        Button btnTooMany = new Button();
        btnTooMany.setText("Oh no, too many hexes!");
        btnTooMany.setOnAction(event -> drawer.clearHexes());
        Button btnBack = new Button();
        btnBack.setText("Go back to main menu.");
        btnBack.setOnAction(event -> primaryStage.setScene(mainMenu));

        vb.getChildren().addAll(btnMore,btnTooMany,btnBack);
        vb.setAlignment(Pos.CENTER);
        root.getChildren().addAll(hexes,vb);
    }

    public RandomHexes(int width, int height) {
        super(root, width, height);
    }
}
