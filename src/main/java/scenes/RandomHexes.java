package scenes;

import board.Board;
import board.BoardView;
import board.HexVector;
import board.tile.Tile;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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


        Button btnMore = new Button();
        btnMore.setText("We need more hexes!");


        btnMore.setOnAction(event -> {
                    for (int tries = 0; tries < 20; tries++) {
                        if (board.addTile(new Tile(), new HexVector((int) (Math.random() * 20 - 10), (int) (Math.random() * 10))))
                            return;
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Nah, we don't need more hexes");
                    alert.showAndWait();
                });

        Button btnTooMany = new Button();
        btnTooMany.setText("Oh no, too many hexes!");
        btnTooMany.setOnAction(event -> view.clear());
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
