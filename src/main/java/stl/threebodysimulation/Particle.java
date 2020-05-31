package stl.threebodysimulation;

import javafx.scene.paint.Color;

import java.util.HashMap;

// This class represents all the attributes of a single particle object.
public class Particle {
    final double mass;
    final int id;
    final Color color;
    // Attributes of a particle. Arrays represent vectors, with index 0 being x-coordinate and 1 being y-coordinate.
    double[] position;
    double[] velocity;
    double[] acceleration;
    // Attributes packaged for sending to the InfoFXMLController.
    HashMap<String, double[]> packagedInformation;

    Listener infoUpdateListener;

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

    // Flattens a particle's position and velocity into a single array of 4 doubles.
    public double[] flatten() {
        return new double[]{position[0], position[1], velocity[0], velocity[1]};
    }

    // Parses a flattened array into position and velocity, and then updates info
    public void updateFromFlattenedParticle(double[] flattenedParticle) {
        position[0] = flattenedParticle[0];
        position[1] = flattenedParticle[1];
        velocity[0] = flattenedParticle[2];
        velocity[1] = flattenedParticle[3];
        infoUpdateListener.onEvent();
    }

    // Updates acceleration according to the accelerationStorage variable in the differential equations class.
    public void updateAcceleration() {
        acceleration[0] = ParticleDiffEq.accelerationStorage[id - 1][0];
        acceleration[1] = ParticleDiffEq.accelerationStorage[id - 1][1];
        infoUpdateListener.onEvent();
    }
}
