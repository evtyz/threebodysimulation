package stl.threebodysimulation;

import javafx.scene.paint.Color;

import java.util.HashMap;

// This class represents all the attributes of a single particle object.
public class Particle {
    private final double mass;
    private final int id;
    private final Color color;
    // Attributes of a particle. Arrays represent vectors, with index 0 being x-coordinate and 1 being y-coordinate.
    private double[] position;
    private double[] velocity;
    private double[] acceleration;
    // Attributes packaged for sending to the InfoFXMLController.
    private HashMap<String, double[]> packagedInformation;

    private Listener infoUpdateListener;

    Particle(double xPos, double yPos, double xVel, double yVel, double mass, int id, Color color) {
        // Constructor for the Particle class.
        // INPUTS:
        // xPos: double, the x-component of the particle's position.
        // yPos: double, the y-component of the particle's position.
        // xVel: double, the x-component of the particle's velocity.
        // yVel: double, the y-component of the particle's velocity.
        // mass: double, the mass of the particle.
        // id: int, the id of the particle, one of {1, 2, 3}
        // color: Color, the color of the particle as a javafx Color object.

        // USAGE:
        // >> Particle(0, 0, -5, 3, 10, 1, Color.BLUE)
        // creates a blue particle that represents object 1 with position [0, 0], velocity [-5, 3], and mass 10.

        position = new double[]{xPos, yPos};
        velocity = new double[]{xVel, yVel};
        acceleration = new double[]{0, 0}; // users cannot provide starting acceleration to a particle.

        // Packages above vectors into a convenient hashmap for later use.
        packagedInformation = new HashMap<>();
        packagedInformation.put("position", position);
        packagedInformation.put("velocity", velocity);
        packagedInformation.put("acceleration", acceleration);

        this.mass = mass;
        this.id = id;
        this.color = color;
    }

    void setInfoUpdateListener(Listener listener) {
        infoUpdateListener = listener;
    }

    // Flattens a particle's position and velocity into a single array of 4 doubles.
    double[] flatten() {
        return new double[]{position[0], position[1], velocity[0], velocity[1]};
    }

    // Parses a flattened array into position and velocity, and then updates info
    void update (double[] flattenedParticle) {
        position[0] = flattenedParticle[0];
        position[1] = flattenedParticle[1];
        velocity[0] = flattenedParticle[2];
        velocity[1] = flattenedParticle[3];
        acceleration[0] = ParticleDiffEq.getAcceleration(id, 0);
        acceleration[1] = ParticleDiffEq.getAcceleration(id, 1);
        infoUpdateListener.onEvent();
    }

    double getMass() {
        return mass;
    }

    double[] getPosition() {
        return position;
    }

    Color getColor() {
        return color;
    }

    HashMap<String, double[]> getPackage() {
        return packagedInformation;
    }
}
