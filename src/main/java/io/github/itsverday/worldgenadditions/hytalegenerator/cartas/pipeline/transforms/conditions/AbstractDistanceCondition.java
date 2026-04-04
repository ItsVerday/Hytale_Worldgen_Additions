package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import com.hypixel.hytale.math.vector.Vector2d;
import com.hypixel.hytale.math.vector.Vector2i;
import io.github.itsverday.worldgenadditions.util.ModuloVector2iCache;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;

import javax.annotation.Nonnull;

public abstract class AbstractDistanceCondition<R> extends ConditionalPipelineCartaTransform.Condition<R> {
    @Nonnull
    private final ConditionalPipelineCartaTransform.Condition<R> child;
    private final boolean fastMode;

    private final ModuloVector2iCache<Integer> distanceCache;
    private final ModuloVector2iCache<Boolean> childCache;

    public AbstractDistanceCondition(@Nonnull ConditionalPipelineCartaTransform.Condition<R> child, boolean fastMode) {
        this.child = child;
        this.fastMode = fastMode;
        this.distanceCache = new ModuloVector2iCache<>(8);
        this.childCache = new ModuloVector2iCache<>(8);
    }

    public abstract double getDistanceToQuery(PipelineCartaTransform.ContextStack<R> stack);

    private boolean processChildWithOffset(@Nonnull PipelineCartaTransform.ContextStack<R> stack, double dx, double dz) {
        Vector2d position = stack.getPosition();
        int x = (int) (position.x + dx);
        int y = (int) (position.y + dz);
        if (childCache.containsKey(x, y)) return childCache.get(x, y);

        stack.pushWithOffset(dx, dz);
        boolean value = child.process(stack);
        stack.pop();
        childCache.put(x, y, value);
        return value;
    }

    private boolean withinDistance(@Nonnull PipelineCartaTransform.ContextStack<R> stack, double maxDistance) {
        Vector2i position = stack.getIntPosition();
        int x = position.x;
        int y = position.y;
        if (distanceCache.containsKey(x, y)) return distanceCache.get(x, y) <= maxDistance * maxDistance;

        // Check if we are at a matching value
        if (child.process(stack)) {
            distanceCache.put(x, y, 0);
            return true;
        }

        // Quickly find an upper bound on distance to matching value in cardinal directions, if possible
        for (int d = 1; d <= maxDistance; d++) {
            if (processChildWithOffset(stack, d, 0) || processChildWithOffset(stack, -d, 0) || processChildWithOffset(stack, 0, d) || processChildWithOffset(stack, 0, -d)) {
                distanceCache.put(x, y, d * d);
                return d <= maxDistance;
            }
        }

        // Check diagonals in the same way
        for (int d = 1; d * d * 2 <= maxDistance * maxDistance; d++) {
            if (processChildWithOffset(stack, d, d) || processChildWithOffset(stack, d, -d) || processChildWithOffset(stack, -d, d) || processChildWithOffset(stack, -d, -d)) {
                distanceCache.put(x, y, d * d * 2);
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
                    distanceCache.put(x, y, foundDistance);
                    return foundDistance <= maxDistance * maxDistance;
                }
            }
        }

        distanceCache.put(x, y, Integer.MAX_VALUE);
        return false;
    }

    @Override
    public boolean process(PipelineCartaTransform.ContextStack<R> stack) {
        return withinDistance(stack, getDistanceToQuery(stack));
    }
}
