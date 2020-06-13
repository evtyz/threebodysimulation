package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * The FXML controller class for an error popup window.
 */
public class ErrorWindowFXMLController extends PopupWindowFXMLController {
    /**
     * The button UI element to close the window.
     */
    @FXML
    private Button closeWindowButton;

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
     * Sets the error message to be displayed.
     *
     * @param label The error message.
     */
    @Override
    public void setLabel(String label) {
        messageLabel.setText(label);
    }

    /**
     * Closes the window.
     */
    public void closeWindow() {
        ((Stage) (closeWindowButton.getScene().getWindow())).close();
    }

    /**
     * Gets the icon for the popup window.
     *
     * @return The icon.
     */
    @Override
    Image getIcon() {
        return new Image("/stl/threebodysimulation/icons/errorIcon.png");
    }
}
