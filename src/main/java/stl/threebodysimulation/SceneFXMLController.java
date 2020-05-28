package stl.threebodysimulation;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class SceneFXMLController implements Initializable {

    // Initialize all UI elements

    @FXML
    private CheckBox infiniteCheckBox;

    @FXML
    private TextField timeskipField;

    @FXML
    private TextField simSpeedField;

    @FXML
    private Button runButton;

    @FXML
    private TextField templateIDField;

    @FXML
    private Button saveButton;

    // Initialize controllers for custom UI elements

    @FXML
    private InfoFXMLController object1InfoController;

    @FXML
    private InfoFXMLController object2InfoController;

    @FXML
    private InfoFXMLController object3InfoController;

    @FXML
    private ParameterFXMLController object1ParameterController;

    @FXML
    private ParameterFXMLController object2ParameterController;

    @FXML
    private ParameterFXMLController object3ParameterController;

    // Packages controllers into arrays.
    private InfoFXMLController[] infoControllers;
    private ParameterFXMLController[] parameterControllers;

    // Empty constructor for use by FXML.
    public SceneFXMLController() {}

    // Called by FXMLLoader in MainApp class, to finalize setup. Inputs are necessary because of parent class.
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Setup controller arrays.
        infoControllers = new InfoFXMLController[] {object1InfoController, object2InfoController, object3InfoController};
        parameterControllers = new ParameterFXMLController[] {object1ParameterController, object2ParameterController, object3ParameterController};

        // Setup each controller in each array with the correct id.
        for (int i = 0; i < 3; i++) {
            infoControllers[i].setup((byte)(i+1));
            parameterControllers[i].setup((byte)(i+1));
        }
    }
}