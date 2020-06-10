package stl.threebodysimulation;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * A wrapper that manages the graphics of a canvas UI object.
 */
class CanvasWrapper {

    /**
     * The canvas to be drawn on.
     */
    private final Canvas canvas;

    /**
     * The particles to draw on the canvas.
     */
    private Particle[] particles;

    /**
     * The graphics supplied to the canvas.
     */
    private final GraphicsContext gc;

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
    
    double[] canvasPos = {0,0};
    double avgMass;
    double[] circleRad = {0, 0, 0};

    /**
     * Sets the graphics options and particles for the canvas.
     *
     * @param settings The SimulationSettings object that options and particles will be read from.
     */
    void setSettings(SimulationSettings settings) {
        trails = settings.getTrails();
        centerOfMass = settings.getCenterOfGravity();
        this.particles = settings.getParticles();

        avgMass = particles[0].getMass() * particles[1].getMass() * particles[2].getMass();
        for (int i = 0; i < 3; i++){
            circleRad[i] = Math.sqrt(particles[i].getMass() / avgMass) * 7;
        }
    }

    /**
     * Updates the canvas according to the current state of the particles.
     */

    void updateCanvas() {
        // TODO
        for (int i = 0; i < 3; i++) {
            canvasPos[0] = particles[i].getPosition()[0] + 400;
            canvasPos[1] = particles[i].getPosition()[1] + 360;
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
