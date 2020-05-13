import board.HexGridDrawer;
import board.HexVector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Program {
    public static void main(String[] args) {
        Application.launch(MainApp.class, args);
    }

    public static class MainApp extends Application {
        static Color[] colors = {Color.RED,Color.BLUE,Color.MAGENTA,Color.BLACK,Color.GREEN,Color.PINK,Color.BROWN};
        @Override
        public void start(Stage stage) throws Exception {
            Pane pane = new Pane();
            HexGridDrawer drawer = new HexGridDrawer(pane);

            Button button = new Button();
            button.setText("We need more hexes!");

            button.setOnAction(event -> drawer.addHex(
                    new HexVector((int)(Math.random()*20-10),(int)(Math.random()*10)),colors[(int)(Math.random()*colors.length)]));
            button.relocate(0,500);
            pane.getChildren().add(button);
            stage.setScene(new Scene(pane, 800, 600));
            stage.show();
        }
    }
}