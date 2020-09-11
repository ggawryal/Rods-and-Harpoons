package scenes;

import com.mongodb.MongoException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public abstract class DatabaseOperationWithAlertOnFail <T> {
    enum Action {
        RETRY, SKIP
    };

    public abstract T operationOnDatabase();

    public final T tryOperation() {
        DatabaseOperationWithAlertOnFail.Action actionChosen;
        do {
            try {
                return operationOnDatabase();
            }
            catch (MongoException e){
                actionChosen = displayAlert();
            }
        }while(actionChosen == DatabaseOperationWithAlertOnFail.Action.RETRY);
        return null;
    }

    Action displayAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Database error");
        alert.setHeaderText("Connection with database failed");
        alert.setContentText("Check internet connection");

        ButtonType retryButton = new ButtonType("Retry");
        ButtonType skipButton = new ButtonType("Skip (data won't be saved)", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(retryButton, skipButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == retryButton){
            return Action.RETRY;
        } else {
            return Action.SKIP;
        }
    }
}
