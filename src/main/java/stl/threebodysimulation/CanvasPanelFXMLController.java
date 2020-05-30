package stl.threebodysimulation;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;

import java.util.Arrays;

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

    public double[] flattenedParticles = new double[12];

    private double currentTime;

    private Task simulation;

    // Blank Constructor for FXML
    public CanvasPanelFXMLController() { }

    public void setParticles(Particle[] particles) {
        this.particles = particles;
    }

    // Setup method that is called from scenecontroller
    public void setup() {
        state = SimulationState.NOT_STARTED;
        pauseButton.setDisable(true);
        stopButton.setDisable(true);
        currentTime = 0;
    }

    public void runSimulation(SimulationSettings settings) {
        // TODO: FIX BUG
        // TODO: run simulation
        ParticleDiffEq particleDiffEq = new ParticleDiffEq(settings.returnMass());

        // TODO: Find reasonable values for parameters here
        DormandPrince853Integrator integrator = new DormandPrince853Integrator(0.01, 30000, 0.01, 0.01);
        flattenParticles();

        if (settings.isInfinite) {
            state = SimulationState.RUNNING;
            stopButton.setDisable(false);
            pauseButton.setDisable(false);

            simulation  = new Task() {
                @Override
                protected Object call() throws Exception {
                    while (state == SimulationState.RUNNING) {
                        integrator.integrate(particleDiffEq, currentTime, flattenedParticles, currentTime + settings.speed, flattenedParticles);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                updateParticlesAndCanvas();
                            }
                        });
                        currentTime += settings.speed;
                        Thread.sleep(1000);
                    }
                    return null;
                }
            };
            startSimulation();
        } else {
            state = SimulationState.FINISHED;
            integrator.integrate(particleDiffEq, 0, flattenedParticles, settings.skip, flattenedParticles);
            updateParticlesAndCanvas();
        }
    }

    public void updateParticlesAndCanvas() {
        unflattenParticles();
        updateCanvas();
    }

    public void startSimulation() {
        Thread simulationThread = new Thread(simulation);
        simulationThread.setDaemon(true);
        simulationThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println(e.getMessage());
            }
        });
        simulationThread.start();
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

    public void flattenParticles() {
        int index = 0;
        for (int i = 0; i < 3; i++) {
            double[] flattenedParticle = particles[i].flatten();
            for (double property : flattenedParticle) {
                flattenedParticles[index++] = property;
            }
        }
    }

    public void unflattenParticles() {
        for (int i = 0; i < 3; i++) {
            particles[i].updateFromFlattenedParticle(Arrays.copyOfRange(flattenedParticles, 4 * i, 4 * i + 4));
            particles[i].updateAcceleration();
        }
    }

    private void clearCanvas() {
        // TODO
    }

    private void updateCanvas() {
        // TODO
    }
}
