package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.joml.Vector2d;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SmoothingPipelineCartaTransform extends PipelineCartaTransform {
    @Nonnull
    private final PipelineCartaTransform previous;
    @Nonnull
    private final PipelineCartaTransform child;

    private final double radius;
    private final double threshold;
    private double totalThreshold = Double.MAX_VALUE;

    private final Vector2d rChildPosition;
    private final Context rChildContext;
    private final HashMap<Integer, Integer> rCounts = new HashMap<>();

    public SmoothingPipelineCartaTransform(@Nonnull PipelineCartaTransform previous, @Nonnull PipelineCartaTransform child, double radius, double threshold) {
        this.previous = previous;
        this.child = child;
        this.radius = radius;
        this.threshold = threshold;

        rChildPosition = new Vector2d();
        rChildContext = new Context();
    }

    @Override
    public int process(@NonNullDecl Context context) {
        int radiusInt = (int) Math.ceil(radius);

        int totalCount = 0;
        int highestCount = 0;

        rCounts.clear();
        for (int dx = -radiusInt; dx <= radiusInt; dx++) {
            for (int dz = -radiusInt; dz <= radiusInt; dz++) {
                if (dx * dx + dz * dz > radius * radius) continue;

                rChildPosition.set(context.position);
                rChildPosition.add(dx, dz);
                rChildContext.assign(context);
                rChildContext.position = rChildPosition;
                int value = child.process(rChildContext);

                int currentCount = 1;
                Integer count = rCounts.get(value);
                if (count != null) {
                    currentCount = count + 1;
                    if (currentCount >= totalThreshold) return value;
                }

                rCounts.put(value, currentCount);

                if (currentCount > highestCount) highestCount = currentCount;
                totalCount++;
            }
        }

        if (totalThreshold == Double.MAX_VALUE) totalThreshold = totalCount * threshold;

        for (Integer value: rCounts.keySet()) {
            int count = rCounts.get(value);
            if (count == highestCount && count >= totalThreshold) return value;
        }

        return previous.process(context);
    }

    @Override
    public List<Integer> allPossibleValues() {
        ArrayList<Integer> values = new ArrayList<>();

        for (Integer possibility: previous.allPossibleValues()) {
            if (!values.contains(possibility)) {
                values.add(possibility);
            }
        }

        for (Integer possibility: child.allPossibleValues()) {
            if (!values.contains(possibility)) {
                values.add(possibility);
            }
        }

        return values;
    }
}
