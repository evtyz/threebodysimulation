package stl.threebodysimulation;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

class CanvasWrapper {
    private Canvas canvas;
    private Particle[] particles;
    private GraphicsContext gc;

    private boolean trails;
    private boolean centerOfMass;

    CanvasWrapper(Canvas canvas) {
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
    }


    void setSettings(SimulationSettings settings) {
        trails = settings.getTrails();
        centerOfMass = settings.getCenterOfGravity();
        this.particles = settings.getParticles();
    }

    void updateCanvas() {
        // TODO
        for (int i = 0; i < 3; i++)
            gc.fillOval(particles[i].getPosition()[0], particles[i].getPosition()[1], 7, 7);
    }

    void clearCanvas() {
        // TODO
    }
}
