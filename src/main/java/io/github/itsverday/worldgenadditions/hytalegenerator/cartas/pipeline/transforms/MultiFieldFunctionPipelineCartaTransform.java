package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.joml.Vector3d;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class MultiFieldFunctionPipelineCartaTransform extends PipelineCartaTransform {
    @Nonnull
    private final PipelineCartaTransform previous;
    @Nonnull
    private final List<Density> dimensions;
    private final int dimensionCount;
    @Nonnull
    private final List<Entry> entries;

    private final Vector3d rChildPosition;
    private final Density.Context rChildContext;
    private final double[] rPosition;

    public MultiFieldFunctionPipelineCartaTransform(PipelineCartaTransform previous, List<Density> dimensions, List<Entry> entries) {
        this.previous = previous;
        this.dimensions = dimensions;
        dimensionCount = dimensions.size();
        this.entries = entries;

        for (Entry entry: entries) {
            if (entry.center.length < dimensionCount) {
                double[] newCenter = new double[dimensionCount];
                for (int i = 0; i < dimensionCount; i++) {
                    if (entry.center.length > i) {
                        newCenter[i] = entry.center[i];
                    } else {
                        newCenter[i] = 0.0;
                    }
                }

                entry.center = newCenter;
            }
        }

        rChildPosition = new Vector3d();
        rChildContext = new Density.Context();
        rPosition = new double[dimensionCount];
    }

    @Override
    public int process(@NonNullDecl Context context) {
        rChildPosition.set(context.position.x, 0, context.position.y);
        rChildContext.position = rChildPosition;
        for (int i = 0; i < dimensionCount; i++) {
            rPosition[i] = dimensions.get(i).process(rChildContext);
        }

        double minimumDistanceSquared = Double.MAX_VALUE;
        PipelineCartaTransform minimumTransform = previous;

        for (Entry entry: entries) {
            double distanceSquared = entry.getDistanceSquared(rPosition);
            if (distanceSquared < minimumDistanceSquared) {
                minimumDistanceSquared = distanceSquared;
                minimumTransform = entry.value;
            }
        }

        return minimumTransform.process(context);
    }

    @Override
    public List<Integer> allPossibleValues() {
        ArrayList<Integer> values = new ArrayList<>();

        for (Entry entry: entries) {
            for (Integer possibility: entry.value.allPossibleValues()) {
                if (!values.contains(possibility)) {
                    values.add(possibility);
                }
            }
        }

        return values;
    }

    public static class Entry {
        PipelineCartaTransform value;
        double[] center;
        double maximumDistance;

        public Entry(@Nonnull PipelineCartaTransform value, double[] center, double maximumDistance) {
            this.value = value;
            this.center = center;
            this.maximumDistance = maximumDistance;
        }

        public double getDistanceSquared(double[] position) {
            double sum = 0.0;
            for (int index = 0; index < position.length; index++) {
                double delta = position[index] - center[index];
                sum += delta * delta;
            }

            if (sum > maximumDistance * maximumDistance && maximumDistance > 0.0) return Double.MAX_VALUE;
            return sum;
        }
    }
}
