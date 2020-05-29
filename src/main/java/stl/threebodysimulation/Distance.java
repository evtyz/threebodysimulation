package stl.threebodysimulation;

public class Distance {

    private double[] vectorDistance;
    private double absoluteDistance;
    private double[] directionalUnitVector;

    public Distance(double[] vector) {
        vectorDistance = vector;
        absoluteDistance = Math.sqrt(Math.pow(vector[0], 2) + Math.pow(vector[1], 2));
        directionalUnitVector = new double[] {vector[0] / absoluteDistance, vector[1] / absoluteDistance};
    }

    public double getAbsoluteDistance() {
        return absoluteDistance;
    }

    public double[] getDirectionalUnitVector() {
        return directionalUnitVector;
    }
}
