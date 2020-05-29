package stl.threebodysimulation;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

public abstract class ParticleDifferentialEquations implements FirstOrderDifferentialEquations {
    public int getDimension() {
        return 2;
    }

    public abstract void computeDerivatives(double t, double[] y, double[] yDot);
}
