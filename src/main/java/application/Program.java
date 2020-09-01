package application;

import database.JsonSavefile;
import database.MongoDB;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import scenes.GameScene;
import scenes.MainMenu;
import scenes.Settings;

public class Program {
    public static void main(String[] args) {
        Application.launch(MainApp.class, args);
    }

    public static class MainApp extends Application {
        public static final int WIDTH = 900;
        public static final int HEIGHT = 650;
        public static Stage primaryStage;
        public static MainMenu mainMenu;
        public static Settings settings;
        public static GameScene gameScene;
        public static MongoDB mongoDB;
        public static JsonSavefile jsonSavefile;

        @Override
        public void start(Stage stage) {
            mongoDB = new MongoDB();
            jsonSavefile = new JsonSavefile("game.sav");

            primaryStage = stage;
            primaryStage.setTitle("Rods and Harpoons");
            stage.getIcons().add(new Image("logo_small.png"));
            primaryStage.setOnCloseRequest(e -> {
                Platform.exit();
                System.exit(0);
            });

            mainMenu = new MainMenu(WIDTH,HEIGHT);
            mainMenu.load();
            settings = new Settings(WIDTH, HEIGHT);
            settings.load();
            gameScene = new GameScene(WIDTH,HEIGHT);

            primaryStage.setScene(mainMenu);
            primaryStage.show();
        }
    }
}