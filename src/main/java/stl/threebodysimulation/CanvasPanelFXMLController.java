package stl.threebodysimulation;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.LinkedHashMap;

/**
 * The controller for the Canvas with graphics.
 */
public class CanvasPanelFXMLController {

    boolean lock = false;

    /**
     * Maximum framerate allowed by program.
     */
    private static final int MAX_FRAMERATE = 100;

    /**
     * Amount of time in milliseconds that a frame appears on screen
     */
    private static final long FRAMETIME = 1000 / MAX_FRAMERATE;

    /**
     * Object used for synchronization between threads
     */
    private static final Object synchronizationObject = new Object();
    /**
     * Flattened version of the Particle array for input into a ParticleDifferentialEquations object.
     */
    private final double[] flattenedParticles = new double[12];
    /**
     * A Listener that is called when the simulation stops.
     */
    private Listener onStopListener;
    /**
     * Particle array that the controller manages.
     */
    private Particle[] particles;
    /**
     * A SimulationState object that represents the state of the simulation. One of {INACTIVE, ACTIVE, PAUSED}.
     */
    private SimulationState state;

    /**
     * The current time in the simulation, in seconds.
     */
    private double currentTime;

    /**
     * The current speed of the simulation, in simulation seconds / real seconds.
     */
    private double speed;

    /**
     * The name of the CSV file we save stats to.
     */
    private String CSVFilePath = "";

    /**
     * A ParticleDifferentialEquations object that represents the unique differential equation of the particles, with respect to their masses in Earth units.
     */
    private ParticleDifferentialEquations particleDifferentialEquations;

    /**
     * The DormandPrince853Integrator provided by Apache Commons Math that we used to approximate values according to the differential equation.
     */
    private DormandPrince853Integrator integrator;

    /**
     * The Canvas UI object.
     */
    @FXML
    private Canvas canvas;

    /**
     * The grid Canvas UI object.
     */
    @FXML
    private Canvas gridCanvas;

    /**
     * The trail Canvas UI object.
     */
    @FXML
    private Canvas trailCanvas;

    /**
     * A CanvasWrapper object that manages the canvas UI object.
     */
    private CanvasWrapper canvasWrapper;

    /**
     * The Button UI object for pausing.
     */
    @FXML
    private Button pauseButton;

    /**
     * The Button UI object for stopping.
     */
    @FXML
    private Button stopButton;

    /**
     * The Label UI object that writes the current time of the simulation.
     */
    @FXML
    private Label timeLabel;

    /**
     * Constructor, for use by the FXML loader.
     */
    public CanvasPanelFXMLController() {
    }

    /**
     * Sets up a CSV file with the specified filename
     *
     * @param filename The name of the CSV file to be setup
     * @return The path of the new CSV file.
     */
    private String setupCSV(String filename) {
        String filepath = String.format(SceneFXMLController.CSVFilePathTemplate, filename);
        File CSVDirectory = new File(SceneFXMLController.CSV_DIRECTORY_NAME);
        File CSVFile = new File(filepath);
        try {
            //noinspection ResultOfMethodCallIgnored : as long as a directory exists, we don't care if mkdir was successful or not.
            CSVDirectory.mkdir(); // Make the CSV folder if it doesn't exist

            if (!CSVFile.createNewFile()) { // Create a new file
                if (CSVFile.delete()) { // If there already is one, delete the old file
                    //noinspection ResultOfMethodCallIgnored : this should always return true, since it only runs if the file was successfully deleted.
                    CSVFile.createNewFile(); // And try creating the new file again.
                } else {
                    breakSimulation(new FilenameSpecificMessage(
                            FilenameSpecificMessage.Type.OVERWRITE_ERROR,
                            CSVFile.getAbsolutePath()
                    ));
                    return "";
                }
            }

            BufferedWriter headerWriter = Files.newBufferedWriter(Paths.get(filepath)); // A FileWriter object to modify the file

            CSVPrinter headerPrinter = new CSVPrinter(headerWriter, CSVFormat.DEFAULT); // CSVPrinter to parse data and write in CSV format using a FileWriter
            String[] headers = new String[]{ // Column headers for a CSV file.
                    "Time",
                    "1 X Pos",
                    "1 Y Pos",
                    "1 X Vel",
                    "1 Y Vel",
                    "1 X Acc",
                    "1 Y Acc",
                    "2 X Pos",
                    "2 Y Pos",
                    "2 X Vel",
                    "2 Y Vel",
                    "2 X Acc",
                    "2 Y Acc",
                    "3 X Pos",
                    "3 Y Pos",
                    "3 X Vel",
                    "3 Y Vel",
                    "3 X Acc",
                    "3 Y Acc",
            };
            //noinspection RedundantCast
            headerPrinter.printRecord((Object[]) headers); // Write the headers into the file. Cast for clarity's sake.
            headerWriter.close(); // Close the file.

        } catch (IOException e) {
            e.printStackTrace(); // Should never happen.
        }

        return filepath;
    }

