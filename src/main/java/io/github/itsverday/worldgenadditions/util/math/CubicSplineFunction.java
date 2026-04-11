package io.github.itsverday.worldgenadditions.util.math;

import com.hypixel.hytale.builtin.hytalegenerator.ArrayUtil;
import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class CubicSplineFunction implements Function<Double, Double>, Double2DoubleFunction {
    private static final double FALLBACK_VALUE = 0.0;

    @Nonnull
    private final List<CubicSplinePoint> points = new ArrayList<>(2);
    @Nonnull
    private final List<CubicSplineRange> ranges = new ArrayList<>(2);

    private CubicSplinePoint first = null;
    private CubicSplinePoint last = null;

    public Double apply(@Nonnull Double input) {
        return get(input);
    }

    public double get(double input) {
        if (Double.isNaN(input)) return FALLBACK_VALUE;
        if (first == null || last == null) return FALLBACK_VALUE;
        if (points.size() == 1 || input <= first.getX()) return first.computeLinear(input);
        if (input >= last.getX()) return last.computeLinear(input);

        int rangeIndex = rangeIndex(input);
        return ranges.get(rangeIndex).compute(input);
    }

    @Nonnull
    public CubicSplineFunction addPoint(CubicSplinePoint point) {
        for (CubicSplinePoint otherPoint: points) {
            if (point.getX() == otherPoint.getX()) return this;
        }

        points.add(point);
        points.sort(Comparator.comparingDouble(CubicSplinePoint::getX));
        first = points.getFirst();
        last = points.getLast();
        initializeRanges();

        return this;
    }

    public boolean contains(double x) {
        return points.parallelStream().anyMatch(point -> point.getX() == x);
    }

    private void initializeRanges() {
        ranges.clear();

        for (int i = 0; i < points.size() - 1; i++) {
            ranges.add(new CubicSplineRange(points.get(i), points.get(i + 1)));
        }
    }

    private int rangeIndex(double x) {
        return ArrayUtil.sortedSearch(ranges, x, (gauge, range) -> {
            if (gauge < range.getX0()) return -1;
            return gauge >= range.getX1() ? 1 : 0;
        });
    }
}
