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
import static application.Properties.MAX_NICKNAME_LENGTH;

public class MatchHistory extends Scene {
    private final static StackPane root = new StackPane();
    private final static VBox rootBox = new VBox(50);
    private final static VBox gamesBox = new VBox(20);
    private static final char fullBlockSymbol = 0x2B22;
    private final static ListView<String> gamesList = new ListView<>();
    private ArrayList<Document> games;
    private VBox playersBox;

    public void load() {
        ImageView logo = new ImageView(new Image("/logo.png"));
        TextField textField = new TextField("nickname");
        textField.setPromptText("nickname");
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > MAX_NICKNAME_LENGTH)
                textField.setText(newValue.substring(0, MAX_NICKNAME_LENGTH));
        });
        textField.setMaxWidth(500);
        textField.setFont(Font.font(30));
        textField.setAlignment(Pos.CENTER);

        gamesBox.setVisible(false);
        gamesList.setMaxWidth(500);

        Button btnSearch = new Button("Search");
        btnSearch.setFont(Font.font(50));
        btnSearch.setOnAction(event -> {
            rootBox.setVisible(false);
            games = mongoDB.getPlayerMatches(textField.getText());
            for (Document gameDocument : games) {
                StringBuilder stringBuilder = new StringBuilder();
                GameInfo game;
                try {
                    game = new GameInfo(gameDocument);
                    for (Player player : game.getPlayers()) {
                        if (player.getNickname().equals(textField.getText())) {
                            stringBuilder.append(" Points: ").append(player.getPoints());
                            break;
                        }
                    }
                    stringBuilder.append(", ")
                            .append(gameDocument.getObjectId("_id").getDate().toString());
                    gamesList.getItems().add(stringBuilder.toString());
                } catch(Exception e) {
                    System.err.println("Failed to load game: " + e.getMessage());
                }
            }
            gamesBox.setVisible(true);
        });

        Button btnBack = new Button("Back");
        btnBack.setFont(Font.font(30));
        btnBack.setOnAction(event -> primaryStage.setScene(mainMenu));

        Button btnShow = new Button("Show");
        btnShow.setFont(Font.font(50));
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
            Board board = new Board(view, game.getBoardSize());

            for(var entry : game.getTiles().entrySet()) {
                board.addTile(entry.getValue(), entry.getKey());
            }
            for(var entry : game.getPawns().entrySet()) {
                board.addPawn(entry.getValue(), entry.getKey());
            }

            VBox gameStateBox = new VBox(10);
            playersBox = new VBox(10);
            playersBox.setMinHeight(200);

            ImageView logoSmall = new ImageView(new Image("/logo_small.png"));
            logoSmall.setFitWidth(150);
            logoSmall.setFitHeight(150);


            for(Player p : game.getPlayers()) {
                Text coloredMark = new Text(fullBlockSymbol + " ");
                coloredMark.setFill(p.getColor());
                coloredMark.setFont(Font.font(20));
                Text playerText = new Text(p.getNickname() + ": 0");
                playerText.setFont(Font.font(20));
                TextFlow textFlow = new TextFlow(coloredMark, playerText);
                textFlow.setTextAlignment(TextAlignment.CENTER);
                playersBox.getChildren().add(textFlow);
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

            HBox hBox = new HBox(5);

            Button btnPrevMove = new Button("<");
            btnPrevMove.setOnAction(event4 -> {
                if(it.hasPrevious()) {
                    undoMove(board, game, it.previous());
                }
            });

            hBox.getChildren().addAll(btnPrevMove, btnNextMove);
            hBox.setAlignment(Pos.CENTER);

            gameStateBox.getChildren().add(logoSmall);
            gameStateBox.getChildren().add(playersBox);
            gameStateBox.getChildren().addAll(hBox);
            gameStateBox.setAlignment(Pos.CENTER);

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

        Button btnGamesBack = new Button("Back");
        btnGamesBack.setFont(Font.font(30));
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
        ((Text)((TextFlow)playersBox.getChildren().get(player.getId())).getChildren().get(1))
                .setText(player.getNickname() + ": " + player.getPoints());
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
