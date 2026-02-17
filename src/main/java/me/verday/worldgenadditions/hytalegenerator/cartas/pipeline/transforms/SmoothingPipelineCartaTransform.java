package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.HashMap;

public class SmoothingPipelineCartaTransform<R> extends AbstractContextModificationPipelineCartaTransform<R> {
    private final double radius;
    private final double threshold;

    public SmoothingPipelineCartaTransform(PipelineCartaTransform<R> child, double radius, double threshold) {
        super(child);
        this.radius = radius;
        this.threshold = threshold;
    }

    @NullableDecl
    @Override
    public R process(@NonNullDecl Context<R> context) {
        int radiusInt = (int) Math.ceil(radius);

        HashMap<R, Integer> counts = new HashMap<>();
        int totalCount = 0;
        int highestCount = 0;
        int totalCountEstimate = (radiusInt * 2 + 1) * (radiusInt * 2 + 1);

        for (int dx = -radiusInt; dx <= radiusInt; dx++) {
            for (int dz = -radiusInt; dz <= radiusInt; dz++) {
                if (dx * dx + dz * dz > radius * radius) continue;

                R value = processChild(context.withOffset(dx, dz));
                int currentCount = 1;
                if (counts.containsKey(value)) currentCount = counts.get(value) + 1;
                counts.put(value, currentCount);
                if (currentCount > highestCount) highestCount = currentCount;
                totalCount++;
                // Optimization: If we have already passed the (estimated) threshold, just return early.
                if (currentCount >= totalCountEstimate * threshold) return value;
            }
        }

        for (R value: counts.keySet()) {
            int count = counts.get(value);
            if (count == highestCount && count >= totalCount * threshold) return value;
        }

        return null;
    }
}
