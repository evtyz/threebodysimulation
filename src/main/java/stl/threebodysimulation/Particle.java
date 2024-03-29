package stl.threebodysimulation;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * This class represents a particle to be simulated.
 */
class Particle {
    /**
     * Mass, in earths.
     */
    private final double mass;

    /**
     * ID of particle, one of {1, 2, 3}
     */
    private final int id;

    /**
     * Color of particle.
     */
    private final Color color;

    /**
     * x and y components of particle's position.
     */
    private final double[] position;

    /**
     * x and y components of particle's velocity.
     */
    private final double[] velocity;

    /**
     * x and y components of particle's acceleration.
     */
    private double[] acceleration;

    /**
     * A hashmap that represents packaged displayable information from the particle.
     */
    private LinkedHashMap<String, double[]> packagedInformation;

    /**
     * A Listener object that is called when the particle's properties change.
     */
    private Listener infoUpdateListener;

    /**
     * Constructor for the particle class.
     *
     * @param xPos  X-position (km)
     * @param yPos  Y-position (km)
     * @param xVel  X-velocity (km/s)
     * @param yVel  Y-velocity (km/s)
     * @param mass  Mass (earths)
     * @param id    ID of particle
     * @param color color of particle
     */
    Particle(double xPos, double yPos, double xVel, double yVel, double mass, int id, Color color) {
        position = new double[]{xPos, yPos};
        velocity = new double[]{xVel, yVel};
        acceleration = new double[]{0, 0}; // users cannot provide starting acceleration to a particle.
        this.mass = mass;
        this.id = id;
        this.color = color;

        setupPackage();
    }

    /**
     * Constructor for a particle from a serialized String ArrayList.
     *
     * @param serializedParticle The particle in serialized form.
     * @param id                 The id of the particle.
     */
    Particle(ArrayList<String> serializedParticle, int id) {
        this.id = id;
        int index = 0;

        this.mass = Double.parseDouble(serializedParticle.get(index++));
        this.color = Color.color(
                Double.parseDouble(serializedParticle.get(index++)),
                Double.parseDouble(serializedParticle.get(index++)),
                Double.parseDouble(serializedParticle.get(index++)));
        this.position = new double[]{
                Double.parseDouble(serializedParticle.get(index++)),
                Double.parseDouble(serializedParticle.get(index++))};
        //noinspection UnusedAssignment
        this.velocity = new double[]{
                Double.parseDouble(serializedParticle.get(index++)),
                Double.parseDouble(serializedParticle.get(index++))};

        setupPackage();
    }

    /**
     * Sets up the packagedInformation hashmap.
     */
    void setupPackage() {
        // Packages above vectors into a convenient hashmap for later use.
        packagedInformation = new LinkedHashMap<>();
        packagedInformation.put("position", position);
        packagedInformation.put("velocity", velocity);
        packagedInformation.put("acceleration", acceleration);
    }

    /**
     * Sets a listener that is called when the particle updates.
     *
     * @param listener The listener to be called.
     */
    void setInfoUpdateListener(Listener listener) {
        infoUpdateListener = listener;
    }

    /**
     * Flattens a particle into an array.
     *
     * @return An array of 4 doubles that represent the {x-position, y-position, x-velocity, y-velocity}.
     */
    double[] flatten() {
        return new double[]{position[0], position[1], velocity[0], velocity[1]};
    }

    /**
     * Updates the particle according to a given input array, as well as a static acceleration array from ParticleDifferentialEquations.
     *
     * @param flattenedParticle The double array that represents {x-position, y-position, x-velocity, y-velocity}.
     */
    void update(double[] flattenedParticle) {
        position[0] = flattenedParticle[0];
        position[1] = flattenedParticle[1];
        velocity[0] = flattenedParticle[2];
        velocity[1] = flattenedParticle[3];
        acceleration[0] = ParticleDifferentialEquations.getAcceleration(id, 0);
        acceleration[1] = ParticleDifferentialEquations.getAcceleration(id, 1);
        infoUpdateListener.onEvent();
    }

    /**
     * Gets mass.
     *
     * @return The mass of the particle.
     */
    double getMass() {
        return mass;
    }

    /**
     * Gets position.
     *
     * @return The position of the particle.
     */
    double[] getPosition() {
        return position;
    }

    /**
     * Gets velocity.
     *
     * @return The velocity of the particle.
     */
    double[] getVelocity() {
        return velocity;
    }

    /**
     * Gets color.
     *
     * @return The color of the particle.
     */
    Color getColor() {
        return color;
    }

    /**
     * Gets all displayable information.
     *
     * @return A hashmap with position, velocity, and acceleration.
     */
    LinkedHashMap<String, double[]> getPackage() {
        return packagedInformation;
    }

    /**
     * Returns a serialized version of the particle.
     *
     * @return A serialized version of the particle, as an ArrayList of strings.
     */
    ArrayList<String> serialize() {
        ArrayList<String> serializedForm = new ArrayList<>();
        serializedForm.add(String.valueOf(mass));
        for (double colorValue : new double[]{color.getRed(), color.getGreen(), color.getBlue()}) {
            serializedForm.add(String.valueOf(colorValue));
        }
        for (double positionValue : position) {
            serializedForm.add(String.valueOf(positionValue));
        }
        for (double velocityValue : velocity) {
            serializedForm.add(String.valueOf(velocityValue));
        }
        return serializedForm;
    }
}
