package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * This class is the FXML controller for the entire scene (or UI).
 */
public class SceneFXMLController implements Initializable {

    /**
     * Default colors that each particle is initialized to.
     */
    private static final Color[] defaultColors = {Color.RED, Color.BLUE, Color.GREEN};

    /**
     * The info display panel in the UI.
     */
    private InfoPanelFXMLController infoPanelController;

    /**
     * The settings panel in the UI.
     */
    private SettingsPanelFXMLController settingsPanelController;

    /**
     * The Tab UI element for the settings panel.
     */
    @FXML
    private Tab settingsTab;

    /**
     * The Tab UI element for the saves panel.
     */
    @FXML
    private Tab savesTab;

    /**
     * The UI element that contains everything outside of the tabs to the left.
     */
    @FXML
    private BorderPane actionPane;

    /**
     * The visualization canvas and controls in the UI.
     */
    private CanvasPanelFXMLController canvasPanelController;

    /**
     * The entire UI scene, as a single element.
     */
    @FXML
    private BorderPane sceneLayout;

    /**
     * Constructor used by the FXML loader.
     */
    public SceneFXMLController() {
    }

    /**
     * Opens a new popup window with an error message
     * @param message The error message to display.
     * @param parent The window that the error popup must block.
     */
    static void openPopupWindow(ErrorMessage message, Window parent) {
        try {
            // Makes an FXML Loader and loads the fxml files
            FXMLLoader windowLoader = new FXMLLoader(SceneFXMLController.class.getResource("/stl/threebodysimulation/layouts/popupWindowLayout.fxml"));
            Scene errorScene = new Scene(windowLoader.load());

            // Style the scenes
            errorScene.getStylesheets().add(SceneFXMLController.class.getResource("/stl/threebodysimulation/styles/bootstrap3.css").toExternalForm());

            // Load the correct message into the layout
            PopupWindowFXMLController errorController = windowLoader.getController();
            errorController.setLabel(message.getMessage());

            // Make a popup sceneLayout that blocks the main screen, and set icons and titles.
            final Stage errorWindow = new Stage();
            errorWindow.initModality(Modality.APPLICATION_MODAL); // Blocks the parent window.
            errorWindow.initOwner(parent);
            errorWindow.setResizable(false); // Not resizable
            errorWindow.getIcons().add(new Image("/stl/threebodysimulation/icons/errorIcon.png")); // Error icon.
            errorWindow.setTitle(message.getTitle());
            errorWindow.setScene(errorScene);
            errorWindow.show();

        } catch (Exception ignored) {
        }
    }

    /**
     * Sets up initial states of UI elements after the FXML loader is done linking. Called by the FXML loader.
     * @param url Unused. From parent class.
     * @param resourceBundle Unused. From parent class.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Load in settings panel
            FXMLLoader settingsPanelLoader = new FXMLLoader(getClass().getResource("/stl/threebodysimulation/layouts/settingsPanelLayout.fxml"));
            settingsTab.setContent(settingsPanelLoader.load());
            settingsPanelController = settingsPanelLoader.getController();
            settingsPanelController.setup(); // Set up settings panel.
            settingsPanelController.setOnOpenManualListener(this::openManual); // Sets up user manual button.
            settingsPanelController.setOnRunSimulationListener(() -> runSimulation(settingsPanelController.getSimulationSettings())); // Sets up what happens when simulation is run.
            settingsPanelController.setOnRunErrorListener(() -> openPopupWindow(ErrorMessage.INPUT_ERROR, sceneLayout.getScene().getWindow())); // Sets up what happens when an error occurs.

            // Load in info panel.
            FXMLLoader infoPanelLoader = new FXMLLoader(getClass().getResource("/stl/threebodysimulation/layouts/infoPanelLayout.fxml"));
            actionPane.setTop(infoPanelLoader.load());
            infoPanelController = infoPanelLoader.getController();
            infoPanelController.setup();

            // Load in canvas panel.
            FXMLLoader canvasPanelLoader = new FXMLLoader(getClass().getResource("/stl/threebodysimulation/layouts/canvasPanelLayout.fxml"));
            actionPane.setCenter(canvasPanelLoader.load());
            canvasPanelController = canvasPanelLoader.getController();
            canvasPanelController.setup(); // Sets up canvas.
            canvasPanelController.setOnStopListener(() -> settingsPanelController.enableRunButton()); // Enables rerunning the simulation if it is stopped.
        } catch (IOException ignored) {
            // This only happens when FXML files aren't found, which should never happen.
        }
    }

    /**
     * Opens the user manual.
     */
    private void openManual() {
        // Opens user manual popups
        try {
            // Load up a new window with user manual layout.
            final Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/stl/threebodysimulation/layouts/userManualLayout.fxml"));
            Scene scene = new Scene(root);
            // Enable css
            scene.getStylesheets().add(getClass().getResource("/stl/threebodysimulation/styles/bootstrap3.css").toExternalForm());
            // We don't want the sceneLayout to be resizable, to save us the UI headache.
            stage.setResizable(false);

            // Icon of app
            stage.getIcons().add(new Image("/stl/threebodysimulation/icons/appIcon.png"));

            // Title of app
            stage.setTitle("User Manual");

            // Stage the UI.
            stage.setScene(scene);
            stage.show();
        } catch (Exception ignored) { // In case the layout is not found. Should never happen.
        }
    }

    /**
     * Runs the simulation.
     * @param settings Settings to run the simulation with.
     */
    private void runSimulation(SimulationSettings settings) {
        // Runs the simulation according to the given settings
        // Sets up info display.
        Particle[] particles = settings.getParticles();
        infoPanelController.setParticles(particles);
        InfoPanelFXMLController.setNumberFormat(settings.getNumberFormat());

        // Sets up visualization.
        canvasPanelController.setParticles(particles);
        canvasPanelController.runSimulation(settings);
    }

    /**
     * Gets the default colors of particles.
     * @return Default colors of all three particles.
     */
    static Color[] getDefaultColors() {
        return defaultColors;
    }
}