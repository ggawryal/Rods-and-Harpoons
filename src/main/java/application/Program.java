package application;

import javafx.application.Application;
import javafx.stage.Stage;
import scenes.GameScene;
import scenes.MainMenu;

public class Program {
    public static void main(String[] args) {
        Application.launch(MainApp.class, args);
    }

    public static class MainApp extends Application {
        public static Stage primaryStage;
        public static MainMenu mainMenu;
        public static GameScene gameScene;

        @Override
        public void start(Stage stage) {
            primaryStage = stage;
            mainMenu = new MainMenu(750,650); mainMenu.load();
            gameScene = new GameScene(750,650); gameScene.load(2);

            primaryStage.setScene(mainMenu);
            primaryStage.show();
        }
    }
}