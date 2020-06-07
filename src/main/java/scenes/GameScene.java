package scenes;

import board.*;
import board.arranger.RatioTileChooser;
import board.arranger.RectangleTileArranger;
import board.arranger.TileArranger;
import board.arranger.TileScoreChooser;
import game.GameManager;
import game.HumanController;
import game.Player;
import game.RandomMovingBot;
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

    private static VBox gameStateBox;
    private static ArrayList<Text> playerPoints;
    private static GameManager gameManager;

    public void load() {
        root.getChildren().clear();
        scrollPane = new ScrollPane();
        hexes = new Pane();
        gameStateBox = new VBox(10);

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

        TileScoreChooser tileScoreChooser = new RatioTileChooser(3,2,1);
        TileArranger tileArranger = new RectangleTileArranger(8,8);
        tileArranger.arrange(board,tileScoreChooser);

        gameManager = new GameManager(moveChecker, board, Arrays.asList(new HumanController(), new RandomMovingBot()));
        view.setActionOnClickForExistingTiles(position -> {
            if(gameManager.getCurrentController() instanceof HumanController) {
               ((HumanController) gameManager.getCurrentController()).onClickResponse(position);
            }
        });

        ImageView logo = new ImageView(new Image("/logo_small.png"));
        logo.setFitWidth(100);
        logo.setFitHeight(100);

        playerPoints = new ArrayList<>();
        ArrayList<Player> players = gameManager.getPlayers();
        for(Player p : players) playerPoints.add(new Text(p.getNickname() + ": 0"));

        Button btnBack = new Button();
        btnBack.setText("Go back to main menu");
        btnBack.setOnAction(event -> {
            gameManager.endGame();
            primaryStage.setScene(mainMenu);
        });

        Button btnRearrange = new Button();
        btnRearrange.setText("Reset tiles");
        btnRearrange.setOnAction(event -> {
            gameManager.endGame();
            gameScene.load();
        });

        gameStateBox.getChildren().add(logo);
        gameStateBox.getChildren().addAll(playerPoints);
        gameStateBox.getChildren().addAll(btnRearrange,btnBack);
        gameStateBox.setAlignment(Pos.CENTER);
        root.setCenter(scrollPane);
        root.setLeft(gameStateBox);

        gameManager.startGame();
    }

    public void updatePlayerPoints(Player player) {
        playerPoints.get(player.getId()).setText(player.getNickname() + ": " + player.getPoints());
    }

    public void showGameOver() {
        ArrayList<Player> players = gameManager.getPlayers();
        players.sort(Comparator.comparingInt(Player::getPoints).reversed());
        boolean draw = players.get(0).getPoints() == players.get(1).getPoints();
        VBox gameOverBox = new VBox(10);

        Text gameOverText = new Text("Game over!");
        gameOverText.setFont(Font.font(20));
        Text winnerText;
        if(!draw) {
            winnerText = new Text(players.get(0).getNickname() + " won with " + players.get(0).getPoints() + " points!");
        } else {
            winnerText = new Text("It's a draw!");
        }
        winnerText.setFont(Font.font(50));
        ArrayList<Text> otherPlayersText = new ArrayList<>();
        if(draw) otherPlayersText.add(new Text(players.get(0).getNickname() + ": " + players.get(0).getPoints() + " points."));
        for(int i=1; i<players.size(); i++) {
            otherPlayersText.add(new Text(players.get(i).getNickname() + ": " + players.get(i).getPoints() + " points."));
        }

        Button btnHide = new Button();
        btnHide.setText("Hide");
        btnHide.setOnAction(event -> root.setCenter(scrollPane));

        gameOverBox.getChildren().addAll(gameOverText,winnerText);
        gameOverBox.getChildren().addAll(otherPlayersText);
        gameOverBox.getChildren().add(btnHide);

        gameOverBox.setAlignment(Pos.CENTER);
        root.setCenter(gameOverBox);
    }

    public GameScene(int width, int height) {
        super(root, width, height);
    }
}
