package stl.threebodysimulation;

// A class that wraps simulation settings
public class SimulationSettings {

    // Settings
    public Particle[] particles;
    public boolean isInfinite;
    public boolean hasTrails;
    public boolean showsCenterOfGravity;
    public double skip;
    public double speed;


    // Constructor that builds a Settings object.
    public SimulationSettings(Particle[] particles, boolean isInfinite, boolean hasTrails, boolean showsCenterOfGravity, double skip, double speed) {
        this.particles = particles;
        this.isInfinite = isInfinite;
        this.hasTrails = hasTrails;
        this.showsCenterOfGravity = showsCenterOfGravity;
        this.skip = skip;
        this.speed = speed;
    }

    public double[] returnMass() {
        return new double[] {particles[0].mass, particles[1].mass, particles[2].mass};
    }
}
