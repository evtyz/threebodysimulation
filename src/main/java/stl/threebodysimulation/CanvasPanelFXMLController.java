package stl.threebodysimulation;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;

import java.util.Arrays;

// TODO: Documentation

// The controller for the canvas with graphics.
public class CanvasPanelFXMLController {
    // Listener called when stop button is pressed
    public Listener onStopListener;

    // Particles managed by our canvas.
    public Particle[] particles;

    // Particles in flattened form, where we can manipulate them using math library.
    public double[] flattenedParticles = new double[12];

    // UI element declarations
    @FXML
    private Canvas canvas;
    @FXML
    private Button pauseButton;
    @FXML
    private Button stopButton;
    @FXML
    private Label timeLabel;

    // The state of the simulation (Not started, running, paused, finished)
    private SimulationState state;

    // Some handy simulation variables
    private double currentTime;
    private double speed;

    // The runnable task that involves simulation
    private Task simulation;

    private static final int FRAMERATE = 25;

    // Math variables
    ParticleDiffEq particleDiffEq;
    DormandPrince853Integrator integrator;

    // Blank Constructor for FXML
    public CanvasPanelFXMLController() {
    }

    // Sets up particles according to the given array
    public void setParticles(Particle[] particles) {
        this.particles = particles;
    }

    // Setup method that is called from scene controller
    public void setup() {
        state = SimulationState.NOT_STARTED;
        pauseButton.setDisable(true);
        stopButton.setDisable(true);
        currentTime = 0;
    }

    public void breakSimulation() {
        updateParticlesAndCanvas();
        stopPressed();
        SceneFXMLController.openPopupWindow("Simulation Error", "An asymptote has been detected, and the simulation has ceased. Please make sure your inputs do not lead to asymptotic behavior, and try again.", new Image("/errorIcon.png"), canvas.getScene().getWindow());
    }

    // This method is called when we want to start running a simulation.
    public void runSimulation(SimulationSettings settings) {
        // INPUTS:
        // settings: SimulationSettings, the settings that we are simulating with.
        // Runs the simulation according to these settings.

        // Set up the particle differential equation according to the masses of each particle.
        particleDiffEq = new ParticleDiffEq(settings.returnMass());

        // TODO: Find reasonable values for parameters here
        // Set up the integrator that we will be using. The minimum step value is set to the minimum value of a double.
        integrator = new DormandPrince853Integrator(Math.pow(10, -20), 30000, 0.01, 0.01);
        flattenParticles();

        currentTime = settings.skip;
        // Get position, velocity, acceleration
        if (currentTime != 0) {
            try {
                integrate(true);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                breakSimulation();
                return;
            }
        }
        // Update canvas
        updateParticlesAndCanvas();

        // Different situations if we are running infinitely or not
        if (settings.isInfinite) {
            // Set state and change buttons to active
            state = SimulationState.RUNNING;
            stopButton.setDisable(false);
            pauseButton.setDisable(false);
            // Speed is relevant now, so set accordingly
            speed = settings.speed;
            // Start simulation.
            startSimulation();
        } else {
            // TODO: Test this part of the code.
            // Instantly done (skips to this time)
            state = SimulationState.FINISHED;
        }
    }

    // This method is called when an update to the graphics is needed.
    public void updateParticlesAndCanvas() {
        // Update the particles according to the state of the FlattenedParticles array.
        unflattenParticles();

        // Then update the canvas according to the new particles.
        updateCanvas();
    }

    public void integrate(boolean start) throws Exception {
        if (start) {
            integrator.integrate(particleDiffEq, 0, flattenedParticles, currentTime, flattenedParticles);
        } else {
            integrator.integrate(particleDiffEq, currentTime, flattenedParticles, currentTime + (speed / FRAMERATE), flattenedParticles);
        }
    }

    // Asynchronously start the simulation.
    public void startSimulation() {

        // Builds a new task that simulates the particle
        simulation = new Task() {
            @Override
            protected Object call() throws Exception {
                // This while loop will end when the state changes
                while (state == SimulationState.RUNNING) {
                    // Integrate between the current time, and the next time
                    try {
                        integrate(false);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                breakSimulation();
                            }
                        });
                        break;
                    }

                    // Update the UI on the main thread
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            updateParticlesAndCanvas();
                        }
                        // TODO: interaction that occurs when two particles collide must be checked for.
                    });
                    // Update the time
                    currentTime += (speed / FRAMERATE);
                    // Slow things down, so the UI can update.
                    Thread.sleep(1000 / FRAMERATE);
                }
                return null;
            }
        };
        // Run the thread.
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
        pauseButton.setText("Pause");
        pauseButton.setDisable(true);
        stopButton.setDisable(true);
        onStopListener.onEvent();
        currentTime = 0;
    }

    // Method called when pause button is pressed
    public void pausePressed() {
        // TODO: Finish implementing method
        switch (state) {
            case PAUSED:
                state = SimulationState.RUNNING;
                pauseButton.setText("Pause");
                startSimulation();
                break;
            // Resume
            case RUNNING:
                state = SimulationState.PAUSED;
                pauseButton.setText("Unpause");
                break;
            // Pause
        }
    }

    // Flattens the properties of all the particles into the FlattenedParticles array
    public void flattenParticles() {
        int index = 0;
        for (int i = 0; i < 3; i++) {
            double[] flattenedParticle = particles[i].flatten();
            for (double property : flattenedParticle) {
                flattenedParticles[index++] = property;
            }
        }
    }

    // Updates the particles according to the new FlattenedParticles array
    public void unflattenParticles() {
        for (int i = 0; i < 3; i++) {
            particles[i].updateFromFlattenedParticle(Arrays.copyOfRange(flattenedParticles, 4 * i, 4 * i + 4));
            // Reminds the particles to check for acceleration
            particles[i].updateAcceleration();
        }
    }

    // Updates the canvas according to the positions of the particles.
    private void updateCanvas() {
        // TODO
        timeLabel.setText(String.format("Time: %.2f secs", currentTime));
    }
}
