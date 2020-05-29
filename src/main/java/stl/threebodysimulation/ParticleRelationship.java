package stl.threebodysimulation;

import java.util.HashMap;

public class ParticleRelationship {

    final double[] vectorDistance;
    final double absoluteDistance;
    final double massFactor1;
    final double massFactor2;
    final double[] acceleration1 = new double[2];
    final double[] acceleration2 = new double[2];

    HashMap<Integer, double[]> accelerationMap = new HashMap<>();

    public ParticleRelationship(double[] vector, double mass1, double mass2, int id1, int id2) {
        vectorDistance = vector;
        absoluteDistance = Math.sqrt(Math.pow(vector[0], 2) + Math.pow(vector[1], 2));
        double[] baseAcceleration = new double[] {vector[0] / Math.pow(absoluteDistance, 3), vector[1] / Math.pow(absoluteDistance, 3)};
        this.massFactor1 = mass2 * ParticleDiffEq.G;
        this.massFactor2 = -1 * mass1 * ParticleDiffEq.G;
        for (int i = 0; i < 2; i++) {
            acceleration1[i] = massFactor1 * baseAcceleration[i];
            acceleration2[i] = massFactor2 * baseAcceleration[i];
        }
        accelerationMap.put(id1, acceleration1);
        accelerationMap.put(id2, acceleration2);
    }
}
