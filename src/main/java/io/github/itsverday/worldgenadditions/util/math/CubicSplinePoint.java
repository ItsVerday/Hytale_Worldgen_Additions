package io.github.itsverday.worldgenadditions.util.math;

public class CubicSplinePoint {
    private final double x;
    private final double y;
    private final double derivative;

    public CubicSplinePoint(double x, double y, double derivative) {
        this.x = x;
        this.y = y;
        this.derivative = derivative;
    }

    public double computeLinear(double input) {
        return (input - x) * derivative + y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDerivative() {
        return derivative;
    }
}
