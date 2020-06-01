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
    public NumberFormat numberFormat;


    // Constructor that builds a Settings object.
    public SimulationSettings(Particle[] particles, boolean isInfinite, boolean hasTrails, boolean showsCenterOfGravity, double skip, double speed, NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
        this.particles = particles;
        this.isInfinite = isInfinite;
        this.hasTrails = hasTrails;
        this.showsCenterOfGravity = showsCenterOfGravity;
        this.skip = skip;
        this.speed = speed;
    }

    boolean getInfinite() {
        return isInfinite;
    }

    boolean getTrails() {
        return hasTrails;
    }

    boolean getCenterOfGravity() {
        return showsCenterOfGravity;
    }

    // Returns the masses of all three particles in an array
    public double[] getMass() {
        return new double[]{particles[0].getMass(), particles[1].getMass(), particles[2].getMass()};
    }
}
