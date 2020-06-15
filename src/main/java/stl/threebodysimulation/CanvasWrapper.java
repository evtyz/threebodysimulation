package stl.threebodysimulation;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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
     * The scale factor for the canvas size adjustment
     */
    double particleScale;
    /**
     * The scale factor for the canvas orientation adjustment
     */
    double translationScale;

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
     * @param scales An 2D array of doubles that represent the max/min x/y coordinates of the particles in the first 10 seconds of simulation.
     * @param settings The SimulationSettings object that options and particles will be read from.
     */
    void setupScalesAndSettings(double[][] scales, SimulationSettings settings) {
        clearCanvas();

        // Establishes the trail width
        trailGC.setLineWidth(1.0);

        // Establishes the center of gravity guide width
        particlesGC.setLineWidth(1.0);

        // Establishes the grid line canvas line width
        gridGC.setLineWidth(0.4);

        // Initializing the imported variables
        trails = settings.getTrails();
        centerOfMass = settings.getCenterOfGravity();
        this.particles = settings.getParticles();

        // Coordinates of the rectangle that the canvas represents.
        double[][] canvasRectangle = calculateRectangle(scales, calculateBuffer(scales, particles));

        // Rounds the values of the canvasRectangle
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 2; j++) {
                canvasRectangle[i][j] = Math.round(canvasRectangle[i][j]);
            }
        }

        // Geometric mean of masses.
        avgMass = Math.cbrt(particles[0].getMass() * particles[1].getMass() * particles[2].getMass());


        // Attempting to normalize the masses.
        for (int i = 0; i < 3; i++) {
            circleDiameter[i] = Math.cbrt(particles[i].getMass() / avgMass) * 4 + 8;
        }


        // Clears the trails canvas of any existing trails
        trailGC.clearRect(0, 0, trailCanvas.getWidth(), trailCanvas.getHeight());

        // Initializes the oldCanvasPos variable with the original position values
        for (int i = 0; i < 3; i++) {
            oldCanvasPos[i][0] = particles[i].getPosition()[0] + 400;
            oldCanvasPos[i][1] = 360 - particles[i].getPosition()[1];
        }

        // Calculates the sum of the particle masses
        massSum = particles[0].getMass() + particles[1].getMass() + particles[2].getMass();

        // Draws grid lines
        for (int i = -3; i < 4; i++) {
            gridGC.setFill(Color.valueOf("#e6e6e6"));
            gridGC.strokeLine((i * 100) + 400, 720, (i * 100) + 400, 0);
            gridGC.strokeLine(0, 360 - (i * 100), 800, 360 - (i * 100));
        }

        // Draws axis
        gridGC.setFill(Color.BLACK);
        gridGC.strokeLine(400, 720, 400, 0);
        gridGC.strokeLine(0, 360, 800, 360);

        setScaleFactors(canvasRectangle);
    }

    private void setScaleFactors(double[][] canvasRectangle){

        // The height and width of the new rectangle
        double canvasRectangleHeight = canvasRectangle[1][1] - canvasRectangle[0][1];
        double canvasRectangleWidth = canvasRectangle[3][0] - canvasRectangle[1][0];

        // Adjusting the new rectangle's height to fit aspect ratio
        if (canvasRectangleHeight > 720) {
            while (canvasRectangleHeight % 720 != 0) {
                canvasRectangle[1][1]++;
                canvasRectangleHeight++;
            }
        } else if (canvasRectangleHeight < 720) {
            while (720 % canvasRectangleHeight != 0) {
                canvasRectangle[1][1]++;
                canvasRectangleHeight++;
            }
        }

        // Adjusting the new rectangle's width to fit aspect ratio
        if (canvasRectangleWidth > 800){
            while (canvasRectangleWidth % 800 != 0) {
                canvasRectangle[3][0]++;
                canvasRectangleWidth++;
            }
        } else if (canvasRectangleWidth < 800) {
            while (800 % canvasRectangleWidth != 0) {
                canvasRectangle[3][0]++;
                canvasRectangleWidth++;
            }
        }

        
    }
    private static double[][] calculateRectangle(double[][] originalRectangle, double buffer) {
        // TODO: calculate rectangle.
        double[][] newRectangle = new double[4][2];

        // Adjusting point 1 (lower left)
        newRectangle[0][0] = originalRectangle[0][0] - buffer;
        newRectangle[0][1] = originalRectangle[0][1] - buffer;

        // Adjusting point 2 (upper left)
        newRectangle[1][0] = originalRectangle[1][0] - buffer;
        newRectangle[1][1] = originalRectangle[1][1] + buffer;

        // Adjusting point 3 (lower right)
        newRectangle[2][0] = originalRectangle[2][0] + buffer;
        newRectangle[2][1] = originalRectangle[2][1] - buffer;

        // Adjusting point 4 (upper right)
        newRectangle[3][0] = originalRectangle[3][0] + buffer;
        newRectangle[3][1] = originalRectangle[3][1] + buffer;

        return newRectangle;
    }

    /**
     * Returns a buffer size based on particles and an original rectangle.
     *
     * @param originalRectangle The coordinates of the corners of the original rectangle.
     * @param particles The particle objects that contain information about velocity and mass.
     * @return An integer that represents the buffer size.
     */
    private double calculateBuffer(double[][] originalRectangle, Particle[] particles) {
        // TODO: Calculate a buffer size

        // Declares buffer variable
        double buffer;

        // Constructs an array of the absolute values of the particle velocities
        double[] particleSquares = new double[3];
        for (int i = 0; i < 3; i++) {
            particleSquares[i] = Math.sqrt((particles[i].getVelocity()[0] * particles[i].getVelocity()[0])
                    + (particles[i].getVelocity()[1] * particles[i].getVelocity()[1]));
        }

        // Calculates the average squared velocity
        double avgSquaredVelocity = Math.sqrt((particleSquares[0] * particles[0].getMass() + particleSquares[1] * particles[1].getMass()
                + particleSquares[2] * particles[2].getMass()) / massSum);

        // Determines the buffer as a product of ASV and a constant
        buffer = avgSquaredVelocity * 5;

        return buffer;
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
            canvasPos[1] = 360 - particles[i].getPosition()[1];
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

            // TODO: center of mass sometimes obscures visibility of actual particle
            // Calculates the center of mass
            for (int i = 0; i < 2; i++) {
                centerOfMassPos[i] = ((particles[0].getMass() * particles[0].getPosition()[i])
                        + (particles[1].getMass() * particles[1].getPosition()[i])
                        + (particles[2].getMass() * particles[2].getPosition()[i])) / massSum;
            }

            // Displays the center of mass
            particlesGC.setFill(Color.valueOf("#555555"));
            particlesGC.fillOval(centerOfMassPos[0] + 400 - 5, 360 - centerOfMassPos[1] - 5, 10, 10);

            particlesGC.setStroke(Color.valueOf("#555555"));
            for (int i = 0; i < 2; i++) {
                for (int j = i + 1; j < 3; j++) {
                    particlesGC.strokeLine(oldCanvasPos[i][0], oldCanvasPos[i][1], oldCanvasPos[j][0], oldCanvasPos[j][1]);
                }
            }
            for (int i = 0; i < 3; i++) {
                particlesGC.strokeLine(oldCanvasPos[i][0], oldCanvasPos[i][1], centerOfMassPos[0] + 400, 360 - centerOfMassPos[1]);
            }

        }
    }

    /**
     * Clears the canvas.
     */
    void clearCanvas() {
        particlesGC.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
