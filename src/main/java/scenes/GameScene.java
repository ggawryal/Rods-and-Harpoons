package scenes;

import board.*;
import board.arranger.RatioTileChooser;
import board.arranger.RectangleTileArranger;
import board.arranger.TileArranger;
import board.arranger.TileScoreChooser;
import game.GameManager;
import game.HumanController;
import game.Player;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static application.Program.MainApp.*;

public class GameScene extends Scene {
    private final static BorderPane root = new BorderPane();
    private static Pane hexes;
    private static ScrollPane scrollPane;

    private static VBox buttonsBox;
    private static Text[] playerPoints;
    private static GameManager gameManager;

    private final static int numOfPlayers = 2;
    private final static int numOfPawns = 3;

    public void load() {
        root.getChildren().clear();
        scrollPane = new ScrollPane();
        hexes = new Pane();
        buttonsBox = new VBox(10);

        scrollPane.setContent(hexes);

        scrollPane.setPannable(true);
        hexes.setOnScroll(Event::consume);
        hexes.addEventHandler(MouseEvent.ANY, event -> {
            if(event.getButton() != MouseButton.MIDDLE)
                event.consume();
        });

        BoardView view = new BoardView(hexes);
        Board board = new Board(view);
        MoveChecker moveChecker = new StandardMoveChecker(board);

        TileScoreChooser tileScoreChooser = new RatioTileChooser(3,3,1);
        TileArranger tileArranger = new RectangleTileArranger(8,8);
        tileArranger.arrange(board,tileScoreChooser);

        gameManager = new GameManager(moveChecker, board, Arrays.asList(new HumanController(), new HumanController()));
        view.setActionOnClickForExistingTiles(position -> {
            if(gameManager.getCurrentController() instanceof HumanController) {
               ((HumanController) gameManager.getCurrentController()).onClickResponse(position);
            }
        });

        ImageView logo = new ImageView(new Image("/logo_small.png"));
        logo.setFitWidth(100);
        logo.setFitHeight(100);

        playerPoints = new Text[numOfPlayers];
        for(int i=0; i<2; i++) {
            playerPoints[i] = new Text("Player " + (i+1) + ": 0");
        }

        Button btnBack = new Button();
        btnBack.setText("Go back to main menu");
        btnBack.setOnAction(event -> primaryStage.setScene(mainMenu));

        Button btnRearrange = new Button();
        btnRearrange.setText("Reset tiles");
        btnRearrange.setOnAction(event -> {
            gameScene.load();
        });

        buttonsBox.getChildren().add(logo);
        for(int i=0; i<numOfPlayers; i++) buttonsBox.getChildren().add(playerPoints[i]);
        buttonsBox.getChildren().addAll(btnRearrange,btnBack);
        buttonsBox.setAlignment(Pos.CENTER);
        root.setCenter(scrollPane);
        root.setLeft(buttonsBox);
    }

    public void updatePlayerPoints(Player player) {
        playerPoints[player.getId()].setText("Player " + (player.getId()+1) + ": " + player.getPoints());
    }

    public void showGameOver() {
        ArrayList<Player> players = gameManager.getPlayers();
        players.sort(Comparator.comparingInt(Player::getPoints).reversed());
        VBox gameOverBox = new VBox(10);
        root.getChildren().clear();

        Text gameOverText = new Text("Game over!");
        gameOverText.setFont(Font.font(20));
        Text winnerText = new Text("Player " + (players.get(0).getId()+1) + " won with " + players.get(0).getPoints() + " points!");
        winnerText.setFont(Font.font(50));
        Text[] otherPlayersText = new Text[players.size()-1];
        for(int i=1; i<players.size(); i++) {
            otherPlayersText[i-1] = new Text("Player " + (players.get(i).getId()+1) + ": " + players.get(i).getPoints() + " points.");
        }

        Button btnBack = new Button();
        btnBack.setText("Go back to main menu");
        btnBack.setOnAction(event -> primaryStage.setScene(mainMenu));

        gameOverBox.getChildren().addAll(gameOverText,winnerText);
        for(int i=0; i<players.size()-1; i++) gameOverBox.getChildren().add(otherPlayersText[i]);
        gameOverBox.getChildren().add(btnBack);

        gameOverBox.setAlignment(Pos.CENTER);
        root.setCenter(gameOverBox);
    }

    public GameScene(int width, int height) {
        super(root, width, height);
    }
}
