package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

// The controller for the error window that pops up when a problem occurs.
public class runErrorWindowFXMLController implements Initializable {
    @FXML
    private Button closeWindowButton;

    @FXML
    private Label messageLabel;

    public runErrorWindowFXMLController() {}

    public void setLabel(String label) {
        messageLabel.setText(label);
    }

    public void closeWindow() {
        ((Stage)(closeWindowButton.getScene().getWindow())).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) { }



}
