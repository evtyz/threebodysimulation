package stl.threebodysimulation;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * A wrapper that manages the graphics of a canvas UI object.
 */
class CanvasWrapper {

    // TODO: Document the purpose of these variables.

    final double[] canvasPos = {0, 0};
    final double[] circleRad = {0, 0, 0};
    /**
     * The canvas to be drawn on.
     */
    private final Canvas canvas;
    /**
     * The graphics supplied to the canvas.
     */
    private final GraphicsContext gc;
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
     * Constructs a basic CanvasWrapper object for a particular canvas UI element.
     *
     * @param canvas The canvas that the wrapper manages.
     */
    CanvasWrapper(Canvas canvas) {
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
    }

    /**
     * Sets the graphics options and particles for the canvas.
     *
     * @param settings The SimulationSettings object that options and particles will be read from.
     */
    void setupWithSettings(SimulationSettings settings) {
        clearCanvas();
        trails = settings.getTrails();
        centerOfMass = settings.getCenterOfGravity();
        this.particles = settings.getParticles();

        // Geometric mean of masses.
        avgMass = Math.cbrt(particles[0].getMass() * particles[1].getMass() * particles[2].getMass());


        // Attempting to normalize the masses.
        for (int i = 0; i < 3; i++) {
            circleRad[i] = Math.cbrt(particles[i].getMass() / avgMass) * 4 + 8;
        }
    }

    /**
     * Updates the canvas according to the current state of the particles.
     */

    void updateCanvas() {
        // TODO
        clearCanvas();
        for (int i = 0; i < 3; i++) {
            canvasPos[0] = particles[i].getPosition()[0] + 400 - circleRad[i];
            canvasPos[1] = particles[i].getPosition()[1] + 360 - circleRad[i];
            gc.setFill(particles[i].getColor());
            gc.fillOval(canvasPos[0], canvasPos[1], circleRad[i], circleRad[i]);
        }
    }

    /**
     * Clears the canvas.
     */
    void clearCanvas() {
        // TODO
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
