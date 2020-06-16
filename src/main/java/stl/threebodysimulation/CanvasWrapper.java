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
     * The scale factor for the canvas size adjustment (x,y)
     */
    double[] particleScale = {1,1};
    /**
     * The scale factor for the canvas orientation adjustment (x,y)
     */
    double[] translationScale = new double[2];

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

        // Calculate the sum of the particle masses
        massSum = particles[0].getMass() + particles[1].getMass() + particles[2].getMass();

        // Coordinates of the rectangle that the canvas represents.
        double[][] canvasRectangle = calculateRectangle(scales, calculateBuffer(particles));

        // TODO for debugging; remove later
        for(int i = 0; i < 4; i++) {
            System.out.println(" ");
            for(int j = 0; j < 2; j++) {
                System.out.println(canvasRectangle[i][j]);
            }
        }

        // Geometric mean of masses.
        avgMass = Math.cbrt(particles[0].getMass() * particles[1].getMass() * particles[2].getMass());


        // Attempting to normalize the masses.
        for (int i = 0; i < 3; i++) {
            circleDiameter[i] = Math.cbrt(particles[i].getMass() / avgMass) * 4 + 8;
        }


        // Clears the trails canvas of any existing trails and grid lines
        trailGC.clearRect(0, 0, trailCanvas.getWidth(), trailCanvas.getHeight());
        gridGC.clearRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());

        // Initializes the oldCanvasPos variable with the original position values
        for (int i = 0; i < 3; i++) {
            oldCanvasPos[i][0] = particles[i].getPosition()[0] + 400;
            oldCanvasPos[i][1] = 360 - particles[i].getPosition()[1];
        }

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

    /**
     * Calculates the scales by which to adjust the canvas size and orientation.
     *
     * @param canvasRectangle The corner coordinates of the original rectangle adjusted for buffer.
     */
    private void setScaleFactors(double[][] canvasRectangle) {

        // Declaring variables used by the method
        double aspectFactor;
        double aspectAdjust;
        double adjDiff;
        double newHeight;
        double newWidth;

        // Calculates absolute width and height of the rectangle
        double rectangleHeight = Math.abs(canvasRectangle[1][1] - canvasRectangle[0][1]);
        double rectangleWidth = Math.abs(canvasRectangle[3][0] - canvasRectangle[1][0]);

        // Calculates the aspect ratios of the canvas and the rectangle
        double rectangleAspect = rectangleWidth / rectangleHeight;
        int canvasAspect = 10 / 9;

        // Adjusts the rectangle to fit the canvas aspect ratio
        if (rectangleAspect > canvasAspect){
            aspectFactor = rectangleAspect / canvasAspect;
            aspectAdjust = rectangleHeight * aspectFactor;
            adjDiff = (aspectAdjust - rectangleHeight) / 2;

            canvasRectangle[1][1] = canvasRectangle[1][1] + adjDiff;
            canvasRectangle[3][1] = canvasRectangle[3][1] + adjDiff;
            canvasRectangle[0][1] = canvasRectangle[0][1] - adjDiff;
            canvasRectangle[2][1] = canvasRectangle[2][1] - adjDiff;
        }

        else if (rectangleAspect < canvasAspect) {
            aspectFactor = canvasAspect / rectangleAspect;
            aspectAdjust = rectangleWidth * aspectFactor;
            adjDiff = aspectAdjust - rectangleWidth;

            canvasRectangle[2][0] = canvasRectangle[2][0] + adjDiff;
            canvasRectangle[3][0] = canvasRectangle[3][0] + adjDiff;
            canvasRectangle[0][0] = canvasRectangle[0][0] - adjDiff;
            canvasRectangle[1][0] = canvasRectangle[1][0] - adjDiff;
        }

        // Defines the new height and width of the rectangle
        newHeight = Math.abs(canvasRectangle[1][1] - canvasRectangle[0][1]);
        newWidth = Math.abs(canvasRectangle[3][0] - canvasRectangle[1][0]);

        // Calculates a scale factor by which to adjust the canvas particles
        particleScale[0] = ((newHeight - 720) / 720) + 1;
        particleScale[1] = ((newWidth - 800) / 800) + 1;

        // Calculates the coordinates of the center of the rectangle
        translationScale[0] = (canvasRectangle[3][0] - canvasRectangle[1][0]) / 2;
        translationScale[1] = (canvasRectangle[1][1] - canvasRectangle[0][1]) / 2;
    }

    /**
     * Adjusts the original rectangle by the length of the buffer.
     *
     * @param originalRectangle The coordinates of the corners of the original rectangle.
     * @param buffer The length by which the new rectangle is extended outwards.
     * @return The new rectangle adjusted by the buffer length.
     */
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
     * @param particles The particle objects that contain information about velocity and mass.
     * @return An integer that represents the buffer size.
     */
    private double calculateBuffer(Particle[] particles) {
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
        System.out.println("buffer: " + buffer);
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
