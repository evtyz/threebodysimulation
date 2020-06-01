package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The FXML controller class for an error popup window.
 */
public class PopupWindowFXMLController implements Initializable {
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
    public PopupWindowFXMLController() {
    }

    /**
     * Sets the error message to be displayed.
     * @param label The error message.
     */
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
     * Initializes the window, according to the Application parent class. Empty.
     * @param location Not used.
     * @param resources Not used.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
