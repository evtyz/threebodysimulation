package stl.threebodysimulation;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


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
     * The listener that is called when a new template is saved.
     */
    private Listener onSaveTemplateListener;

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
     * Whether we should overwrite an existing template.
     */
    private boolean forceTemplateSave;

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
     * The VBox UI element that the entire settings panel fits into.
     */
    @FXML
    private VBox settingsBox;

    /**
     * The button UI element for saving templates.
     */
    @FXML
    private Button saveButton;

    /**
     * The button UI element for browsing CSVs.
     */
    @FXML
    private Button browseButton;

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

        // Default: no overwriting!
        forceCSV = false;
        forceTemplateSave = false;

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

        FontIcon saveIcon = new FontIcon(Material.SAVE);
        saveIcon.setIconColor(Color.valueOf("#555555"));
        saveIcon.setIconSize(20);
        saveButton.setGraphic(saveIcon);

        FontIcon browseIcon = new FontIcon(Material.FOLDER);
        browseIcon.setIconColor(Color.valueOf("#555555"));
        browseIcon.setIconSize(17);
        browseButton.setGraphic(browseIcon);
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
     * @param forSimulation True if the validation check is for simulation purposes, false otherwise.
     * @return True if ready, False if not ready.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted") // Inverted for clarity's sake.
    private boolean executeValidityCheck(boolean forSimulation) {
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

        if (forSimulation) {
            if (!readiness) {
                onRunErrorListener.onEvent();
                return false;
            }
            if (CSVFileConflict()) {
                return false;
            }
            forceCSV = false; // Reset forceCSV.
            return true;
        }

        return readiness;
    }

    /**
     * Checks if the CSV file the user wants to save should be overwritten.
     *
     * @return true if there is a file conflict, false if no conflict exists.
     */
    private boolean CSVFileConflict() {
        if (forceCSV) {
            return false;
        }

        // Check if the user wants to save CSV files in the first place
        if (!saveCSVCheckBox.isSelected()) {
            return false; // No conflict exists since the CSV file won't be written anyways
        }

        String filepath = "CSV" + SceneFXMLController.fileSeparator + CSVIDField.getText() + ".csv";

        File CSVFile = new File(filepath);

        if (!CSVFile.exists()) {
            return false; // No conflict exists since no CSV file already exists with the same name.
        }

        // If the user presses yes, we will restart the runSimulation method from the start (with forceCSV true this time).
        SceneFXMLController.openWarningWindow(
                new ConfirmationMessage(ConfirmationMessage.Type.CSV_CONFIRMATION, CSVFile.getAbsolutePath()),
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
        if (!executeValidityCheck(true)) {
            return;
        }
        if (infiniteCheckBox.isSelected()) {
            setActiveRunButton(false);
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

        return new SimulationSettings(particles, infiniteEnabled, trailsEnabled, centerOfGravityEnabled, skip, speed, numberFormatBox.getValue(), CSVFileName);
    }

    /**
     * Changes UI elements based on if CSV toggle is enabled.
     */
    public void saveCSVToggle() {
        CSVIDWrapper.changeState(saveCSVCheckBox.isSelected());
    }

    /**
     * Enables/Disables the run button.
     *
     * @param state The state the run button is changed to. True if enabled, false if disabled.
     */
    void setActiveRunButton(boolean state) {
        runButton.setDisable(!state);
    }

    /**
     * Opens the CSV directory in a file explorer window.
     */
    public void openCSVDirectory() {
        DesktopAPI.openDirectory("CSV");
    }

    /**
     * Tries to save the current template into a .tbsettings file.
     */
    public void saveTemplate() {
        boolean readiness = true;

        if (!templateIDFieldWrapper.isReady()) {
            readiness = false;
        }
        if (!executeValidityCheck(false)) {
            readiness = false;
        }

        if (!readiness) {
            SceneFXMLController.openErrorWindow(ErrorMessage.SAVE_ERROR, settingsBox.getScene().getWindow());
            return;
        }

        SimulationSettings settings = getSimulationSettings();

        String saveDirectoryPath = "Saves";
        File saveDirectory = new File(saveDirectoryPath);
        saveDirectory.mkdir();

        String filepath = saveDirectoryPath + SceneFXMLController.fileSeparator + templateIDField.getText() + ".tbsettings";

        File saveFile = new File(filepath);

        if (forceTemplateSave || !saveFile.exists()) {
            storeTemplate(settings, filepath);
        } else {
            SceneFXMLController.openWarningWindow(
                    new ConfirmationMessage(ConfirmationMessage.Type.TEMPLATE_CONFIRMATION, saveFile.getAbsolutePath()),
                    settingsBox.getScene().getWindow(),
                    () -> {
                        forceTemplateSave = true;
                        saveTemplate();
                    });
        }

        forceTemplateSave = false;
    }

    /**
     * Tries to store a settings object into a file.
     *
     * @param settings The SimulationSettings object to be stored.
     * @param filepath The path of the file where the object will be stored.
     */
    void storeTemplate(SimulationSettings settings, String filepath) {
        ArrayList<String> serializedForm = settings.serialize();
        try {
            BufferedWriter serializedWriter = Files.newBufferedWriter(Paths.get(filepath));
            CSVPrinter serializedPrinter = new CSVPrinter(serializedWriter, CSVFormat.DEFAULT);
            serializedPrinter.printRecord(serializedForm);
            serializedWriter.close();
            onSaveTemplateListener.onEvent();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Sets the listener that is called when the user saves a template.
     *
     * @param onSaveTemplateListener The listener that will be called.
     */
    void setOnSaveTemplateListener(Listener onSaveTemplateListener) {
        this.onSaveTemplateListener = onSaveTemplateListener;
    }

    /**
     * Loads the information in a SimulationSettings object into the UI.
     *
     * @param settings The SimulationSettings to be loaded.
     */
    void loadSettings(SimulationSettings settings) {
        infiniteCheckBox.setSelected(settings.getInfinite());
        trailCheckBox.setSelected(settings.getTrails());
        centerCheckBox.setSelected(settings.getCenterOfGravity());
        timeskipWrapper.setText(String.valueOf(settings.getSkip()));
        simSpeedWrapper.setText(String.valueOf(settings.getSpeed()));
        numberFormatBox.getSelectionModel().select(settings.getNumberFormat());
        infiniteToggle();

        for (int id = 0; id < 3; id++) {
            parameterControllers[id].loadParticle(settings.getParticles()[id]);
        }

        String CSVID = settings.getCSVFileName();

        if (!CSVID.equals("")) {
            CSVIDWrapper.changeState(true);
            CSVIDWrapper.setText(CSVID);
            saveCSVCheckBox.setSelected(true);
        } else {
            saveCSVCheckBox.setSelected(false);
            saveCSVToggle();
        }
    }

    /**
     * Sets the template name to be displayed on the UI.
     *
     * @param templateName The template name.
     */
    void loadTemplateName(String templateName) {
        templateIDFieldWrapper.setText(templateName);
    }
}
