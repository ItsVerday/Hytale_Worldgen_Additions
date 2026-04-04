package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.HashMap;

public class SmoothingPipelineCartaTransform extends AbstractContextModificationPipelineCartaTransform {
    private final double radius;
    private final double threshold;

    public SmoothingPipelineCartaTransform(PipelineCartaTransform child, double radius, double threshold) {
        super(child);
        this.radius = radius;
        this.threshold = threshold;
    }

    @Override
    public int process(@NonNullDecl ContextStack stack) {
        int radiusInt = (int) Math.ceil(radius);

        HashMap<Integer, Integer> counts = new HashMap<>();
        int totalCount = 0;
        int highestCount = 0;
        double totalCountEstimate = (radiusInt * 2 + 1) * (radiusInt * 2 + 1) * 0.79;

        for (int dx = -radiusInt; dx <= radiusInt; dx++) {
            for (int dz = -radiusInt; dz <= radiusInt; dz++) {
                if (dx * dx + dz * dz > radius * radius) continue;

                stack.pushWithOffset(dx, dz);
                int value = processChild(stack);
                stack.pop();

                if (value == -1) continue;

                int currentCount = 1;
                Integer count = counts.get(value);
                if (count != null) {
                    currentCount = count + 1;
                    if (currentCount >= totalCountEstimate * threshold) return value;
                }

                counts.put(value, currentCount);

                if (currentCount > highestCount) highestCount = currentCount;
                totalCount++;
            }
        }

        for (Integer value: counts.keySet()) {
            int count = counts.get(value);
            if (count == highestCount && count >= totalCount * threshold) return value;
        }

        return processChild(stack);
    }
}
