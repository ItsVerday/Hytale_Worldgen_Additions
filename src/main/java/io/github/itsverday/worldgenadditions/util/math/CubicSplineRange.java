package io.github.itsverday.worldgenadditions.util.math;

public class CubicSplineRange {
    private final double x0;
    private final double x1;
    private final double y0;
    private final double y1;
    private final double derivative0;
    private final double derivative1;

    private final double xScale;
    private final double b;
    private final double c;
    private final double d;

    public CubicSplineRange(CubicSplinePoint start, CubicSplinePoint end) {
        x0 = start.getX();
        x1 = end.getX();
        y0 = start.getY();
        y1 = end.getY();
        derivative0 = start.getDerivative();
        derivative1 = end.getDerivative();

        double xDelta = x1 - x0;
        double scaledDerivative0 = derivative0 * xDelta;
        double scaledDerivative1 = derivative1 * xDelta;

        // a = y0
        b = scaledDerivative0;
        c = -3.0 * y0 + 3.0 * y1 - 2.0 * scaledDerivative0 - scaledDerivative1;
        d = 2.0 * y0 - 2.0 * y1 + scaledDerivative0 + scaledDerivative1;
        xScale = 1 / xDelta;
    }

    public double compute(double x) {
        x -= x0;
        x *= xScale;

        double x2 = x * x;
        double x3 = x2 * x;
        return d * x3 + c * x2 + b * x + y0;
    }

    public double getX0() {
        return x0;
    }

    public double getX1() {
        return x1;
    }
}
