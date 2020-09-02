package scenes;

import board.*;
import board.arranger.RatioTileChooser;
import board.arranger.RectangleTileArranger;
import board.arranger.TileArranger;
import board.arranger.TileScoreChooser;
import board.views.BoardView;
import board.views.JavaFXBoardView;
import game.*;
import game.controllers.ControllerFactory;
import game.controllers.HumanController;
import game.controllers.PlayerController;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import util.GameInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static application.Program.MainApp.*;

public class GameScene extends Scene implements GameObserver{
    private final static BorderPane root = new BorderPane();
    private static final char fullBlockSymbol = 0x2588; //unicode

    private static Pane hexes;
    private static ScrollPane scrollPane;

    private static VBox gameStateBox;
    private static ArrayList<TextFlow> playerPoints;
    private static GameManager gameManager;

    public void load(List<String> nicknames, List<ControllerFactory> controllers) {
        loadPanes();
        BoardView view = new JavaFXBoardView(hexes);
        Board board = new Board(view);
        MoveChecker moveChecker = new StandardMoveChecker(board);

        TileScoreChooser tileScoreChooser = new RatioTileChooser(3,2,1);
        TileArranger tileArranger = new RectangleTileArranger(8,8);
        tileArranger.arrange(board,tileScoreChooser);

        gameManager = new GameManager(moveChecker, board, nicknames, controllers, 2);
        view.setActionOnClickForExistingTiles(position -> {
            if(gameManager.getCurrentController() instanceof HumanController) {
                ((HumanController) gameManager.getCurrentController()).onClickResponse(position);
            }
        });

        gameManager.setObserver(this);
        loadUI();
    }

    public void load(GameInfo gameInfo) {
        loadPanes();
        BoardView view = new JavaFXBoardView(hexes);
        Board board = new Board(view);
        MoveChecker moveChecker = new StandardMoveChecker(board);

        gameManager = new GameManager(moveChecker, board, gameInfo);
        view.setActionOnClickForExistingTiles(position -> {
            if(gameManager.getCurrentController() instanceof HumanController) {
                ((HumanController) gameManager.getCurrentController()).onClickResponse(position);
            }
        });
        gameManager.setObserver(this);
        loadUI();
    }

    public void loadPanes() {
        root.getChildren().clear();
        scrollPane = new ScrollPane();
        hexes = new Pane();
        gameStateBox = new VBox(10);

        scrollPane.setContent(hexes);

        scrollPane.setPannable(true);
        hexes.setOnScroll(Event::consume);
        hexes.addEventHandler(MouseEvent.ANY, event -> {
            if (event.getButton() != MouseButton.MIDDLE)
                event.consume();
        });
    }

    public void loadUI() {
        ImageView logo = new ImageView(new Image("/logo_small.png"));
        logo.setFitWidth(100);
        logo.setFitHeight(100);

        playerPoints = new ArrayList<>();
        ArrayList<Player> players = gameManager.getPlayers();
        for(Player p : players) {
            Text coloredMark = new Text(fullBlockSymbol + " ");
            switch(p.getId()) {
                case 0: coloredMark.setFill(new Color(1,0,0,1)); break;
                case 1: coloredMark.setFill(new Color(0,0.5,0,1)); break;
                case 2: coloredMark.setFill(new Color(0,0,1,1)); break;
                case 3: coloredMark.setFill(new Color(1,1,0,1)); break;
            }
            Text playerText = new Text(p.getNickname() + ": 0");
            TextFlow textFlow = new TextFlow(coloredMark, playerText);
            textFlow.setTextAlignment(TextAlignment.CENTER);
            playerPoints.add(textFlow);
        }

        Button btnBack = new Button();
        btnBack.setText("Go back to main menu");
        btnBack.setOnAction(event -> {
            gameManager.endGame(false);
            mainMenu.load();
            primaryStage.setScene(mainMenu);
        });

        Button btnRearrange = new Button();
        btnRearrange.setText("Reset tiles");
        btnRearrange.setOnAction(event -> {
            gameManager.endGame(false);
            //gameScene.load(nicknames, controllers);
        });

        gameStateBox.getChildren().add(logo);
        gameStateBox.getChildren().addAll(playerPoints);
        gameStateBox.getChildren().addAll(btnRearrange,btnBack);
        gameStateBox.setAlignment(Pos.CENTER);
        root.setCenter(scrollPane);
        root.setLeft(gameStateBox);

        gameManager.startGame();
    }

    @Override
    public void onPlayerPointsUpdated(Player player) {
        ((Text)playerPoints.get(player.getId()).getChildren().get(1)).setText(player.getNickname() + ": " + player.getPoints());
    }

    @Override
    public void onGameInfoUpdated(GameInfo gameInfo) {
        jsonSavefile.saveGame(gameInfo);
    }

    @Override
    public void onGameOver(GameInfo gameInfo, boolean saveGame) {
        if(saveGame) {
            jsonSavefile.saveGame(gameInfo);
            String gameId = mongoDB.saveGame(gameInfo);
            mongoDB.updatePlayersHighscores(gameInfo.getPlayers(), gameId);
        }

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
