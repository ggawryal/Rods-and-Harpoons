package scenes;

import game.controllers.ControllerFactory;
import game.controllers.HumanControllerFactory;
import game.controllers.botcontrollerfactories.*;
import game.threads.JavaFXThreadRunner;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import util.sleeper.RealTimeSleeper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static application.Program.MainApp.*;
import static application.Properties.*;

public class Settings extends Scene {
    private final static StackPane root = new StackPane();
    private final static VBox rootBox = new VBox(50);
    private final static HBox settingsBox = new HBox(20);
    private final static VBox playerBoxes = new VBox(20);
    private final static VBox otherSettingsBox = new VBox(20);
    private Button btnNewPlayer;

    public void load() {
        ImageView logo = new ImageView(new Image("/logo.png"));

        btnNewPlayer = new Button("+");
        btnNewPlayer.setOnAction(event -> addPlayerBox());
        playerBoxes.getChildren().add(btnNewPlayer);
        playerBoxes.setAlignment(Pos.TOP_CENTER);
        playerBoxes.setMinHeight(205);
        // adding 3 and removing 1 to trigger disabling buttons
        for(int i=0; i<3; i++) addPlayerBox();
        removePlayerBox((HBox)playerBoxes.getChildren().get(2));

        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);

        Slider boardSlider = new Slider(5,20,DEFAULT_BOARD_SIZE);
        Slider pawnsSlider = new Slider(1,4,DEFAULT_PAWNS_PER_PLAYER);
        Text highscoreText = new Text("High Scores will not be updated with these settings.");

        HBox boardSizeBox = new HBox(20);
        boardSizeBox.getChildren().add(new Text("Board size: "));
        boardSlider.setMajorTickUnit(5);
        boardSlider.setMinorTickCount(4);
        boardSlider.setShowTickMarks(true);
        boardSlider.setShowTickLabels(true);
        boardSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            boardSlider.setValue(newValue.intValue());
            if(newValue.intValue() != DEFAULT_BOARD_SIZE) highscoreText.setVisible(true);
            else if(pawnsSlider.getValue() == DEFAULT_PAWNS_PER_PLAYER) highscoreText.setVisible(false);
        });
        boardSlider.setMinWidth(180);
        boardSizeBox.getChildren().add(boardSlider);

        HBox pawnsPerPlayerBox = new HBox(20);
        pawnsPerPlayerBox.getChildren().add(new Text("Pawns per player: "));
        pawnsSlider.setMajorTickUnit(1);
        pawnsSlider.setMinorTickCount(0);
        pawnsSlider.setShowTickMarks(true);
        pawnsSlider.setShowTickLabels(true);
        pawnsSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            pawnsSlider.setValue(newValue.intValue());
            if(newValue.intValue() != DEFAULT_PAWNS_PER_PLAYER) highscoreText.setVisible(true);
            else if(boardSlider.getValue() == DEFAULT_BOARD_SIZE) highscoreText.setVisible(false);
        });
        pawnsPerPlayerBox.getChildren().add(pawnsSlider);

        Button btnRestore = new Button("Restore defaults");
        btnRestore.setOnAction(event -> {
            boardSlider.setValue(DEFAULT_BOARD_SIZE);
            pawnsSlider.setValue(DEFAULT_PAWNS_PER_PLAYER);
        });
        btnRestore.setAlignment(Pos.CENTER);
        highscoreText.setVisible(false);
        highscoreText.setTextAlignment(TextAlignment.CENTER);

        otherSettingsBox.getChildren().addAll(boardSizeBox, pawnsPerPlayerBox, btnRestore, highscoreText);
        settingsBox.getChildren().addAll(playerBoxes, separator, otherSettingsBox);
        settingsBox.setAlignment(Pos.CENTER);

        Button btnPlay = new Button("Play");
        btnPlay.setFont(Font.font(50));
        btnPlay.setOnAction(event -> {
            ArrayList<String> nicknames = new ArrayList<>();
            ArrayList<ControllerFactory> controllers = new ArrayList<>();

            for(int i=0; i<playerBoxes.getChildren().size()-1; i++) {
                nicknames.add(((TextField)((HBox)playerBoxes.getChildren().get(i)).getChildren().get(0)).getText());
                controllers.add((((ChoiceBox<ControllerFactory>)((HBox)playerBoxes.getChildren().get(i)).getChildren().get(1)).getValue()));
            }

            gameScene.load(nicknames, controllers, (int)boardSlider.getValue(), (int)pawnsSlider.getValue());
            primaryStage.setScene(gameScene);
        });
        Button btnBack = new Button("Back");
        btnBack.setFont(Font.font(30));
        btnBack.setOnAction(event -> primaryStage.setScene(mainMenu));

        HBox buttonsBox = new HBox(20);
        buttonsBox.getChildren().addAll(btnPlay, btnBack);
        buttonsBox.setAlignment(Pos.CENTER);

        rootBox.getChildren().add(logo);
        rootBox.getChildren().add(settingsBox);
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
