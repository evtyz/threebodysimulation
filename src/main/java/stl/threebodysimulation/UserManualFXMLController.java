package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

// TODO documentation

public class UserManualFXMLController implements Initializable {

    @FXML
    TextFlow manualTextFlow;

    @FXML
    TextFlow creditsTextFlow;

    public UserManualFXMLController() {

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Text manualText = new Text();
        Text creditsText = new Text();

        manualText.setText("lol");
        creditsText.setText("lmao");

        manualTextFlow.getChildren().add(manualText);
        creditsTextFlow.getChildren().add(creditsText);

        // Load text here.
    }
}
