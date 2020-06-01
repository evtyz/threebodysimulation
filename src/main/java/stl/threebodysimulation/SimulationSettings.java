package stl.threebodysimulation;

// A class that wraps simulation settings
public class SimulationSettings {

    // Settings
    Particle[] particles;
    private boolean isInfinite;
    private boolean hasTrails;
    private boolean showsCenterOfGravity;
    private double skip;
    private double speed;
    private NumberFormat numberFormat;


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

    double getSkip() {
        return skip;
    }

    double getSpeed() {
        return speed;
    }

    // Returns the masses of all three particles in an array
    double[] getMass() {
        return new double[]{particles[0].getMass(), particles[1].getMass(), particles[2].getMass()};
    }
}
