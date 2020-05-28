package stl.threebodysimulation;

import javafx.scene.paint.Color;

import java.util.HashMap;

public class Particle {
    // Attributes of a particle. Arrays represent vectors, with index 0 being x-coordinate and 1 being y-coordinate.
    int[] position;
    int[] velocity;
    int[] acceleration;
    final int mass;
    final int id;
    final Color color;

    // Attributes packaged for sending to the InfoFXMLController.
    HashMap<String, int[]> packagedInformation;

    Particle(int xPos, int yPos, int xVel, int yVel, int mass, int id, Color color) {
        // Constructor for the Particle class.
        // INPUTS:
        // xPos: int, the x-component of the particle's position.
        // yPos: int, the y-component of the particle's position.
        // xVel: int, the x-component of the particle's velocity.
        // yVel: int, the y-component of the particle's velocity.
        // mass: int, the mass of the particle.
        // id: int, the id of the particle, one of {1, 2, 3}
        // color: Color, the color of the particle as a javafx Color object.

        // USAGE:
        // >> Particle(0, 0, -5, 3, 10, 1, Color.BLUE)
        // creates a blue particle that represents object 1 with position [0, 0], velocity [-5, 3], and mass 10.

        position = new int[]{xPos, yPos};
        velocity = new int[]{xVel, yVel};
        acceleration = new int[]{0, 0}; // users cannot provide starting acceleration to a particle.

        // Packages above vectors into convenient hashmap for later use.
        packagedInformation = new HashMap<>();
        packagedInformation.put("position", position);
        packagedInformation.put("velocity", velocity);
        packagedInformation.put("acceleration", acceleration);

        this.mass = mass;
        this.id = id;
        this.color = color;
    }
}
