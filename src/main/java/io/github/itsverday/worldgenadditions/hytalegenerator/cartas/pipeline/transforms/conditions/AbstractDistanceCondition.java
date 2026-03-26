package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import com.hypixel.hytale.math.vector.Vector2i;
import io.github.itsverday.worldgenadditions.util.ModuloVector2iCache;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;
import io.github.itsverday.worldgenadditions.util.WorkerIndexerData;

import javax.annotation.Nonnull;

public abstract class AbstractDistanceCondition<R> extends ConditionalPipelineCartaTransform.Condition<R> {
    @Nonnull
    private final ConditionalPipelineCartaTransform.Condition<R> child;
    private final boolean fastMode;

    private final WorkerIndexerData<ModuloVector2iCache<Integer>> distanceCache;

    public AbstractDistanceCondition(@Nonnull ConditionalPipelineCartaTransform.Condition<R> child, boolean fastMode) {
        this.child = child;
        this.fastMode = fastMode;
        this.distanceCache = new WorkerIndexerData<>(() -> new ModuloVector2iCache<>(8));
    }

    public abstract double getDistanceToQuery(PipelineCartaTransform.ContextStack<R> stack);

    private boolean processChildWithOffset(@Nonnull PipelineCartaTransform.ContextStack<R> stack, double dx, double dz) {
        stack.pushWithOffset(dx, dz);
        boolean value = child.process(stack);
        stack.pop();
        return value;
    }

    private boolean withinDistance(@Nonnull PipelineCartaTransform.ContextStack<R> stack, double maxDistance) {
        ModuloVector2iCache<Integer> thisValueDistanceCache = distanceCache.get(stack.getWorkerId());
        Vector2i position = stack.getIntPosition();
        if (thisValueDistanceCache.containsKey(position)) return thisValueDistanceCache.get(position) <= maxDistance * maxDistance;

        // Check if we are at a matching value
        if (child.process(stack)) {
            thisValueDistanceCache.put(position, 0);
            return true;
        }

        // Quickly find an upper bound on distance to matching value in cardinal directions, if possible
        for (int d = 1; d <= maxDistance; d++) {
            if (processChildWithOffset(stack, d, 0) || processChildWithOffset(stack, -d, 0) || processChildWithOffset(stack, 0, d) || processChildWithOffset(stack, 0, -d)) {
                thisValueDistanceCache.put(position, d * d);
                return d <= maxDistance;
            }
        }

        // Check diagonals in the same way
        for (int d = 1; d * d * 2 <= maxDistance * maxDistance; d++) {
            if (processChildWithOffset(stack, d, d) || processChildWithOffset(stack, d, -d) || processChildWithOffset(stack, -d, d) || processChildWithOffset(stack, -d, -d)) {
                thisValueDistanceCache.put(position, d * d * 2);
                return d * d * 2 <= maxDistance * maxDistance;
            }
        }

        if (!fastMode) {
            int distanceEstimate = (int) Math.ceil(maxDistance);
            // More thorough check for matching values
            int foundDistance = Integer.MAX_VALUE;
            for (int range = 1; range <= distanceEstimate; range++) {
                for (int dx = -range; dx <= range; dx++) {
                    for (int dz = -range; dz <= range; dz += Math.abs(dx) == range ? 1 : range * 2) {
                        int distance = dx * dx + dz * dz;
                        if (distance > maxDistance * maxDistance || distance > foundDistance) continue;
                        if (processChildWithOffset(stack, dx, dz)) foundDistance = distance;
                    }
                }

                if (foundDistance < Integer.MAX_VALUE && range * range >= 2 * foundDistance) {
                    thisValueDistanceCache.put(position, foundDistance);
                    return foundDistance <= maxDistance * maxDistance;
                }
            }
        }

        thisValueDistanceCache.put(position, Integer.MAX_VALUE);
        return false;
    }

    @Override
    public boolean process(PipelineCartaTransform.ContextStack<R> stack) {
        return withinDistance(stack, getDistanceToQuery(stack));
    }
}
