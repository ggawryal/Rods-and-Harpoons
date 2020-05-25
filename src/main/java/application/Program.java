package application;

import javafx.application.Application;
import javafx.stage.Stage;
import scenes.MainMenu;
import scenes.RandomHexes;

public class Program {
    public static void main(String[] args) {
        Application.launch(MainApp.class, args);
    }

    public static class MainApp extends Application {
        public static Stage primaryStage;
        public static MainMenu mainMenu;
        public static RandomHexes randomHexes;

        @Override
        public void start(Stage stage) throws Exception {
            primaryStage = stage;
            mainMenu = new MainMenu(750,650); mainMenu.load();
            randomHexes = new RandomHexes(750,650); randomHexes.load();

            primaryStage.setScene(mainMenu);
            primaryStage.show();
        }
    }
}