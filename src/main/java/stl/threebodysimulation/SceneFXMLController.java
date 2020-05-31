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

import java.net.URL;
import java.util.ResourceBundle;


// This class is the FXML controller for the entire scene (or UI).
public class SceneFXMLController implements Initializable {

    public static final Color[] defaultColors = {Color.RED, Color.BLUE, Color.GREEN};
    public Particle[] particles;
    // Initialize controllers for custom UI elements
    @FXML
    private InfoPanelFXMLController infoPanelController;
    @FXML
    private SettingsPanelFXMLController settingsPanelController;
    @FXML
    private CanvasPanelFXMLController canvasPanelController;
    @FXML
    private BorderPane window;

    // Empty constructor for use by FXML.
    public SceneFXMLController() {
    }

    // Called by FXMLLoader in MainApp class, to finalize setup. Inputs are necessary because of parent class.
    public void initialize(URL url, ResourceBundle resourceBundle) {
        infoPanelController.setup();
        settingsPanelController.setup();
        settingsPanelController.onOpenManualListener = new Listener() {
            @Override
            public void onEvent() {
                openManual();
            }
        };
        settingsPanelController.onRunSimulationListener = new Listener() {
            @Override
            public void onEvent() {
                runSimulation(settingsPanelController.getSimulationSettings());
            }
        };
        settingsPanelController.onRunErrorListener = new Listener() {
            @Override
            public void onEvent() {
                openPopupWindow("Simulation Error", "The simulation cannot be run, because some parameters are not valid. Please input valid numbers, and then try again.", new Image("/errorIcon.png"));
            }
        };
        canvasPanelController.setup();
        canvasPanelController.onStopListener = new Listener() {
            @Override
            public void onEvent() {
                // TODO: What happens when we stop.
                settingsPanelController.setDisabledRunButton(false);
            }
        };
    }

    // Method that opens a popup window with a message and icon.
    public void openPopupWindow(String title, String message, Image icon) {
        try {
            // Makes an FXML Loader and loads the fxml files
            FXMLLoader windowLoader = new FXMLLoader(getClass().getResource("/popupWindow.fxml"));
            Parent root = windowLoader.load();

            // Load the correct message into the layout
            PopupWindowFXMLController errorController = windowLoader.getController();
            errorController.setLabel(message);

            // Style the scenes
            Scene errorScene = new Scene(root);
            errorScene.getStylesheets().add(getClass().getResource("/bootstrap3.css").toExternalForm());

            // Make a popup window that blocks the main screen, and set icons and titles.
            final Stage errorWindow = new Stage();
            errorWindow.initModality(Modality.APPLICATION_MODAL);
            errorWindow.initOwner(window.getScene().getWindow());
            errorWindow.setResizable(false);
            errorWindow.getIcons().add(icon);
            errorWindow.setTitle(title);
            errorWindow.setScene(errorScene);
            errorWindow.show();

        } catch (Exception ignored) {}
    }

    public void openManual() {
        // Opens user manual popups
        try {
            final Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/userManual.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/bootstrap3.css").toExternalForm());
            // We don't want the window to be resizable, to save us the UI headache.
            stage.setResizable(false);

            // Icon of app
            stage.getIcons().add(new Image("/appIcon.png"));

            // Title of app
            stage.setTitle("User Manual");

            // Stage the UI.
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception ignored){
        }
    }

    public void runSimulation(SimulationSettings settings) {
        // Runs the simulation according to the given settings
        particles = settings.particles;
        infoPanelController.setParticles(particles);
        infoPanelController.setNumberFormat(settings.numberFormat);
        canvasPanelController.setParticles(particles);
        canvasPanelController.runSimulation(settings);
    }

    public static double[] centerOfMass(Particle[] particles) {
        // Returns the center of mass coordinates for an array of particles
        // INPUTS:
        // particles: Particle[], the array of particles that the center of mass is calculated for.
        // RETURNS:
        // centerOfMass: double[], the x and y coordinates of the center of mass.
        double totalMass = 0;
        double[] centerOfMass = new double[2];
        for (Particle particle : particles) {
            totalMass += particle.mass;
            centerOfMass[0] += particle.position[0] * particle.mass;
            centerOfMass[1] += particle.position[1] * particle.mass;
        }
        centerOfMass[0] /= totalMass;
        centerOfMass[1] /= totalMass;
        return centerOfMass;
    }
}