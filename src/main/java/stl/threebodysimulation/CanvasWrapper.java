package stl.threebodysimulation;

import javafx.scene.canvas.Canvas;

public class CanvasWrapper {
    Canvas canvas;
    Particle[] particles;

    boolean trails;
    boolean centerOfMass;

    CanvasWrapper(Canvas canvas) {
        this.canvas = canvas;
    }

    void setSettings(SimulationSettings settings) {
        trails = settings.hasTrails;
        centerOfMass = settings.showsCenterOfGravity;
    }

    void updateCanvas() {
        // TODO
    }

    void clearCanvas() {
        // TODO
    }
}
