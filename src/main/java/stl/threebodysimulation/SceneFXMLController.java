package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


// This class is the FXML controller for the entire scene (or UI).
public class SceneFXMLController implements Initializable {

    public Particle[] particles;
    // Initialize controllers for custom UI elements
    @FXML
    private InfoPanelFXMLController infoPanelController;
    @FXML
    private SettingsPanelFXMLController settingsPanelController;
    @FXML
    private CanvasPanelFXMLController canvasPanelController;

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
        canvasPanelController.setup();
        canvasPanelController.onStopListener = new Listener() {
            @Override
            public void onEvent() {
                // TODO: What happens when we stop.
            }
        };
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
            stage.getIcons().add(new Image("/icon.png"));

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
        canvasPanelController.setParticles(particles);
        infoPanelController.setParticles(particles);
        canvasPanelController.runSimulation(settings);
    }
}