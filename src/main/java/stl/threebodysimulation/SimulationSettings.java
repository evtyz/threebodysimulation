package stl.threebodysimulation;

import java.util.ArrayList;

/**
 * A class that packages simulation settings
 */
class SimulationSettings {

    /**
     * Particle array to be simulated.
     */
    private final Particle[] particles;

    /**
     * Whether the simulation runs continuously.
     */
    private final boolean isInfinite;

    /**
     * Whether the simulation shows trails.
     */
    private final boolean hasTrails;

    /**
     * Whether the simulation shows the center of gravity/mass.
     */
    private final boolean showsCenterOfGravity;

    /**
     * The amount of time that the simulation skips.
     */
    private final double skip;

    /**
     * The simulation speed multiplier.
     */
    private final double speed;

    /**
     * The format of numerical information display.
     */
    private final NumberFormat numberFormat;

    /**
     * The name of the CSV file that information is saved to.
     */
    private final String CSVFileName;


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
        this.isInfinite = isInfinite;
        this.hasTrails = hasTrails;
        this.showsCenterOfGravity = showsCenterOfGravity;
        this.skip = skip;
        this.speed = speed;
        this.numberFormat = numberFormat;
        this.CSVFileName = CSVFileName;
        this.particles = particles;
    }

    /**
     * A constructor that creates a SimulationSettings object from a serialized ArrayList of strings.
     *
     * @param serializedSettings The serialized form of the SimulationSettings.
     */
    SimulationSettings(ArrayList<String> serializedSettings) {
        int index = 0;

        this.isInfinite = serializedSettings.get(index++).equals("True");
        this.hasTrails = serializedSettings.get(index++).equals("True");
        this.showsCenterOfGravity = serializedSettings.get(index++).equals("True");
        this.skip = Double.parseDouble(serializedSettings.get(index++));
        this.speed = Double.parseDouble(serializedSettings.get(index++));
        this.numberFormat = parseNumberFormat(serializedSettings.get(index++));
        this.CSVFileName = serializedSettings.get(index++);
        this.particles = new Particle[3];

        for (int id = 1; id <= 3; id++) {
            ArrayList<String> serializedParticle = new ArrayList<>();
            for (int properties = 0; properties < 8; properties++) {
                serializedParticle.add(serializedSettings.get(index + properties));
            }
            index += 8;
            this.particles[id - 1] = new Particle(serializedParticle, id);
        }
    }

    /**
     * Returns the NumberFormat that corresponds with a string description, or null if none is found.
     *
     * @param serializedFormat The string description of the NumberFormat.
     * @return The NumberFormat that the description corresponds to, or null if none is found.
     */
    private static NumberFormat parseNumberFormat(String serializedFormat) {
        for (NumberFormat format : NumberFormat.values()) {
            if (format.toString().equals(serializedFormat)) {
                return format;
            }
        }
        return null; // Should never happen.
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

    /**
     * Converts the SimulationSettings object into a serialized ArrayList of strings that represent the settings' state.
     *
     * @return The ArrayList of strings that represent the settings' serialized form.
     */
    ArrayList<String> serialize() {
        ArrayList<String> serializedForm = new ArrayList<>();
        serializedForm.add(isInfinite ? "True" : "False");
        serializedForm.add(hasTrails ? "True" : "False");
        serializedForm.add(showsCenterOfGravity ? "True" : "False");
        serializedForm.add(String.valueOf(skip));
        serializedForm.add(String.valueOf(speed));
        serializedForm.add(numberFormat.toString());
        serializedForm.add(CSVFileName);
        for (Particle particle : particles) {
            serializedForm.addAll(particle.serialize());
        }
        return serializedForm;
    }
}
