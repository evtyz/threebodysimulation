package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The FXML controller class for an error popup window.
 */
public class ErrorWindowFXMLController implements Initializable {
    /**
     * The label UI element that holds the error message.
     */
    @FXML
    private Label messageLabel;

    /**
     * The constructor for the FXML loader.
     */
    public ErrorWindowFXMLController() {
    }

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
    void setLabel(String label) {
        messageLabel.setText(label);
    }

    /**
     * Closes the window.
     */
    public void closeWindow() {
        ((Stage) (messageLabel.getScene().getWindow())).close();
    }

    /**
     * Gets the icon for the popup window.
     *
     * @return The icon.
     */
    Image getIcon() {
        return new Image("/stl/threebodysimulation/icons/errorIcon.png");
    }


    /**
     * Sets a listener for user's actions. By default, does nothing.
     *
     * @param confirmListener The listener that is set.
     */
    void setConfirmListener(Listener confirmListener) {
    }
}
