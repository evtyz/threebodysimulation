package stl.threebodysimulation;

/**
 * A class that packages simulation settings
 */
class SimulationSettings {

    /**
     * Particle array to be simulated.
     */
    private Particle[] particles;

    /**
     * Whether the simulation runs continuously.
     */
    private boolean isInfinite;

    /**
     * Whether the simulation shows trails.
     */
    private boolean hasTrails;

    /**
     * Whether the simulation shows the center of gravity/mass.
     */
    private boolean showsCenterOfGravity;

    /**
     * The amount of time that the simulation skips.
     */
    private double skip;

    /**
     * The simulation speed multiplier.
     */
    private double speed;

    /**
     * The format of numerical information display.
     */
    private NumberFormat numberFormat;


    /**
     * Basic constructor that creates a SimulationSettings object.
     * @param particles Particle array of particles to be simulated.
     * @param isInfinite Whether the simulation is continuous.
     * @param hasTrails Whether the simulation shows particle trails.
     * @param showsCenterOfGravity Whether the simulation shows center of gravity.
     * @param skip // The time the simulation skips to.
     * @param speed // The speed multiplier of the simulation.
     * @param numberFormat // The NumberFormat of the simulation info display.
     */
    SimulationSettings(Particle[] particles, boolean isInfinite, boolean hasTrails, boolean showsCenterOfGravity, double skip, double speed, NumberFormat numberFormat) {
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

    NumberFormat getNumberFormat() {
        return numberFormat;
    }

    Particle[] getParticles() {
        return particles;
    }
}
