package stl.threebodysimulation;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;

/**
 * The controller for the setting panel UI layout.
 */
public class SettingsPanelFXMLController {

    /**
     * The maximum timeskip allowed, in seconds, in both positive and negative directions.
     */
    private static final double MAX_ABS_TIMESKIP = 10000000;

    /**
     * The slowest simulation speed allowed.
     */
    private static final double MIN_SIMULATION_SPEED = 0.0001;

    /**
     * The fastest simulation speed allowed.
     */
    private static final double MAX_SIMULATION_SPEED = 10000;

    /**
     * The listener that is called when the manual button is pressed.
     */
    private Listener onOpenManualListener;

    /**
     * The listener that is called when the run simulation button is pressed.
     */
    private Listener onRunSimulationListener;

    /**
     * The listener that is called when an error occurs with inputs.
     */
    private Listener onRunErrorListener;

    /**
     * The CheckBox UI element for running infinitely.
     */
    @FXML
    private CheckBox infiniteCheckBox;

    /**
     * The TextField UI element for timeskips.
     */
    @FXML
    private TextField timeskipField;

    /**
     * The Tooltip UI element for timeskip hints.
     */
    @FXML
    private Tooltip timeskipTooltip;

    /**
     * The TextWrapper that the timeskip elements are wrapped in.
     */
    private LimitedTextFieldWrapper timeskipWrapper;

    /**
     * The Label UI element for the simulation speed.
     */
    @FXML
    private Label simSpeedLabel;

    /**
     * The TextField UI element for the simulation speed.
     */
    @FXML
    private TextField simSpeedField;

    /**
     * The Tooltip UI element for simulation speed hints.
     */
    @FXML
    private Tooltip simSpeedTooltip;

    /**
     * The TextWrapper that the simulation speed elements are wrapped in.
     */
    private LimitedTextFieldWrapper simSpeedWrapper;

    /**
     * The CheckBox UI element that records whether trails should be shown.
     */
    @FXML
    private CheckBox trailCheckBox;

    /**
     * The CheckBox UI element that records whether the center of mass should be shown.
     */
    @FXML
    private CheckBox centerCheckBox;

    /**
     * The ChoiceBox UI element where users choose a number format.
     */
    @FXML
    private ChoiceBox<NumberFormat> numberFormatBox;

    /**
     * The Button UI element that is clicked to run a simulation.
     */
    @FXML
    private Button runButton;

    /**
     * The CheckBox UI element that enables saving CSVs.
     */
    @FXML
    private CheckBox saveCSVCheckBox;

    /**
     * The Label UI element that is greyed out if saving CSVs is disabled.
     */
    @FXML
    private Label CSVIDLabel;

    /**
     * The TextField UI element that is filled in with the CSV file name.
     */
    @FXML
    private TextField CSVIDField;

    /**
     * The Tooltip UI element that is attached to the CSVIDField.
     */
    @FXML
    private Tooltip CSVIDFieldTooltip;

    /**
     * A wrapper that manages CSVIDField.
     */
    private TextFieldWrapper CSVIDWrapper;

    /**
     * Whether we should overwrite an existing CSV file.
     */
    private boolean forceCSV;

    /**
     * The TextField UI element that holds a template ID for saving.
     */
    @FXML
    private TextField templateIDField;

    /**
     * The Tooltip UI element that is attached to the templateIDField.
     */
    @FXML
    private Tooltip templateIDTooltip;

    /**
     * A wrapper that manages templateIDField.
     */
    private TextFieldWrapper templateIDFieldWrapper;

    /**
     * The Button UI element that is clicked to save a template.
     */
    @FXML
    private Button saveButton;

    /**
     * The VBox UI element that the entire settings panel fits into.
     */
    @FXML
    private VBox settingsBox;

    /**
     * An array that holds all controllers for object settings UIs.
     */
    private ParameterFXMLController[] parameterControllers;

    /**
     * Constructor called by the FXML loader.
     */
    public SettingsPanelFXMLController() {
    }

    /**
     * Sets up the initial state of the settings panel.
     */
    void setup() {
        // Setup controller arrays.
        parameterControllers = new ParameterFXMLController[3];
        try {
            // Setup each controller in each array with the correct id.
            for (int id = 0; id < 3; id++) {
                FXMLLoader parameterSettingsLoader = new FXMLLoader(getClass().getResource("/stl/threebodysimulation/layouts/particleParametersLayout.fxml"));
                settingsBox.getChildren().add(4 + 2 * id, parameterSettingsLoader.load());  // Slot in settings at right place in the panel. Can throw IOException if FXML file doesn't exist.
                parameterControllers[id] = parameterSettingsLoader.getController();
                parameterControllers[id].setup(id + 1, SceneFXMLController.getDefaultColors()[id]);
            }
        } catch (IOException ignored) {
            // This should never happen.
            return;
        }

        // Default: no overwriting CSVs!
        forceCSV = false;

        // Default value of timeskip.
        timeskipField.setText("0");

        // Wrap text fields and related tooltips, along with limits, into one object.
        timeskipWrapper = new LimitedTextFieldWrapper(timeskipField, timeskipTooltip, -MAX_ABS_TIMESKIP, MAX_ABS_TIMESKIP, true);
        simSpeedWrapper = new LimitedTextFieldWrapper(simSpeedField, simSpeedTooltip, MIN_SIMULATION_SPEED, MAX_SIMULATION_SPEED, true, simSpeedLabel);

        // Change UI elements based on default state of checkbox
        infiniteToggle();

        // Sets up number formats and default format.
        numberFormatBox.setItems(FXCollections.observableArrayList(NumberFormat.values()));
        numberFormatBox.setValue(NumberFormat.ADAPTIVE);

        CSVIDWrapper = new TextFieldWrapper(CSVIDField, CSVIDFieldTooltip, "CSV Filename", CSVIDLabel);
        templateIDFieldWrapper = new TextFieldWrapper(templateIDField, templateIDTooltip, "Template Filename");

        saveCSVToggle();
    }

