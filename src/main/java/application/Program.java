package application;

import database.JsonSavefile;
import database.MongoDB;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import scenes.*;

import java.util.logging.Level;

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
        public static MatchHistory matchHistory;
        public static HighScores highScores;
        public static MongoDB mongoDB;
        public static JsonSavefile jsonSavefile;

        @Override
        public void start(Stage stage) {
            java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
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
            matchHistory = new MatchHistory(WIDTH, HEIGHT);
            matchHistory.load();
            highScores = new HighScores(WIDTH, HEIGHT);
            highScores.load();

            primaryStage.setScene(mainMenu);
            primaryStage.show();
        }
    }
}