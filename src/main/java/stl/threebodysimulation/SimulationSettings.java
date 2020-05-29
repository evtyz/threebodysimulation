package stl.threebodysimulation;

// A class that wraps simulation settings
public class SimulationSettings {

    // Settings
    private Particle[] particles;
    private boolean isInfinite;
    private boolean hasTrails;
    private boolean showsCenterOfGravity;
    private double skip;
    private double speed;


    // Constructor that builds a Settings object.
    public SimulationSettings(Particle[] particles, boolean isInfinite, boolean hasTrails, boolean showsCenterOfGravity, double skip, double speed) {
        this.particles = particles;
        this.isInfinite = isInfinite;
        this.hasTrails = hasTrails;
        this.showsCenterOfGravity = showsCenterOfGravity;
        this.skip = skip;
        this.speed = speed;
    }
}
