package scenes;

import game.controllers.BotController;
import game.controllers.ControllerFactory;
import game.controllers.HumanControllerFactory;
import game.controllers.botcontrollerfactories.*;
import game.threads.JavaFXThreadRunner;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
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
import util.sleeper.RealTimeSleeper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static application.Program.MainApp.*;

public class Settings extends Scene {
    private final static StackPane root = new StackPane();
    private final static VBox rootBox = new VBox(50);
    private final static VBox playerBoxes = new VBox(20);
    private Button btnNewPlayer;

    private final int MIN_PLAYERS = 2;
    private final int MAX_PLAYERS = 4;
    final int MAX_NICKNAME_LENGTH = 20;

    public void load() {
        ImageView logo = new ImageView(new Image("/logo.png"));

        Button btnPlay = new Button("Play");
        btnPlay.setFont(Font.font(50));
        btnPlay.setOnAction(event -> {
            ArrayList<String> nicknames = new ArrayList<>();
            ArrayList<ControllerFactory> controllers = new ArrayList<>();

            for(int i=0; i<playerBoxes.getChildren().size()-1; i++) {
                nicknames.add(((TextField)((HBox)playerBoxes.getChildren().get(i)).getChildren().get(0)).getText());
                controllers.add((((ChoiceBox<ControllerFactory>)((HBox)playerBoxes.getChildren().get(i)).getChildren().get(1)).getValue()));
            }

            gameScene.load(nicknames, controllers);
            primaryStage.setScene(gameScene);
        });

        btnNewPlayer = new Button("+");
        btnNewPlayer.setOnAction(event -> addPlayerBox());
        playerBoxes.getChildren().add(btnNewPlayer);
        playerBoxes.setAlignment(Pos.CENTER);
        // adding 3 and removing 1 to trigger disabling buttons
        for(int i=0; i<3; i++) addPlayerBox();
        removePlayerBox((HBox)playerBoxes.getChildren().get(2));

        Button btnBack = new Button("Back");
        btnBack.setFont(Font.font(30));
        btnBack.setOnAction(event -> primaryStage.setScene(mainMenu));
        HBox buttonsBox = new HBox(20);
        buttonsBox.getChildren().addAll(btnPlay, btnBack);
        buttonsBox.setAlignment(Pos.CENTER);

        rootBox.getChildren().add(logo);
        rootBox.getChildren().addAll(playerBoxes);
        rootBox.getChildren().add(buttonsBox);
        rootBox.setAlignment(Pos.CENTER);
        root.getChildren().add(rootBox);
    }

    private void addPlayerBox() {
        HBox hBox = new HBox(20);
        TextField textField = new TextField("Player " + playerBoxes.getChildren().size());
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length() > MAX_NICKNAME_LENGTH) textField.setText(newValue.substring(0,MAX_NICKNAME_LENGTH));
        });
        List<ControllerFactory> controllerFactories = Arrays.asList(
                new HumanControllerFactory(),
                new EasyBotControllerFactory(new RealTimeSleeper(), new JavaFXThreadRunner()),
                new MediumBotControllerFactory(new RealTimeSleeper(), new JavaFXThreadRunner()),
                new HardBotControllerFactory(new RealTimeSleeper(), new JavaFXThreadRunner())
        );

        ChoiceBox<ControllerFactory> choiceBox = new ChoiceBox<>(FXCollections.observableArrayList(controllerFactories));
        choiceBox.setValue(controllerFactories.get(0));
        Button btnRemove = new Button("-");
        btnRemove.setOnAction(event -> removePlayerBox(hBox));
        hBox.getChildren().addAll(textField, choiceBox, btnRemove);
        hBox.setAlignment(Pos.CENTER);

        playerBoxes.getChildren().add(playerBoxes.getChildren().size()-1,hBox);
        if(playerBoxes.getChildren().size() == MAX_PLAYERS+1) btnNewPlayer.setDisable(true);
        for(int i=0; i<playerBoxes.getChildren().size()-1; i++) {
            ((HBox)playerBoxes.getChildren().get(i)).getChildren().get(2).setDisable(false);
        }
    }

    private void removePlayerBox(HBox hBox) {
        playerBoxes.getChildren().remove(hBox);
        if(playerBoxes.getChildren().size() == MIN_PLAYERS+1) {
            for(int i=0; i<MIN_PLAYERS; i++) {
                ((HBox)playerBoxes.getChildren().get(i)).getChildren().get(2).setDisable(true);
            }
        }
        btnNewPlayer.setDisable(false);
    }

    public Settings(int width, int height) {
        super(root,width,height);
    }
}
