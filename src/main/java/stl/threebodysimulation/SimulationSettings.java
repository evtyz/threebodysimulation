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
     * The name of the CSV file that information is saved to.
     */
    private String CSVFileName;


    /**
     * Basic constructor that creates a SimulationSettings object.
     *
     * @param particles            Particle array of particles to be simulated.
     * @param isInfinite           Whether the simulation is continuous.
     * @param hasTrails            Whether the simulation shows particle trails.
     * @param showsCenterOfGravity Whether the simulation shows center of gravity.
     * @param skip                 The time the simulation skips to.
     * @param speed                The speed multiplier of the simulation.
     * @param numberFormat         The NumberFormat of the simulation info display.
     * @param CSVFileName          The file name of the CSV where simulation stats are saved to. If saving is disabled, this is an empty string.
     */
    SimulationSettings(Particle[] particles, boolean isInfinite, boolean hasTrails, boolean showsCenterOfGravity, double skip, double speed, NumberFormat numberFormat, String CSVFileName) {
        this.numberFormat = numberFormat;
        this.particles = particles;
        this.isInfinite = isInfinite;
        this.hasTrails = hasTrails;
        this.showsCenterOfGravity = showsCenterOfGravity;
        this.skip = skip;
        this.speed = speed;
        this.CSVFileName = CSVFileName;
    }

    /**
     * Gets whether the simulation is infinite.
     *
     * @return True if infinite, false if not.
     */
    boolean getInfinite() {
        return isInfinite;
    }

    /**
     * Gets whether the simulation has trails.
     *
     * @return True if it has trails, false if not.
     */
    boolean getTrails() {
        return hasTrails;
    }

    /**
     * Gets whether the simulation shows center of gravity.
     *
     * @return True if it shows, false if not.
     */
    boolean getCenterOfGravity() {
        return showsCenterOfGravity;
    }

    /**
     * Gets the time that the simulation skips to.
     *
     * @return The time that the simulation skips to.
     */
    double getSkip() {
        return skip;
    }

    /**
     * Gets the speed that the simulation runs at.
     *
     * @return The speed that the simulation runs at.
     */
    double getSpeed() {
        return speed;
    }

    /**
     * Gets the mass of the particles in the simulation.
     *
     * @return The mass of the particles in the simulation, in an array of 3 doubles.
     */
    double[] getMass() {
        return new double[]{particles[0].getMass(), particles[1].getMass(), particles[2].getMass()};
    }

    /**
     * Gets the NumberFormat that the user has chosen for the simulation.
     *
     * @return The NumberFormat chosen by the user.
     */
    NumberFormat getNumberFormat() {
        return numberFormat;
    }

    /**
     * Gets the particles involved in the simulation.
     *
     * @return The particles, in an array of 3, involved in the simulation.
     */
    Particle[] getParticles() {
        return particles;
    }

    /**
     * Gets the CSV file name that simulation stats are saved to.
     *
     * @return The filename of the CSV where stats are saved to.
     */
    String getCSVFileName() {
        return CSVFileName;
    }
}