    /**
     * Sets up the initial state of the controller. Only called once.
     */
    void setup() {
        state = SimulationState.INACTIVE;
        // Buttons should be disabled until the simulation runs.
        pauseButton.setDisable(true);
        stopButton.setDisable(true);
        // Initial time.
        currentTime = 0;
        // Wrap the canvas.
        canvasWrapper = new CanvasWrapper(canvas, gridCanvas, trailCanvas);
    }

    /**
     * Sets a Listener that is called when the simulation stops.
     *
     * @param listener The listener that will be called.
     */
    void setOnStopListener(Listener listener) {
        onStopListener = listener;
    }

    /**
     * Sets the Particle arrays to be simulated.
     *
     * @param particles An array of 3 particles to be simulated by the canvas controller.
     */
    void setParticles(Particle[] particles) {
        this.particles = particles;
    }

    /**
     * Runs the simulation according to the given settings.
     *
     * @param settings The SimulationSettings object that supplies properties for the simulation.
     */
    void runSimulation(SimulationSettings settings) {

        String CSVFileName = settings.getCSVFileName();
        if (CSVFileName.equals("")) {
            CSVFilePath = "";
        } else {
            CSVFilePath = setupCSV(settings.getCSVFileName());
            if (CSVFilePath.equals("")) {
                return;
            }
        }

        // Set up the particle differential equation according to the masses of each particle.
        particleDifferentialEquations = new ParticleDifferentialEquations(settings.getMass());

        // Set up the integrator that we will be using. The minimum step size is 10 ^ -20, so that the integrator will return errors at asymptotes.
        integrator = new DormandPrince853Integrator(Math.pow(10, -20), 10000, 0.01, 0.0001);

        // Flatten particles into the flattenedParticles array.
        flattenParticles();

        // Set current time.
        currentTime = settings.getSkip();

        // Get position, velocity, acceleration at current time.
        if (currentTime != 0) {
            try {
                // Get the position and velocity of particles at currentTime
                integrator.integrate(particleDifferentialEquations, 0, flattenedParticles, currentTime, flattenedParticles);
            } catch (NumberIsTooSmallException e) {
                // Asymptote error (the integrator can't converge and gives up)
                System.out.println(e.getMessage());
                breakSimulationAfterUpdate(FilenameUnspecificMessage.ASYMPTOTE_ERROR);
                return;
            } catch (NumberIsTooLargeException e) {
                // Double overflow error (inputs too large for double datatype to handle)
                System.out.println(e.getMessage());
                breakSimulationAfterUpdate(FilenameUnspecificMessage.OVERFLOW_ERROR);
                return;
            } catch (Exception e) {
                // Other errors
                System.out.println(e.getMessage());
                breakSimulationAfterUpdate(FilenameUnspecificMessage.UNKNOWN_ERROR);
                return;
            }
        }

        double[][] scales = generateScale(integrator, flattenedParticles.clone(), settings);

        updateParticles();

        // Clear out the canvas and provide the wrapper with settings.
        canvasWrapper.setupScalesAndSettings(scales, settings);

        // Update canvas
        updateCanvas();

        if (!CSVFilePath.equals("")) {
            updateCSV();
        }

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
            // We are already done, so set the state to inactive.
            state = SimulationState.INACTIVE;
        }
    }

    /**
     * Calculates the maximum and minimum x/y coordinates during the first ten seconds of simulation.
     *
     * @param integrator The integrator used.
     * @param particles The particles' initial states in flattened form.
     * @param settings The settings of the simulation.
     * @return The four corners of the smallest possible rectangle that all three particles do not escape in the first 10 seconds of simulation.
     */
    private double[][] generateScale(DormandPrince853Integrator integrator, double[] particles, SimulationSettings settings) {
        final int SIMULATION_LENGTH = 10;
        double simulationTime = currentTime;

        double[][] minsAndMaxs = minAndMaxPositions(particles);

        for (double time = simulationTime; time < SIMULATION_LENGTH * settings.getSpeed() + simulationTime; time += settings.getSpeed() / 5) {
            try {
                // Get the position and velocity of particles at currentTime
                integrator.integrate(particleDifferentialEquations, time, particles, time + settings.getSpeed() / 5, particles);
                double[][] currentMinsAndMaxs = minAndMaxPositions(particles);
                for (int i = 0; i < 2; i++) {
                    if (currentMinsAndMaxs[0][i] < minsAndMaxs[0][i]) {
                        minsAndMaxs[0][i] = currentMinsAndMaxs[0][i];
                    }
                    if (currentMinsAndMaxs[1][i] > minsAndMaxs[1][i]) {
                        minsAndMaxs[1][i] = currentMinsAndMaxs[1][i];
                    }
                }
            } catch (Exception e) {
                // All exceptions mean automatic break.
                break;
            }
        }

        double[][] coordinatePositions = new double[4][2];
        int index = 0;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                coordinatePositions[index++] = new double[] {minsAndMaxs[i][0], minsAndMaxs[j][1]};
            }
        }
        return coordinatePositions;
    }

    /**
     * Calculates the min and max x/y coordinates of a single set of particles
     *
     * @param particles Particles in flattened form.
     * @return double 2D array {{minimum x, minimum y}, {maximum x, maximum y}}
     */
    private double[][] minAndMaxPositions(double[] particles) {
        double[] xPositions = new double[] {particles[0], particles[4], particles[8]};
        double[] yPositions = new double[] {particles[1], particles[5], particles[9]};
        Arrays.sort(xPositions);
        Arrays.sort(yPositions);
        return new double[][] {{xPositions[0], yPositions[0]}, {xPositions[2], yPositions[2]}};
    }

    /**
     * Updates the flattenedParticles array according to the particles array.
     */
    private void flattenParticles() {
        int index = 0;
        for (int id = 0; id < 3; id++) {
            // Flatten the particle
            double[] flattenedParticle = particles[id].flatten();
            for (double property : flattenedParticle) {
                // Fill out the array according to the flattened particle.
                flattenedParticles[index++] = property;
            }
        }
    }

    /**
     * Updates particles.
     */
    private void updateParticles() {
        for (int id = 0; id < 3; id++) {
            // Update each of the particles according to the state of the FlattenedParticles array.
            particles[id].update(Arrays.copyOfRange(flattenedParticles, 4 * id, 4 * id + 4));
        }
    }

    /**
     * Updates the canvas.
     */
    private void updateCanvas() {
        timeLabel.setText(String.format("Time: %.5f secs", currentTime));
        canvasWrapper.updateCanvas();
    }

    private void updateCSV() {
        try {
            BufferedWriter dataWriter = Files.newBufferedWriter(Paths.get(CSVFilePath), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
            CSVPrinter dataPrinter = new CSVPrinter(dataWriter, CSVFormat.DEFAULT);
            //noinspection RedundantCast
            dataPrinter.printRecord((Object[]) getRecord()); // Cast for clarity's sake.
            dataWriter.close();
        } catch (IOException e) {
            e.printStackTrace(); // Should never occur, unless the user deletes the file midway through a simulation.
        }
    }

    /**
     * Updates particles and update canvas.
     */
    private void updateAll() {
        updateParticles();

        updateCanvas();

        if (!CSVFilePath.equals("")) { // Check if the user wants to save CSVs. If not, CSVFileName should equal an empty string.
            updateCSV();
        }

        lock = false;

        // Finally, let the thread that runs the simulation unpause.
        synchronized (synchronizationObject) {
            synchronizationObject.notify();
        }
    }

    /**
     * Converts particle information into an array of strings.
     *
     * @return The array of strings to be stored as the next row in a CSV file.
     */
    private String[] getRecord() {
        String[] record = new String[1 + 6 * 3]; // Time + 3 particles * 6 attributes for each particle
        int index = 0;
        record[index++] = String.format("%.07f", currentTime);
        for (Particle particle : particles) {
            LinkedHashMap<String, double[]> information = particle.getPackage();
            for (String vectorKey : information.keySet()) {
                record[index++] = String.format("%.07f", information.get(vectorKey)[0]);
                record[index++] = String.format("%.07f", information.get(vectorKey)[1]);
            }
        }
        return record;
    }

    /**
     * Starts the simulation in a separate thread.
     */
    private void startSimulation() {

        // Builds a new JavaFX task that simulates the particle
        Task<Void> simulation = new Task<>() {
            /**
             * This method is called by the thread and calculates values for the simulation.
             * @return null: inherited from task interface.
             * @throws Exception If the task is interrupted.
             */
            @Override
            protected Void call() throws Exception {
                while (state == SimulationState.ACTIVE) { // Break if the simulation becomes inactive or paused.
                    long taskTime = System.currentTimeMillis(); // Record current time (to sync framerate)
                    try {
                        // Store the state of the particles at the next frame.
                        integrator.integrate(particleDifferentialEquations, currentTime, flattenedParticles, currentTime + (speed / MAX_FRAMERATE), flattenedParticles);
                    } catch (NumberIsTooSmallException e) {
                        // Asymptote error catching
                        System.out.println(e.getMessage());
                        Platform.runLater(() -> breakSimulationAfterUpdate(FilenameUnspecificMessage.ASYMPTOTE_ERROR));
                        break;
                    } catch (NumberIsTooLargeException e) {
                        // Double overflow error catching.
                        System.out.println(e.getMessage());
                        Platform.runLater(() -> breakSimulationAfterUpdate(FilenameUnspecificMessage.OVERFLOW_ERROR));
                        break;
                    } catch (Exception e) {
                        // Other errors
                        System.out.println(e.getMessage());
                        Platform.runLater(() -> breakSimulationAfterUpdate(FilenameUnspecificMessage.UNKNOWN_ERROR));
                        break;
                    }

                    // Update the time
                    currentTime += (speed / MAX_FRAMERATE);

                    lock = true;

                    // Update the UI on the main thread
                    Platform.runLater(() -> updateAll());

                    // Wait for the UI to update completely and notify this thread.
                    if (lock) {
                        synchronized (synchronizationObject) {
                            synchronizationObject.wait();
                        }
                    }

                    // Check how much time left to wait before next frame
                    long leftoverTime = FRAMETIME - (System.currentTimeMillis() - taskTime);
                    if (leftoverTime > 0) {
                        // Wait until next frame.
                        // TODO: This lags behind by around 1/30th of a second every loop, fix!
                        //noinspection BusyWait
                        Thread.sleep(leftoverTime);
                    }
                }
                return null;
            }
        };
        // Run the thread.
        Thread simulationThread = new Thread(simulation);
        simulationThread.setDaemon(true);
        // For debugging purposes, an exception handler to terminal output is created.
        simulationThread.setUncaughtExceptionHandler((t, e) -> System.out.println(e.getMessage()));
        simulationThread.start();
    }

    /**
     * Stops the simulation. Links to stop button.
     */
    public void stopPressed() {
        state = SimulationState.INACTIVE; // Change state
        pauseButton.setText("Pause"); // Change pause button to say "pause" instead of "unpause"
        // Disable buttons
        pauseButton.setDisable(true);
        stopButton.setDisable(true);
        // Let the listener know the simulation stopped.
        onStopListener.onEvent();
    }

    /**
     * Stop or start the simulation, depending on current state. Links to pause button.
     */
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
                pauseButton.setText("Resume");
                break;
        }
    }

    /**
     * Breaks the simulation prematurely and sends an error message to the user.
     *
     * @param errorMessage What error message to send to the user.
     */
    private void breakSimulationAfterUpdate(PopupMessage errorMessage) {
        updateAll();
        breakSimulation(errorMessage);
    }

    private void breakSimulation(PopupMessage errorMessage) {
        stopPressed();
        SceneFXMLController.openErrorWindow(errorMessage, canvas.getScene().getWindow());
    }
}