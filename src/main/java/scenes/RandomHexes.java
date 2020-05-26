package scenes;

import board.*;
import board.arranger.RatioTileChooser;
import board.arranger.RectangleTileArranger;
import board.arranger.TileArranger;
import board.arranger.TileScoreChooser;
import game.GameManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import static application.Program.MainApp.mainMenu;
import static application.Program.MainApp.primaryStage;

public class RandomHexes extends Scene {
    private final static Pane root = new Pane();
    private final static Pane hexes = new Pane();
    private final static VBox vb = new VBox();

    public void load() {
        BoardView view = new BoardView(hexes);
        Board board = new Board(view);
        MoveChecker moveChecker = new StandardMoveChecker(board);
        GameManager gameManager = new GameManager(moveChecker, board, 2);
        view.setActionOnClick(gameManager::onClickResponse);

        TileScoreChooser tileScoreChooser = new RatioTileChooser(3,3,1);
        TileArranger tileArranger = new RectangleTileArranger(8,8);
        tileArranger.arrange(board,tileScoreChooser);


        Button btnBack = new Button();
        btnBack.setText("Go back to main menu.");
        btnBack.setOnAction(event -> primaryStage.setScene(mainMenu));

        Button btnRearrange = new Button();
        btnRearrange.setText("Reset tiles.");
        btnRearrange.setOnAction(event -> {
            tileArranger.arrange(board,tileScoreChooser);
            gameManager.init();
        });

        vb.getChildren().addAll(btnRearrange,btnBack);
        vb.setAlignment(Pos.CENTER);
        root.getChildren().addAll(hexes,vb);

        gameManager.init();
    }

    public RandomHexes(int width, int height) {
        super(root, width, height);
    }
}
