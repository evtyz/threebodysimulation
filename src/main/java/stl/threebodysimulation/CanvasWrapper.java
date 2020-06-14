package stl.threebodysimulation;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;

/**
 * A wrapper that manages the graphics of a canvas UI object.
 */
class CanvasWrapper {
    /**
     * An array of the X and Y positions of a particle.
     */
    final double[] canvasPos = {0, 0};
    /**
     * An array of the radii of each particle respectively.
     */
    final double[] circleDiameter = {0, 0, 0};
    /**
     * The canvas to draw particles on.
     */
    private final Canvas canvas;
    /**
     * The canvas to draw grid lines on.
     */
    private final Canvas gridCanvas;
    /**
     * The canvas to draw trails on.
     */
    private final Canvas trailCanvas;
    /**
     * The graphics supplied to the particle canvas.
     */
    private final GraphicsContext particlesGC;
    /**
     * The graphics supplied to the grid line canvas
     */
    private final GraphicsContext gridGC;
    /**
     * The graphics supplied to the trail canvas
     */
    private final GraphicsContext trailGC;
    /**
     * The average mass of all the particles.
     */
    double avgMass;
    /**
     * The particles to draw on the canvas.
     */
    private Particle[] particles;
    /**
     * Whether the canvas should show particle trails.
     */
    private boolean trails;
    /**
     * Whether the canvas should show the center of mass of the particles.
     */
    private boolean centerOfMass;
    /**
     * A 2D array of the old canvas position values
     */
    double[][] oldCanvasPos = new double[3][2];
    /**
     * An array containing the position of the center of mass of the system
     */
    double[] centerOfMassPos = new double[2];
    /**
     * The sum of the masses of the particles
     */
    double massSum;

    /**
     * Constructs a basic CanvasWrapper object for a particular canvas UI element.
     *
     * @param canvas The canvas that the wrapper manages.
     */
    CanvasWrapper(Canvas canvas, Canvas gridCanvas, Canvas trailCanvas) {
        this.canvas = canvas;
        this.gridCanvas = gridCanvas;
        this.trailCanvas = trailCanvas;
        particlesGC = canvas.getGraphicsContext2D();
        gridGC = gridCanvas.getGraphicsContext2D();
        trailGC = trailCanvas.getGraphicsContext2D();
    }

    /**
     * Sets the graphics options and particles for the canvas.
     *
     * @param settings The SimulationSettings object that options and particles will be read from.
     */
    void setupWithSettings(SimulationSettings settings) {
        clearCanvas();

        // Establishes the trail width
        trailGC.setLineWidth(1.0);

        // Initializing the imported variables
        trails = settings.getTrails();
        centerOfMass = settings.getCenterOfGravity();
        this.particles = settings.getParticles();

        // Arrays of the X and Y values of the particles
        final double[] particleXValues = {particles[0].getPosition()[0], particles[1].getPosition()[0], particles[2].getPosition()[0]};
        final double[] particleYValues = {particles[0].getPosition()[1], particles[1].getPosition()[1], particles[2].getPosition()[1]};
        Arrays.sort(particleXValues);
        Arrays.sort(particleYValues);

        // Geometric mean of masses.
        avgMass = Math.cbrt(particles[0].getMass() * particles[1].getMass() * particles[2].getMass());


        // Attempting to normalize the masses.
        for (int i = 0; i < 3; i++) {
            circleDiameter[i] = Math.cbrt(particles[i].getMass() / avgMass) * 4 + 8;
        }

        // Establishing the coordinate corners of the original rectangle
        double[] point1 = {particleXValues[0], particleYValues[2]};
        double[] point2 = {particleXValues[2], particleYValues[2]};
        double[] point3 = {particleXValues[2], particleYValues[0]};
        double[] point4 = {particleXValues[0], particleYValues[0]};
        double[][] rectPoints = {point1, point2, point3, point4};

        // Clears the trails canvas of any existing trails
        trailGC.clearRect(0, 0, trailCanvas.getWidth(), trailCanvas.getHeight());

        // Initializes the oldCanvasPos variable with the original position values
        for (int i = 0; i < 3; i++) {
            oldCanvasPos[i][0] = particles[i].getPosition()[0] + 400;
            oldCanvasPos[i][1] = particles[i].getPosition()[1] + 360;
        }

        // Calculates the sum of the particle masses
        massSum = particles[0].getMass() + particles[1].getMass() + particles[2].getMass();
    }

    /**
     * Updates the canvas according to the current state of the particles.
     */

    void updateCanvas() {
        // TODO
        clearCanvas();

        // Displays the positions of the particles on the canvas
        for (int i = 0; i < 3; i++) {
            canvasPos[0] = particles[i].getPosition()[0] + 400;
            canvasPos[1] = particles[i].getPosition()[1] + 360;
            particlesGC.setFill(particles[i].getColor());
            particlesGC.fillOval(canvasPos[0] - (circleDiameter[i] / 2), canvasPos[1] - (circleDiameter[i] / 2), circleDiameter[i], circleDiameter[i]);


            // Conditionally draws trails
            if (trails) {
                trailGC.setStroke(particles[i].getColor());
                trailGC.strokeLine(oldCanvasPos[i][0], oldCanvasPos[i][1], canvasPos[0], canvasPos[1]);
            }

            oldCanvasPos[i][0] = canvasPos[0];
            oldCanvasPos[i][1] = canvasPos[1];

        }

        // Conditionally draws the center of mass
        if (centerOfMass) {

            // Calculates the center of mass
            for (int i = 0; i < 2; i++) {
                centerOfMassPos[i] = ((particles[0].getMass() * particles[0].getPosition()[i])
                        + (particles[1].getMass() * particles[1].getPosition()[i])
                        + (particles[2].getMass() * particles[2].getPosition()[i])) / massSum;
            }

            // Displays the center of mass
            particlesGC.setFill(Color.BLACK);
            particlesGC.fillOval(centerOfMassPos[0] + 400, centerOfMassPos[1] + 360, 10, 10);
        }
    }

    /**
     * Clears the canvas.
     */
    void clearCanvas() {
        particlesGC.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
