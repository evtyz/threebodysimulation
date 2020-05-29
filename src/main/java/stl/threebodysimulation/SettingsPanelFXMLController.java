package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

// This class is the controller for the settings panel UI element on the left
public class SettingsPanelFXMLController {

    // Default colors
    private final Color[] defaultColors = {Color.RED, Color.BLUE, Color.GREEN};
    // Default limits
    private final double MAX_ABS_TIMESKIP = 10000;
    private final double MIN_SIMULATION_SPEED = 0.01;
    private final double MAX_SIMULATION_SPEED = 100;
    // UI element declarations
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
    @FXML
    private ParameterFXMLController object1ParameterController;
    @FXML
    private ParameterFXMLController object2ParameterController;
    @FXML
    private ParameterFXMLController object3ParameterController;
    private ParameterFXMLController[] parameterControllers;

    public SettingsPanelFXMLController() {
    }

    // setup method, called by scenecontroller
    public void setup() {
        // Setup controller arrays.
        parameterControllers = new ParameterFXMLController[]{object1ParameterController, object2ParameterController, object3ParameterController};

        // Setup each controller in each array with the correct id.
        for (int i = 0; i < 3; i++) {
            parameterControllers[i].setup((byte) (i + 1), defaultColors[i]);
        }

        // Wrap text fields and related tooltips, along with limits, into one object.
        timeskipWrapper = new TextFieldWrapper(timeskipField, timeskipTooltip, -MAX_ABS_TIMESKIP, MAX_ABS_TIMESKIP);
        simSpeedWrapper = new TextFieldWrapper(simSpeedField, simSpeedTooltip, MIN_SIMULATION_SPEED, MAX_SIMULATION_SPEED);

        // Change UI elements based on default state of checkbox
        onChangeInfiniteCheckbox();
    }

    // This method handles the state of various UI elements depending on whether the "run infinitely" checkbox is ticked or not.
    public void onChangeInfiniteCheckbox() {
        if (infiniteCheckBox.isSelected()) {
            timeskipLabel.setTextFill(Color.LIGHTGRAY);
            timeskipWrapper.changeState(false);
            simSpeedLabel.setTextFill(Color.BLACK);
            simSpeedWrapper.changeState(true);
            trailCheckBox.setDisable(false);
            centerCheckBox.setDisable(false);
        } else {
            timeskipLabel.setTextFill(Color.BLACK);
            timeskipWrapper.changeState(true);
            simSpeedLabel.setTextFill(Color.LIGHTGRAY);
            simSpeedWrapper.changeState(false);
            trailCheckBox.setSelected(false);
            trailCheckBox.setDisable(true);
            centerCheckBox.setSelected(false);
            centerCheckBox.setDisable(true);
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

    // Called when the run simulation button is pressed.
    public void runSimulation() {
        // TODO: Finish this method.
        if (!executeValidityCheck()) {
            return;
        }

        Particle[] particles = new Particle[3];
        for (int i = 0; i < 3; i++) {
            particles[i] = parameterControllers[i].convertToParticle();
        }


    }

    private void simInputError() {
        // Handles errors when the user presses "Run Simulation" but doesn't fill everything correctly.
        // TODO: Write this method
    }

    public void openManual() {
        // Called when the user presses the View User Manual button.
        // TODO: Write this method
    }
}
