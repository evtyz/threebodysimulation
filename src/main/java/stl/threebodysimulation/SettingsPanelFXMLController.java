package stl.threebodysimulation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

// This class is the controller for the settings panel UI element on the left
public class SettingsPanelFXMLController {

    // Default colors

    // Default limits
    private final double MAX_ABS_TIMESKIP = 1000000;
    private final double MIN_SIMULATION_SPEED = 0.001;
    private final double MAX_SIMULATION_SPEED = 1000;
    // Listeners
    public Listener onOpenManualListener;
    public Listener onRunSimulationListener;
    public Listener onRunErrorListener;
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
    private ChoiceBox<NumberFormat> numberFormatBox;
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

    // Default method for FXML
    public SettingsPanelFXMLController() {
    }

    // setup method, called by scenecontroller
    public void setup() {
        // Setup controller arrays.
        parameterControllers = new ParameterFXMLController[]{object1ParameterController, object2ParameterController, object3ParameterController};

        // Setup each controller in each array with the correct id.
        for (int i = 0; i < 3; i++) {
            parameterControllers[i].setup(i + 1, SceneFXMLController.defaultColors[i]);
        }

        timeskipField.setText("0");

        // Wrap text fields and related tooltips, along with limits, into one object.
        timeskipWrapper = new TextFieldWrapper(timeskipField, timeskipTooltip, -MAX_ABS_TIMESKIP, MAX_ABS_TIMESKIP, true);
        simSpeedWrapper = new TextFieldWrapper(simSpeedField, simSpeedTooltip, MIN_SIMULATION_SPEED, MAX_SIMULATION_SPEED, true);

        // Change UI elements based on default state of checkbox
        onChangeInfiniteCheckbox();

        numberFormatBox.setItems(FXCollections.observableArrayList(NumberFormat.values()));
        numberFormatBox.setValue(NumberFormat.SCIENTIFIC_2);
    }

    // This method handles the state of various UI elements depending on whether the "run infinitely" checkbox is ticked or not.
    public void onChangeInfiniteCheckbox() { // TODO: Add support for center of gravity in non-infinite simulations
        if (infiniteCheckBox.isSelected()) {
            simSpeedLabel.setTextFill(Color.BLACK);
            simSpeedWrapper.changeState(true);
            trailCheckBox.setDisable(false);
            centerCheckBox.setDisable(false);
        } else {
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
        // readiness: boolean, true if ready, false if not ready.

        boolean readiness = true;

        // All wrappers must be called, so that they have an opportunity to highlight red.

        if (!timeskipWrapper.isReady()) {
            readiness = false;
        }
        if (!simSpeedWrapper.isReady()) {
            readiness = false;
        }

        for (ParameterFXMLController controller : parameterControllers) {
            if (!controller.isReady()) {
                readiness = false;
            }
        }
        if (!readiness) {
            onRunErrorListener.onEvent();
        }

        return readiness;
    }

    // Called when the run simulation button is pressed.
    public void runSimulation() {
        // TODO: Finish this method.
        if (!executeValidityCheck()) {
            return;
        }
        onRunSimulationListener.onEvent();
    }

    public void openManual() {
        // Called when the user presses the View User Manual button.
        try {
            onOpenManualListener.onEvent();
        } catch (Exception ignored) {
        }
    }

    public SimulationSettings getSimulationSettings() {
        // A class that packages the inputs in the settingspanel into a SimulationSettings object.
        // RETURNS:
        // new SimulationSettings: a SimulationSettings object that represents settings for the simulation.
        Particle[] particles = new Particle[3];
        for (int i = 0; i < 3; i++) {
            particles[i] = parameterControllers[i].convertToParticle();
        }

        boolean infiniteEnabled = infiniteCheckBox.isSelected();
        boolean trailsEnabled = trailCheckBox.isSelected();
        boolean centerOfGravityEnabled = centerCheckBox.isSelected();
        double skip;
        try {
            skip = Double.parseDouble(timeskipField.getText());
        } catch (NumberFormatException ignored) {
            skip = 0;
        }
        double speed;
        try {
            speed = Double.parseDouble(simSpeedField.getText());
        } catch (NumberFormatException ignored) {
            speed = 0;
        }

        if (infiniteEnabled) {
            runButton.setDisable(true);
        }

        return new SimulationSettings(particles, infiniteEnabled, trailsEnabled, centerOfGravityEnabled, skip, speed, numberFormatBox.getValue());
    }

    public void setDisabledRunButton(boolean state) {
        runButton.setDisable(state);
    }
}
