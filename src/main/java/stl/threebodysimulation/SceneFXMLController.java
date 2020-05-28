package stl.threebodysimulation;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;

public class SceneFXMLController implements Initializable {

    // Initialize all UI elements

    @FXML
    private CheckBox infiniteCheckBox;

    @FXML
    private TextField timeskipField;

    @FXML
    private Tooltip timeskipTooltip;

    private TextFieldWrapper timeskipWrapper;

    @FXML
    private TextField simSpeedField;

    @FXML
    private Tooltip simSpeedTooltip;

    private TextFieldWrapper simSpeedWrapper;

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

    // Default colors
    private Color[] defaultColors = {Color.RED, Color.BLUE, Color.GREEN};

    // Default limits
    private double MAX_ABS_TIMESKIP = 10000;
    private double MIN_SIMULATION_SPEED = 0.01;
    private double MAX_SIMULATION_SPEED = 100;

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
            parameterControllers[i].setup((byte)(i+1), defaultColors[i]);
        }

        // Wrap text fields and related tooltips, along with limits, into one object.
        timeskipWrapper = new TextFieldWrapper(timeskipField, timeskipTooltip, -MAX_ABS_TIMESKIP, MAX_ABS_TIMESKIP);
        simSpeedWrapper = new TextFieldWrapper(simSpeedField, simSpeedTooltip, MIN_SIMULATION_SPEED, MAX_SIMULATION_SPEED);
    }
}