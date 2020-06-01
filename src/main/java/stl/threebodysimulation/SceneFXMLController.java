package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.ResourceBundle;


// This class is the FXML controller for the entire scene (or UI).
public class SceneFXMLController implements Initializable {

    // Default colors
    private static final Color[] defaultColors = {Color.RED, Color.BLUE, Color.GREEN};

    private static Window parentWindow;

    private Particle[] particles;
    // Initialize controllers for custom UI elements
    @FXML
    private InfoPanelFXMLController infoPanelController;
    @FXML
    private SettingsPanelFXMLController settingsPanelController;
    @FXML
    private CanvasPanelFXMLController canvasPanelController;
    @FXML
    private BorderPane sceneLayout;

    // Empty constructor for use by FXML.
    public SceneFXMLController() {
    }

    // Method that opens a popup sceneLayout with a message and icon.
    static void openPopupWindow(ErrorMessage message, Window parent) {
        try {
            // Makes an FXML Loader and loads the fxml files
            FXMLLoader windowLoader = new FXMLLoader(SceneFXMLController.class.getResource("/popupWindow.fxml"));
            Parent root = windowLoader.load();

            // Load the correct message into the layout
            PopupWindowFXMLController errorController = windowLoader.getController();
            errorController.setLabel(message.getMessage());

            // Style the scenes
            Scene errorScene = new Scene(root);
            errorScene.getStylesheets().add(SceneFXMLController.class.getResource("/bootstrap3.css").toExternalForm());

            // Make a popup sceneLayout that blocks the main screen, and set icons and titles.
            final Stage errorWindow = new Stage();
            errorWindow.initModality(Modality.APPLICATION_MODAL);
            errorWindow.initOwner(parent);
            errorWindow.setResizable(false);
            errorWindow.getIcons().add(new Image("/errorIcon.png"));
            errorWindow.setTitle(message.getTitle());
            errorWindow.setScene(errorScene);
            errorWindow.show();

        } catch (Exception ignored) {
        }
    }

    // Called by FXMLLoader in MainApp class, to finalize setup. Inputs are necessary because of parent class.
    public void initialize(URL url, ResourceBundle resourceBundle) {
        infoPanelController.setup();
        settingsPanelController.setup();
        settingsPanelController.setOnOpenManualListener(this::openManual);
        settingsPanelController.setOnRunSimulationListener(() -> runSimulation(settingsPanelController.getSimulationSettings()));
        settingsPanelController.setOnRunErrorListener(() -> openPopupWindow(ErrorMessage.INPUT_ERROR, sceneLayout.getScene().getWindow()));
        canvasPanelController.setup();
        canvasPanelController.setOnStopListener(() -> settingsPanelController.setDisabledRunButton());
    }

    private void openManual() {
        // Opens user manual popups
        try {
            final Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/userManual.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/bootstrap3.css").toExternalForm());
            // We don't want the sceneLayout to be resizable, to save us the UI headache.
            stage.setResizable(false);

            // Icon of app
            stage.getIcons().add(new Image("/appIcon.png"));

            // Title of app
            stage.setTitle("User Manual");

            // Stage the UI.
            stage.setScene(scene);
            stage.show();
        } catch (Exception ignored) {
        }
    }

    private void runSimulation(SimulationSettings settings) {
        // Runs the simulation according to the given settings
        particles = settings.getParticles();
        infoPanelController.setParticles(particles);
        InfoPanelFXMLController.setNumberFormat(settings.getNumberFormat());
        canvasPanelController.setParticles(particles);
        canvasPanelController.runSimulation(settings);
    }

    static Color[] getDefaultColors() {
        return defaultColors;
    }
}