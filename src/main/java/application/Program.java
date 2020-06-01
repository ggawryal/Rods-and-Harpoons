package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import scenes.GameScene;
import scenes.MainMenu;

public class Program {
    public static void main(String[] args) {
        Application.launch(MainApp.class, args);
    }

    public static class MainApp extends Application {
        public static final int WIDTH = 900;
        public static final int HEIGHT = 650;
        public static Stage primaryStage;
        public static MainMenu mainMenu;
        public static GameScene gameScene;

        @Override
        public void start(Stage stage) {
            primaryStage = stage;
            primaryStage.setTitle("Rods and Harpoons");
            stage.getIcons().add(new Image("logo_small.png"));
            primaryStage.setOnCloseRequest(e -> {
                Platform.exit();
                System.exit(0);
            });

            mainMenu = new MainMenu(WIDTH,HEIGHT);
            mainMenu.load();
            gameScene = new GameScene(WIDTH,HEIGHT);

            primaryStage.setScene(mainMenu);
            primaryStage.show();

        }
    }
}