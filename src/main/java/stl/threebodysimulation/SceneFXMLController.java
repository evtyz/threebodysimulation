package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;


// This class is the FXML controller for the entire scene (or UI).
public class SceneFXMLController implements Initializable {

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
        // TODO: Implement method.
    }

    public void runSimulation(SimulationSettings settings) {
        // Runs the simulation according to the given settings
        // TODO: Implement method.

        ParticleDiffEq particleDiffEq = new ParticleDiffEq(settings.returnMass());

        canvasPanelController.runSimulation();
    }
}