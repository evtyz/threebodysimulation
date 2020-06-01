package stl.threebodysimulation;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class CanvasWrapper {
    Canvas canvas;
    Particle[] particles;
    GraphicsContext gc;

    boolean trails;
    boolean centerOfMass;

    CanvasWrapper(Canvas canvas) {
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
    }


    void setSettings(SimulationSettings settings) {
        trails = settings.getTrails();
        centerOfMass = settings.getCenterOfGravity();
    }

    void updateCanvas() {
        // TODO
        for(int i = 0; i < 3; i++)
            gc.fillOval(particles[i].getPosition()[0], particles[i].getPosition()[1], 7, 7);
    }

    void clearCanvas() {
        // TODO
    }
}
