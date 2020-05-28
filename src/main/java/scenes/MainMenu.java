package scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import static application.Program.MainApp.*;

public class MainMenu extends Scene {
    private final static StackPane root = new StackPane();
    private final static VBox rootBox = new VBox(50);
    private final static VBox buttonsBox = new VBox(20);

    public void load() {
        ImageView logo = new ImageView(new Image("/logo.png"));

        Button btnPlay = new Button();
        btnPlay.setFont(Font.font(50));
        btnPlay.setText("Play");
        btnPlay.setOnAction(event -> primaryStage.setScene(gameScene));

        Button btnExit = new Button();
        btnExit.setFont(Font.font(50));
        btnExit.setText("Exit");
        btnExit.setOnAction(event -> System.exit(0));

        buttonsBox.getChildren().addAll(btnPlay, btnExit);
        buttonsBox.setAlignment(Pos.CENTER);
        rootBox.getChildren().addAll(logo, buttonsBox);
        rootBox.setAlignment(Pos.CENTER);
        root.getChildren().add(rootBox);
    }

    public MainMenu(int width, int height) {
        super(root,width,height);
    }
}