    /**
     * Changes the state of various UI elements depending on whether the "run infinitely" checkbox is ticked or not.
     */
    public void infiniteToggle() {
        if (infiniteCheckBox.isSelected()) {
            simSpeedWrapper.changeState(true);
            trailCheckBox.setDisable(false);
        } else {
            simSpeedWrapper.changeState(false);
            trailCheckBox.setSelected(false);
            trailCheckBox.setDisable(true);
        }
    }

    /**
     * Sets the listener for the manual button.
     *
     * @param listener The listener that will be called when the manual button is pressed.
     */
    void setOnOpenManualListener(Listener listener) {
        onOpenManualListener = listener;
    }

    /**
     * Sets the listener for the simulation button.
     *
     * @param listener The listener that will be called when the simulation button is pressed.
     */
    void setOnRunSimulationListener(Listener listener) {
        onRunSimulationListener = listener;
    }

    /**
     * Sets the listener for when errors occur.
     *
     * @param listener The listener that will be called when an error happens.
     */
    void setOnRunErrorListener(Listener listener) {
        onRunErrorListener = listener;
    }

    /**
     * Checks if all object and parameter settings are ready.
     *
     * @return True if ready, False if not ready.
     */
    private boolean executeValidityCheck() {
        boolean readiness = true;

        // All wrappers must be called, so that they have an opportunity to highlight red.
        if (!timeskipWrapper.isReady()) {
            readiness = false;
        }
        if (!simSpeedWrapper.isReady()) {
            readiness = false;
        }
        if (!CSVIDWrapper.isReady()) {
            readiness = false;
        }

        for (ParameterFXMLController controller : parameterControllers) {
            if (!controller.isReady()) {
                readiness = false;
            }
        }
        if (!readiness) {
            onRunErrorListener.onEvent();
            return false;
        }

        if (!forceCSV) {
            if (CSVFileConflict()) {
                return false;
            }
        }

        // Reset forceCSV.
        forceCSV = false;

        return true;
    }

    /**
     * Checks if the CSV file the user wants to save should be overwritten.
     *
     * @return true if the user chooses not to overwrite the file, false if no conflict exists or if the user wants to overwrite the file.
     */
    private boolean CSVFileConflict() {
        // Check if the user wants to save CSV files in the first place
        if (!saveCSVCheckBox.isSelected()) {
            return false; // No conflict exists since the CSV file won't be written anyways
        }

        File CSVFile = new File("CSV" + SceneFXMLController.fileSeparator + CSVIDField.getText() + ".csv");

        if (!CSVFile.exists()) {
            return false; // No conflict exists since no CSV file already exists with the same name.
        }

        SceneFXMLController.openWarningWindow(
                "This simulation will overwrite an existing CSV file. Are you sure you want to proceed?",
                settingsBox.getScene().getWindow(),
                () -> {
                    forceCSV = true;
                    runSimulation();
                });

        return true;
    }

    /**
     * Runs the simulation, if the settings are valid. Links to FXML.
     */
    public void runSimulation() {
        // TODO: Finish this method.
        if (!executeValidityCheck()) {
            return;
        }
        onRunSimulationListener.onEvent();
    }

    /**
     * Opens the manual. Links to FXML.
     */
    public void openManual() {
        onOpenManualListener.onEvent();
    }

    /**
     * Packages the inputs in the settings panel into a SimulationSettings object.
     *
     * @return The SimulationSettings that the simulation will run with.
     */
    SimulationSettings getSimulationSettings() {
        // Package each object's settings UI into a particle object.
        Particle[] particles = new Particle[3];
        for (int i = 0; i < 3; i++) {
            particles[i] = parameterControllers[i].convertToParticle();
        }

        // Track settings down.
        boolean infiniteEnabled = infiniteCheckBox.isSelected();
        boolean trailsEnabled = trailCheckBox.isSelected();
        boolean centerOfGravityEnabled = centerCheckBox.isSelected();
        double skip;
        try {
            skip = Double.parseDouble(timeskipField.getText());
        } catch (NumberFormatException ignored) { // Should never happen.
            skip = 0;
        }
        double speed;
        try {
            speed = Double.parseDouble(simSpeedField.getText());
        } catch (NumberFormatException ignored) { // Should never happen.
            speed = 0;
        }
        String CSVFileName;
        if (saveCSVCheckBox.isSelected()) {
            CSVFileName = CSVIDField.getText();
        } else {
            CSVFileName = "";
        }

        // Disable run button if the simulation is going to continuously run.
        if (infiniteEnabled) {
            runButton.setDisable(true);
        }

        return new SimulationSettings(particles, infiniteEnabled, trailsEnabled, centerOfGravityEnabled, skip, speed, numberFormatBox.getValue(), CSVFileName);
    }

    /**
     * Changes UI elements based on if CSV toggle is enabled.
     */
    public void saveCSVToggle() {
        CSVIDWrapper.changeState(saveCSVCheckBox.isSelected());
    }

    /**
     * Enables the run button.
     */
    void enableRunButton() {
        runButton.setDisable(false);
    }
}
