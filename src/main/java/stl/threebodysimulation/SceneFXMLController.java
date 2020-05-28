package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;


// This class is the FXML controller for the entire scene (or UI).
public class SceneFXMLController implements Initializable {

    // Initialize all UI elements

    @FXML
    private CheckBox infiniteCheckBox;

    @FXML
    private Label timeskipLabel;

    @FXML
    private TextField timeskipField;

    @FXML
    private Tooltip timeskipTooltip;

    private TextFieldWrapper timeskipWrapper;

    @FXML
    private Label simSpeedLabel;

    @FXML
    private TextField simSpeedField;

    @FXML
    private Tooltip simSpeedTooltip;

    private TextFieldWrapper simSpeedWrapper;

    @FXML
    private CheckBox trailCheckBox;

    @FXML
    private CheckBox centerCheckBox;

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
    private final Color[] defaultColors = {Color.RED, Color.BLUE, Color.GREEN};

    // Default limits
    private final double MAX_ABS_TIMESKIP = 10000;
    private final double MIN_SIMULATION_SPEED = 0.01;
    private final double MAX_SIMULATION_SPEED = 100;

    // Empty constructor for use by FXML.
    public SceneFXMLController() { }

    // Called by FXMLLoader in MainApp class, to finalize setup. Inputs are necessary because of parent class.
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Setup controller arrays.
        infoControllers = new InfoFXMLController[]{object1InfoController, object2InfoController, object3InfoController};
        parameterControllers = new ParameterFXMLController[]{object1ParameterController, object2ParameterController, object3ParameterController};

        // Setup each controller in each array with the correct id.
        for (int i = 0; i < 3; i++) {
            infoControllers[i].setup((byte) (i + 1));
            parameterControllers[i].setup((byte) (i + 1), defaultColors[i]);
        }

        // Wrap text fields and related tooltips, along with limits, into one object.
        timeskipWrapper = new TextFieldWrapper(timeskipField, timeskipTooltip, -MAX_ABS_TIMESKIP, MAX_ABS_TIMESKIP);
        simSpeedWrapper = new TextFieldWrapper(simSpeedField, simSpeedTooltip, MIN_SIMULATION_SPEED, MAX_SIMULATION_SPEED);

        onChangeInfiniteCheckbox();
    }

    public void onChangeInfiniteCheckbox() {
        if (infiniteCheckBox.isSelected()) {
            timeskipLabel.setTextFill(Color.LIGHTGRAY);
            timeskipWrapper.changeState(false);
            simSpeedLabel.setTextFill(Color.BLACK);
            simSpeedWrapper.changeState(true);
        } else {
            timeskipLabel.setTextFill(Color.BLACK);
            timeskipWrapper.changeState(true);
            simSpeedLabel.setTextFill(Color.LIGHTGRAY);
            simSpeedWrapper.changeState(false);
        }
    }

    public boolean executeValidityCheck() {
        // Check if all objects and parameters are ready.
        // RETURNS:
        // boolean, true if ready, false if not ready.

        if (!timeskipWrapper.isReady()) {
            simInputError();
            return false;
        }
        if (!simSpeedWrapper.isReady()) {
            simInputError();
            return false;
        }

        for (ParameterFXMLController controller : parameterControllers) {
            if (!controller.isReady()) {
                simInputError();
                return false;
            }
        }
        return true;
    }

    public void runSimulation() {
        if (!executeValidityCheck()) {
            return;
        }

        Particle[] particles = new Particle[3];
        for (int i = 0; i < 3; i++) {
            particles[i] = parameterControllers[i].convertToParticle();
        }


    }

    private void simInputError() {
        // TODO: Write this method
    }
}