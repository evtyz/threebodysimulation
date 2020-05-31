package stl.threebodysimulation;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

// This class defines the mathematical relationships between three different masses
public class ParticleDiffEq implements FirstOrderDifferentialEquations {

    // Storage that shows the x- and y-accelerations of each particle, so other classes can read from it.
    static double[][] accelerationStorage;

    // TODO: Precision?
    // Universal Gravitational Constant in km^3 * earthmasses^-1 * seconds^-2
    static final double G = 398575.0725;

    // Array of masses for each particle
    private final double[] masses;

    // Constructor for class
    public ParticleDiffEq(double[] masses) {
        this.masses = masses;
        accelerationStorage = new double[3][2];
    }

    // The dimension of the differential equations.
    public int getDimension() {
        return 3 * 2 * 2; // 3 particles * 2 dimensions * 2 derivatives (both displacement -> velocity and velocity -> acceleration)
    }

    // Defines the differential equation.
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        /* INPUTS:
         t: time, int.

         y: Array of doubles, length 12:
        [X-pos of particle 1, Y-pos of particle 1,
         X-vel of particle 1, Y-vel of particle 1,
         X-pos of particle 2, Y-pos of particle 2,
         X-vel of particle 2, Y-vel of particle 2,
         X-pos of particle 3, Y-pos of particle 3,
         X-vel of particle 3, Y-vel of particle 3]

         yDot: Array of doubles, length 12, derivative of y:
        [X-vel of particle 1, Y-vel of particle 1,
         X-acc of particle 1, Y-acc of particle 1,
         X-vel of particle 2, Y-vel of particle 2,
         X-acc of particle 2, Y-acc of particle 2,
         X-vel of particle 3, Y-vel of particle 3,
         X-acc of particle 3, Y-acc of particle 3]
        */

        /* FUNCTION:
           Writes the derivative of y into yDot.
         */

        // Copy velocity for y to yDot in correct position.
        for (int particle = 0; particle < 3; particle++) {
            yDot[4 * particle] = y[4 * particle + 2];
            yDot[4 * particle + 1] = y[4 * particle + 3];
        }

        accelerationStorage = new double[3][2];

        addToAcceleration(new double[]{y[4] - y[0], y[5] - y[1]}, masses[0], masses[1], 0, 1);
        addToAcceleration(new double[]{y[8] - y[0], y[9] - y[1]}, masses[0], masses[2], 0, 2);
        addToAcceleration(new double[]{y[8] - y[4], y[9] - y[5]}, masses[1], masses[2], 1, 2);

        // Set yDot acceleration indices to correct value
        for (int particle = 0; particle < 3; particle++) {
            yDot[4 * particle + 2] = accelerationStorage[particle][0];
            yDot[4 * particle + 3] = accelerationStorage[particle][1];
        }
    }

    // This method calculates the pull that two particles have on one another and adds it to accelerationStorage
    private void addToAcceleration(double[] vector, double mass1, double mass2, int id1, int id2) {
        // INPUTS:
        // vector: double[], the vector distance between particle1 and particle2.
        // mass1: double, the mass of particle 1
        // mass2: double, the mass of particle 2
        // id1: int, the id of particle 1
        // id2: int, the id of particle 2
        // USAGE:
        // >> addToAcceleration([3.0, 4.0], 1, 1, 1, 2)
        // >> accelerationStorage[1] increments by [+0.6, +0.8]
        // >> accelerationStorage[2] increments by [-0.6, -0.8]

        // Absolute value of vector, according to pythagorean theorem
        double absoluteDistance = Math.sqrt(Math.pow(vector[0], 2) + Math.pow(vector[1], 2));

        // Common factors of acceleration: vector / absVector^3
        double[] baseAcceleration;
        if (absoluteDistance != 0) {
            baseAcceleration = new double[]{vector[0] / Math.pow(absoluteDistance, 3), vector[1] / Math.pow(absoluteDistance, 3)};
        } else {
            // obviously zero if there is no distance between two objects.
            baseAcceleration = new double[]{0, 0};
        }

        // individual coefficients for each acceleration
        double massFactor1 = mass2 * G;
        // Reversed for mass2, because vectors have direction
        double massFactor2 = -1 * mass1 * G;

        // Multiply together.
        for (int i = 0; i < 2; i++) {
            accelerationStorage[id1][i] += massFactor1 * baseAcceleration[i];
            accelerationStorage[id2][i] += massFactor2 * baseAcceleration[i];
        }
    }
}
