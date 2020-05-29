package stl.threebodysimulation;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

public class ParticleDiffEq implements FirstOrderDifferentialEquations {

    // TODO: Deal with precision and overflow issues, this number is too big.
    public static final double G = 132711917360.38; // Universal Gravitational Constant in km^3 * solarmasses^-1 * seconds^-2
    private final double[] masses;

    public ParticleDiffEq(double[] masses) {
        this.masses = masses;
    }

    public int getDimension() {
        return 3 * 2 * 2; // 3 particles * 2 dimensions * 2 derivatives (both displacement -> velocity and velocity -> acceleration)
    }

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

         Dot: Array of doubles, length 12, derivative of y:
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
        for (int i = 0; i < 3; i++) {
            yDot[4 * i] = y[4 * i + 2];
            yDot[4 * i + 1] = y[4 * i + 3];
        }

        ParticleRelationship[] relationships = new ParticleRelationship[] {
                new ParticleRelationship(new double[] {y[4] - y[0], y[5] - y[1]}, masses[0], masses[1], 0, 1),
                new ParticleRelationship(new double[] {y[8] - y[0], y[9] - y[1]}, masses[0], masses[2], 0, 2),
                new ParticleRelationship(new double[] {y[8] - y[4], y[9] - y[1]}, masses[1], masses[2], 1, 2)
        };

        double[][] accelerations = new double[3][2];

        for (ParticleRelationship relationship : relationships) {
            for (int particle : relationship.accelerationMap.keySet()) {
                double[] acceleration = relationship.accelerationMap.get(particle);
                accelerations[particle][0] += acceleration[0];
                accelerations[particle][1] += acceleration[1];
            }
        }

        for (int particle = 0; particle < 3; particle++) {
            yDot[4 * particle + 2] = accelerations[particle][0];
            yDot[4 * particle + 3] = accelerations[particle][1];
        }
    }
}
