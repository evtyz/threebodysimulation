package stl.threebodysimulation;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;

import java.util.Arrays;

// TODO: Documentation

// The controller for the canvas with graphics.
public class CanvasPanelFXMLController {

    // Listener called when stop button is pressed
    private Listener onStopListener;

    // Particles managed by our canvas.
    private Particle[] particles;

    // Particles in flattened form, where we can manipulate them using math library.
    private double[] flattenedParticles = new double[12];

    // UI element declarations
    @FXML
    private Canvas canvas;
    private CanvasWrapper canvasWrapper;
    @FXML
    private Button pauseButton;
    @FXML
    private Button stopButton;
    @FXML
    private Label timeLabel;

    // The state of the simulation (Not started, running, paused, finished)
    private SimulationState state;

    // Object used for synchronization between threads
    private final static Object synchronizationObject = new Object();

    // Some handy simulation variables
    private double currentTime;
    private double speed;

    private static final int MAX_FRAMERATE = 100;
    private static final long FRAMETIME = 1000 / MAX_FRAMERATE;

    // Math variables
    private ParticleDiffEq particleDiffEq;
    private DormandPrince853Integrator integrator;

    // Blank Constructor for FXML
    public CanvasPanelFXMLController() {
    }

    void setOnStopListener(Listener listener) {
        onStopListener = listener;
    }

    // Sets up particles according to the given array
    void setParticles(Particle[] particles) {
        this.particles = particles;
        canvasWrapper.particles = particles;
    }

    // Setup method that is called from scene controller
    void setup() {
        state = SimulationState.INACTIVE;
        pauseButton.setDisable(true);
        stopButton.setDisable(true);
        currentTime = 0;
        canvasWrapper = new CanvasWrapper(canvas);
    }

    private void breakSimulation(ErrorMessage errorMessage) {
        updateAll();
        stopPressed();
        SceneFXMLController.openPopupWindow(errorMessage, canvas.getScene().getWindow());
    }

    // This method is called when we want to start running a simulation.
    void runSimulation(SimulationSettings settings) {
        // INPUTS:
        // settings: SimulationSettings, the settings that we are simulating with.
        // Runs the simulation according to these settings.

        canvasWrapper.clearCanvas();
        canvasWrapper.setSettings(settings);

        // Set up the particle differential equation according to the masses of each particle.
        particleDiffEq = new ParticleDiffEq(settings.getMass());

        // TODO: Find reasonable values for parameters here
        // Set up the integrator that we will be using.
        integrator = new DormandPrince853Integrator(Math.pow(10, -20), 30000, 0.01, 0.01);

        flattenParticles();

        currentTime = settings.getSkip();

        // Get position, velocity, acceleration
        if (currentTime != 0) {
            try {
                integrator.integrate(particleDiffEq, 0, flattenedParticles, currentTime, flattenedParticles);
            } catch (NumberIsTooSmallException e) {
                System.out.println(e.getMessage());
                breakSimulation(ErrorMessage.ASYMPTOTE_ERROR);
                return;
            } catch (NumberIsTooLargeException e) {
                System.out.println(e.getMessage());
                breakSimulation(ErrorMessage.OVERFLOW_ERROR);
                return;
            }
        }
        // Update canvas
        updateAll();

        // Different situations if we are running infinitely or not
        if (settings.getInfinite()) {
            // Set state and change buttons to active
            state = SimulationState.ACTIVE;
            stopButton.setDisable(false);
            pauseButton.setDisable(false);
            // Speed is relevant now, so set accordingly
            speed = settings.getSpeed();
            // Start simulation.
            startSimulation();
        } else {
            // Instantly done (skips to this time)
            state = SimulationState.INACTIVE;
        }
    }

    // This method is called when an update to the graphics is needed.
    private void updateAll() {
        // Update the particles according to the state of the FlattenedParticles array.
        for (int i = 0; i < 3; i++) {
            particles[i].updateFromFlattenedParticle(Arrays.copyOfRange(flattenedParticles, 4 * i, 4 * i + 4));
            // Reminds the particles to check for acceleration
            particles[i].updateAcceleration();
        }

        // Then update the UI according to the new particles.
        timeLabel.setText(String.format("Time: %.2f secs", currentTime));
        canvasWrapper.updateCanvas();

        synchronized (synchronizationObject) {
            synchronizationObject.notify();
        }
    }

    // Asynchronously start the simulation.
    private void startSimulation() {

        // Builds a new task that simulates the particle
        Task simulation = new Task() {
            @Override
            protected Object call() throws Exception {
                // This while loop will end when the state changes
                while (state == SimulationState.ACTIVE) {
                    long taskTime = System.currentTimeMillis();
                    // Integrate between the current time, and the next time
                    try {
                        integrator.integrate(particleDiffEq, currentTime, flattenedParticles, currentTime + (speed / MAX_FRAMERATE), flattenedParticles);
                    } catch (NumberIsTooSmallException e) {
                        System.out.println(e.getMessage());
                        Platform.runLater(() -> breakSimulation(ErrorMessage.ASYMPTOTE_ERROR));
                        break;
                    } catch (NumberIsTooLargeException e) {
                        System.out.println(e.getMessage());
                        Platform.runLater(() -> breakSimulation(ErrorMessage.OVERFLOW_ERROR));
                        break;
                    }

                    // Update the UI on the main thread
                    Platform.runLater(() -> updateAll());
                    synchronized (synchronizationObject) {
                        synchronizationObject.wait();
                    }
                    // Update the time
                    currentTime += (speed / MAX_FRAMERATE);
                    // Slow things down, so the UI can update.
                    long leftoverTime = FRAMETIME - (System.currentTimeMillis() - taskTime);
                    if (leftoverTime > 0) {
                        // TODO: This lags behind by around 1/30th of a second every loop, fix!
                        Thread.sleep(leftoverTime);
                    }
                }
                return null;
            }
        };
        // Run the thread.
        Thread simulationThread = new Thread(simulation);
        simulationThread.setDaemon(true);
        simulationThread.setUncaughtExceptionHandler((t, e) -> System.out.println(e.getMessage()));
        simulationThread.start();
    }

    // Method called when stop button is pressed
    public void stopPressed() {
        state = SimulationState.INACTIVE;
        pauseButton.setText("Pause");
        pauseButton.setDisable(true);
        stopButton.setDisable(true);
        onStopListener.onEvent();
        currentTime = 0;
    }

    // Method called when pause button is pressed
    public void pausePressed() {
        switch (state) {
            // Resume
            case PAUSED:
                state = SimulationState.ACTIVE;
                pauseButton.setText("Pause");
                startSimulation();
                break;
            // Pause
            case ACTIVE:
                state = SimulationState.PAUSED;
                pauseButton.setText("Unpause");
                break;
        }
    }

    // Flattens the properties of all the particles into the FlattenedParticles array
    private void flattenParticles() {
        int index = 0;
        for (int i = 0; i < 3; i++) {
            double[] flattenedParticle = particles[i].flatten();
            for (double property : flattenedParticle) {
                flattenedParticles[index++] = property;
            }
        }
    }
}
