package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The FXML controller for the confirmation dialog that appears when overwriting a file.
 */
public class WarningWindowFXMLController implements Initializable {
    /**
     * The label UI element that holds the error message.
     */
    @FXML
    private Label messageLabel;

    /**
     * A listener that is called when the user confirms the warning.
     */
    private Listener confirmListener;

    /**
     * Initializes the window, according to the Application parent class. Empty.
     *
     * @param location  Not used.
     * @param resources Not used.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Sets the error message to be displayed.
     *
     * @param label The error message.
     */
    public void setLabel(String label) {
        messageLabel.setText(label);
    }

    /**
     * Method that is called when the user confirms the action.
     */
    public void confirm() {
        confirmListener.onEvent();
        closeWindow();
    }

    /**
     * Closes the window.
     */
    public void closeWindow() {
        ((Stage) (messageLabel.getScene().getWindow())).close();
    }

    /**
     * Sets the listener object for confirmation.
     *
     * @param confirmListener The listener that will be called upon confirmation.
     */
    void setConfirmListener(Listener confirmListener) {
        this.confirmListener = confirmListener;
    }
}
