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
        trails = settings.hasTrails;
        centerOfMass = settings.showsCenterOfGravity;
    }

    void updateCanvas() {
        // TODO
        for(int i = 0; i < 3; i++)
            gc.fillOval(particles[i].position[0], particles[i].position[1], 7, 7);
    }

    void clearCanvas() {
        // TODO
    }
}
