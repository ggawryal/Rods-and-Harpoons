package scenes;

import board.Board;
import board.Move;
import board.drawable.tile.ScoreTile;
import board.views.BoardView;
import board.views.JavaFXBoardView;
import game.Player;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bson.Document;
import util.GameInfo;
import util.PlayerMove;

import java.util.ArrayList;
import java.util.Comparator;

import static application.Program.MainApp.*;

public class MatchHistory extends Scene {
    private final static StackPane root = new StackPane();
    private final static VBox rootBox = new VBox(50);
    private final static VBox gamesBox = new VBox(20);
    private static final char fullBlockSymbol = 0x2588;
    private final static ListView<String> gamesList = new ListView<>();
    private ArrayList<Document> games;
    ArrayList<TextFlow> playerPoints;

    public void load() {
        ImageView logo = new ImageView(new Image("/logo.png"));
        TextField textField = new TextField("nickname");
        textField.setMaxWidth(500);
        textField.setFont(Font.font(30));
        textField.setAlignment(Pos.CENTER);

        gamesBox.setVisible(false);

        Button btnSearch = new Button();
        btnSearch.setFont(Font.font(50));
        btnSearch.setText("Search");
        btnSearch.setOnAction(event -> {
            rootBox.setVisible(false);
            games = mongoDB.getPlayerMatches(textField.getText());
            for (Document gameDocument : games) {
                StringBuilder stringBuilder = new StringBuilder();
                GameInfo game = new GameInfo(gameDocument);
                for (Player player : game.getPlayers()) {
                    if (player.getNickname().equals(textField.getText())) {
                        stringBuilder.append(" Points: ").append(player.getPoints());
                        break;
                    }
                }
                stringBuilder.append(", ")
                        .append(gameDocument.getObjectId("_id").getDate().toString());
                gamesList.getItems().add(stringBuilder.toString());
            }
            gamesBox.setVisible(true);
        });

        Button btnBack = new Button();
        btnBack.setFont(Font.font(30));
        btnBack.setText("Back");
        btnBack.setOnAction(event -> primaryStage.setScene(mainMenu));

        Button btnShow = new Button();
        btnShow.setFont(Font.font(50));
        btnShow.setText("Show");
        btnShow.setOnAction(event -> {
            GameInfo game = new GameInfo(games.get(gamesList.getSelectionModel().getSelectedIndex()));
            game.getPlayers().sort(Comparator.comparingInt(Player::getId));

            final Stage dialog = new Stage();
            final BorderPane root = new BorderPane();
            Pane hexes = new Pane();
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(hexes);
            scrollPane.setPannable(true);

            BoardView view = new JavaFXBoardView(hexes);
            Board board = new Board(view);

            for(var entry : game.getTiles().entrySet()) {
                board.addTile(entry.getValue(), entry.getKey());
            }
            for(var entry : game.getPawns().entrySet()) {
                board.addPawn(entry.getValue(), entry.getKey());
            }

            VBox gameStateBox = new VBox(10);
            playerPoints = new ArrayList<>();

            ImageView logoSmall = new ImageView(new Image("/logo_small.png"));
            logoSmall.setFitWidth(100);
            logoSmall.setFitHeight(100);


            for(Player p : game.getPlayers()) {
                Text coloredMark = new Text(fullBlockSymbol + " ");
                coloredMark.setFill(p.getColor());
                Text playerText = new Text(p.getNickname() + ": " + p.getPoints());
                TextFlow textFlow = new TextFlow(coloredMark, playerText);
                textFlow.setTextAlignment(TextAlignment.CENTER);
                playerPoints.add(textFlow);
            }

            var it = game.getPlayersMoves().listIterator();
            while(it.hasNext()) it.next();
            while(it.hasPrevious()) {
                undoMove(board, game, it.previous());
            }

            Button btnNextMove = new Button(">");
            btnNextMove.setOnAction(event3 -> {
                if(it.hasNext()) {
                    doMove(board, game, it.next());
                }
            });

            HBox hbox = new HBox(5);

            Button btnPrevMove = new Button("<");
            btnPrevMove.setOnAction(event4 -> {
                if(it.hasPrevious()) {
                    undoMove(board, game, it.previous());
                }
            });

            hbox.getChildren().addAll(btnPrevMove, btnNextMove);
            hbox.setAlignment(Pos.CENTER);

            gameStateBox.getChildren().add(logoSmall);
            gameStateBox.getChildren().addAll(playerPoints);
            gameStateBox.setAlignment(Pos.CENTER);
            gameStateBox.getChildren().addAll(hbox);

            root.setCenter(scrollPane);
            root.setLeft(gameStateBox);

            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(primaryStage);
            dialog.setTitle(gamesList.getSelectionModel().getSelectedItem());

            Scene scene = new Scene(root, WIDTH, HEIGHT);
            dialog.setScene(scene);
            dialog.show();
        });
        btnShow.setDisable(true);
        gamesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) btnShow.setDisable(false);
            else btnShow.setDisable(true);
        });

        Button btnGamesBack = new Button();
        btnGamesBack.setFont(Font.font(30));
        btnGamesBack.setText("Back");
        btnGamesBack.setOnAction(event -> {
            gamesBox.setVisible(false);
            gamesList.getItems().clear();
            rootBox.setVisible(true);
        });

        rootBox.getChildren().addAll(logo, textField, btnSearch, btnBack);
        rootBox.setAlignment(Pos.CENTER);

        gamesBox.getChildren().addAll(gamesList, btnShow, btnGamesBack);
        gamesBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(rootBox, gamesBox);
    }

    public void onPlayerPointsUpdated(Player player) {
        ((Text)playerPoints.get(player.getId()).getChildren().get(1)).setText(player.getNickname() + ": " + player.getPoints());
    }

    public void doMove(Board board, GameInfo game, PlayerMove move) {
        board.removeTile(move.getMove().getFrom());
        Player player = game.getPlayers().get(move.getPlayerId());
        board.movePawn(move.getMove());
        player.addPoints(move.getPoints());
        onPlayerPointsUpdated(player);
    }

    public void undoMove(Board board, GameInfo game, PlayerMove move) {
        board.addTile(new ScoreTile(move.getPoints()), move.getMove().getFrom());
        Player player = game.getPlayers().get(move.getPlayerId());
        board.movePawn(new Move(move.getMove().getTo(), move.getMove().getFrom()));
        player.addPoints(-move.getPoints());
        onPlayerPointsUpdated(player);
    }

    public MatchHistory(int width, int height) {
        super(root, width, height);
    }
}
