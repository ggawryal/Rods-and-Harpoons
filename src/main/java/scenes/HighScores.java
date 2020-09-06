package scenes;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.bson.Document;

import java.util.ArrayList;

import static application.Program.MainApp.*;

public class HighScores extends Scene {
    private final static StackPane root = new StackPane();
    private final static VBox rootBox = new VBox(30);
    TableView<Document> highScores = new TableView<>();

    public void load() {
        ImageView logo = new ImageView(new Image("/logo.png"));

        highScores.setMaxWidth(510);
        highScores.setFixedCellSize(25);
        highScores.setMaxHeight(280);
        highScores.setEditable(false);

        TableColumn<Document, Integer> column1  = new TableColumn<>("#");
        column1.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(highScores.getItems().indexOf(c.getValue()) + 1));
        column1.setSortable(false);
        column1.setMinWidth(50);
        column1.setMaxWidth(50);
        column1.setReorderable(false);

        TableColumn<Document, String> column2 = new TableColumn<>("Nickname");
        column2.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getString("nickname")));
        column2.setSortable(false);
        column2.setMinWidth(300);
        column2.setMaxWidth(300);
        column2.setReorderable(false);

        TableColumn<Document, String> column3 = new TableColumn<>("Score");
        column3.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getInteger("highscore").toString()));
        column3.setSortable(false);
        column3.setMinWidth(150);
        column3.setMaxWidth(150);
        column3.setReorderable(false);

        highScores.getColumns().addAll(column1, column2, column3);

        Button btnBack = new Button("Back");
        btnBack.setFont(Font.font(30));
        btnBack.setOnAction(event -> primaryStage.setScene(mainMenu));

        rootBox.getChildren().addAll(logo, highScores, btnBack);
        rootBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(rootBox);
    }

    public void refresh() {
        highScores.getItems().clear();
        ArrayList<Document> players = mongoDB.getPlayersHighScores(10);
        for(Document player : players) {
            highScores.getItems().add(player);
        }
    }

    public HighScores(int width, int height) {
        super(root, width, height);
    }
}
