package scenes;

import game.controllers.*;
import game.controllers.strategies.GreedyStrategy;
import game.controllers.strategies.RandomMoveStrategy;
import game.threads.JavaFXThreadRunner;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import util.RealTimeSleeper;

import java.util.ArrayList;

import static application.Program.MainApp.gameScene;
import static application.Program.MainApp.primaryStage;

public class Settings extends Scene {
    private final static StackPane root = new StackPane();
    private final static VBox rootBox = new VBox(50);
    private final static VBox playerBoxes = new VBox(20);

    public void load() {
        ImageView logo = new ImageView(new Image("/logo.png"));

        for(int i=0; i<2; i++) addPlayerBox();

        Button btnPlay = new Button();
        btnPlay.setFont(Font.font(50));
        btnPlay.setText("Play");
        btnPlay.setOnAction(event -> {
            ArrayList<String> nicknames = new ArrayList<>();
            ArrayList<PlayerController> controllers = new ArrayList<>();

            for(Node playerBox : playerBoxes.getChildren()) {
                nicknames.add(((TextField)((HBox)playerBox).getChildren().get(0)).getText());
                switch(((ChoiceBox<String>)((HBox)playerBox).getChildren().get(1)).getValue()) {
                    case "Human":
                        controllers.add(new HumanController());
                        break;
                    case "Easy Bot":
                        controllers.add(new BotController(new RandomMoveStrategy(), new RealTimeSleeper(),new JavaFXThreadRunner()));
                        break;
                    case "Medium Bot":
                        controllers.add(new BotController(new GreedyStrategy(), new RealTimeSleeper(),new JavaFXThreadRunner()));
                        break;
                }
            }

            gameScene.load(nicknames, controllers);
            primaryStage.setScene(gameScene);
        });

        rootBox.getChildren().add(logo);
        rootBox.getChildren().addAll(playerBoxes);
        rootBox.getChildren().add(btnPlay);
        rootBox.setAlignment(Pos.CENTER);
        root.getChildren().add(rootBox);
    }

    private void addPlayerBox() {
        HBox hBox = new HBox(20);
        TextField textField = new TextField("Player " + (playerBoxes.getChildren().size()+1));
        ChoiceBox<String> choiceBox = new ChoiceBox<>(FXCollections.observableArrayList("Human", "Easy Bot", "Medium Bot"));
        choiceBox.setValue("Human");
        hBox.getChildren().addAll(textField, choiceBox);
        hBox.setAlignment(Pos.CENTER);
        playerBoxes.getChildren().add(hBox);
    }

    public Settings(int width, int height) {
        super(root,width,height);
    }
}
