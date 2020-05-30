package stl.threebodysimulation;

import java.util.HashMap;

public class ParticleRelationship {

    // TODO: Deal with precision and overflow issues, this number is too big.
    static final double G = 398575.0725; // Universal Gravitational Constant in km^3 * earthmasses^-1 * seconds^-2
    HashMap<Integer, double[]> accelerationMap = new HashMap<>();

    // This constructor creates a hashmap that denotes how much acceleration is provided to a particle by this relationship.
    public ParticleRelationship(double[] vector, double mass1, double mass2, int id1, int id2) {
        // INPUTS:
        // vector: double[], the vector distance between particle1 and particle2.
        // mass1: double, the mass of particle 1
        // mass2: double, the mass of particle 2
        // id1: int, the id of particle 1
        // id2: int, the id of particle 2
        // USAGE:
        // >> ParticleRelationship([3.0, 4.0], 1, 1, 1, 2)
        // >> Creates HashMap:
        // >> 1 -> [0.6, 0.8]
        // >> 2 -> [-0.6, -0.8]

        // Absolute value of vector
        double absoluteDistance = Math.sqrt(Math.pow(vector[0], 2) + Math.pow(vector[1], 2));

        double[] baseAcceleration;

        // Common factors of acceleration: vector / absVector^3
        if (absoluteDistance != 0) {
            baseAcceleration = new double[]{vector[0] / Math.pow(absoluteDistance, 3), vector[1] / Math.pow(absoluteDistance, 3)};
        } else {
            baseAcceleration = new double[]{0, 0};
        }


        // individual coefficients for each acceleration
        double massFactor1 = mass2 * G;
        double massFactor2 = -1 * mass1 * G;
        double[] acceleration1 = new double[2];
        double[] acceleration2 = new double[2];
        // Multiply together.
        for (int i = 0; i < 2; i++) {
            acceleration1[i] = massFactor1 * baseAcceleration[i];
            acceleration2[i] = massFactor2 * baseAcceleration[i];
        }

        // TODO: make this constructor more efficient.

        accelerationMap.put(id1, acceleration1);
        accelerationMap.put(id2, acceleration2);
    }
}
