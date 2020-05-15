package scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static application.Program.MainApp.primaryStage;
import static application.Program.MainApp.randomHexes;

public class MainMenu extends Scene {
    private final static StackPane root = new StackPane();
    private final static VBox vb = new VBox(50);

    public void load() {
        Text text = new Text();
        text.setText("--- Epic Title ---");

        Button btnRandomHexes = new Button();
        btnRandomHexes.setText("Drawing random hexagons");
        btnRandomHexes.setOnAction(event -> primaryStage.setScene(randomHexes));

        Button btnExit = new Button();
        btnExit.setText("Exit game");
        btnExit.setOnAction(event -> System.exit(0));

        vb.getChildren().addAll(text, btnRandomHexes, btnExit);
        vb.setAlignment(Pos.CENTER);
        root.getChildren().add(vb);
    }

    public MainMenu(int width, int height) {
        super(root,width,height);
    }
}
