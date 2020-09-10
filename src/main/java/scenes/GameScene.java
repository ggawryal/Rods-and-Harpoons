package scenes;

import board.Board;
import board.MoveChecker;
import board.StandardMoveChecker;
import board.arranger.RatioTileChooser;
import board.arranger.RectangleTileArranger;
import board.arranger.TileArranger;
import board.arranger.TileScoreChooser;
import board.views.BoardView;
import board.views.JavaFXBoardView;
import game.GameManager;
import game.GameObserver;
import game.Player;
import game.controllers.ControllerFactory;
import game.controllers.HumanController;
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
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import util.GameInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static application.Program.MainApp.*;

public class GameScene extends Scene implements GameObserver{
    private final static BorderPane root = new BorderPane();
    private static final char fullBlockSymbol = 0x2B22; //unicode

    private static Pane hexes;
    private static ScrollPane scrollPane;

    private static VBox gameStateBox;
    private static VBox playersBox;
    private static GameManager gameManager;

    public void load(List<String> nicknames, List<ControllerFactory> controllers, int boardSize, int pawnsPerPlayer) {
        loadPanes();
        BoardView view = new JavaFXBoardView(hexes);
        Board board = new Board(view, boardSize);
        MoveChecker moveChecker = new StandardMoveChecker(board);

        TileScoreChooser tileScoreChooser = new RatioTileChooser(3,2,1);
        TileArranger tileArranger = new RectangleTileArranger(boardSize,boardSize);
        tileArranger.arrange(board,tileScoreChooser);

        gameManager = new GameManager(moveChecker, board, nicknames, controllers, pawnsPerPlayer);
        view.setActionOnClickForExistingTiles(position -> {
            if(gameManager.getCurrentController() instanceof HumanController) {
                ((HumanController) gameManager.getCurrentController()).onClickResponse(position);
            }
        });

        gameManager.setObserver(this);
        loadUI(gameManager.getPlayers(), controllers, boardSize, pawnsPerPlayer);
    }

    public void load(GameInfo gameInfo) {
        loadPanes();
        BoardView view = new JavaFXBoardView(hexes);
        Board board = new Board(view, gameInfo.getBoardSize());
        MoveChecker moveChecker = new StandardMoveChecker(board);

        gameManager = new GameManager(moveChecker, board, gameInfo);
        view.setActionOnClickForExistingTiles(position -> {
            if (gameManager.getCurrentController() instanceof HumanController) {
                ((HumanController) gameManager.getCurrentController()).onClickResponse(position);
            }
        });
        gameManager.setObserver(this);
        loadUI(gameManager.getPlayers(), gameInfo.getControllerFactories(), gameInfo.getBoardSize(), gameInfo.getPawnsPerPlayer());
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

    public void loadUI(List<Player> players, List<ControllerFactory> controllers, int boardSize, int pawnsPerPlayer) {
        ImageView logo = new ImageView(new Image("/logo_small.png"));
        logo.setFitWidth(200);
        logo.setFitHeight(200);

        playersBox = new VBox(10);
        playersBox.setMinHeight(200);

        for(Player p : players) {
            Text coloredMark = new Text(fullBlockSymbol + " ");
            coloredMark.setFill(p.getColor());
            coloredMark.setFont(Font.font(20));
            Text playerText = new Text(p.getNickname() + ": 0");
            playerText.setFont(Font.font(20));
            TextFlow textFlow = new TextFlow(coloredMark, playerText);
            textFlow.setTextAlignment(TextAlignment.CENTER);
            playersBox.getChildren().add(textFlow);
        }

        Button btnExit = new Button("Exit");
        btnExit.setOnAction(event -> {
            gameManager.endGame(false);
            mainMenu.refresh();
            primaryStage.setScene(mainMenu);
        });

        Button btnReset = new Button("Reset");
        btnReset.setOnAction(event -> {
            gameManager.endGame(false);
            ArrayList<String> nicknames = new ArrayList<>();
            for(Player player : players) {
                nicknames.add(player.getNickname());
            }
            gameScene.load(nicknames, controllers, boardSize, pawnsPerPlayer);
        });

        gameStateBox.getChildren().add(logo);
        gameStateBox.getChildren().addAll(playersBox);
        gameStateBox.getChildren().addAll(btnReset, btnExit);
        gameStateBox.setAlignment(Pos.CENTER);
        gameStateBox.setMaxWidth(200);
        root.setCenter(scrollPane);
        root.setLeft(gameStateBox);

        gameManager.startGame();
    }

    @Override
    public void onPlayerPointsUpdated(Player player) {
        ((Text)((TextFlow)playersBox.getChildren().get(player.getId())).getChildren().get(1))
                .setText(player.getNickname() + ": " + player.getPoints());
    }

    @Override
    public void onGameInfoUpdated(GameInfo gameInfo) {
        jsonSavefile.saveGame(gameInfo);
    }

    @Override
    public void onGameOver(GameInfo gameInfo, boolean updateHighScore) {
        jsonSavefile.saveGame(gameInfo);
        mongoDB.saveGame(gameInfo);
        if(updateHighScore) {
            mongoDB.updatePlayersHighScore(gameInfo.getPlayers(), gameInfo.getControllerFactories());
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

        Button btnHide = new Button("Hide");
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
