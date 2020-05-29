package stl.threebodysimulation;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;

// The controller for the canvas with graphics.
public class CanvasPanelFXMLController {
    // UI element declarations
    @FXML
    private Canvas canvas;

    @FXML
    private Button pauseButton;

    @FXML
    private Button stopButton;

    private SimulationState state;

    public Listener onStopListener;

    public Particle[] particles;

    // Blank Constructor for FXML
    public CanvasPanelFXMLController() {
    }

    // Setup method that is called from scenecontroller
    public void setup() {
        state = SimulationState.NOT_STARTED;
        pauseButton.setDisable(true);
        stopButton.setDisable(true);
    }

    public void runSimulation(SimulationSettings settings) {
        particles = settings.particles;
        // TODO: run simulation
        state = SimulationState.RUNNING;
        stopButton.setDisable(false);
        pauseButton.setDisable(false);
    }

    // Method called when stop button is pressed
    public void stopPressed() {
        // TODO: Finish implementing method
        state = SimulationState.FINISHED;
        pauseButton.setDisable(true);
        stopButton.setDisable(true);
        onStopListener.onEvent();
    }

    // Method called when pause button is pressed
    public void pausePressed() {
        // TODO: Finish implementing method
        switch (state) {
            case PAUSED:
                state = SimulationState.RUNNING;
                pauseButton.setText("Pause");
                break;
                // Resume
            case RUNNING:
                state = SimulationState.PAUSED;
                pauseButton.setText("Unpause");
                break;
                // Pause
        }
    }
}
