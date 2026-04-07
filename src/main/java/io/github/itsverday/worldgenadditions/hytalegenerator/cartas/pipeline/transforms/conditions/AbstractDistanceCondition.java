package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import com.hypixel.hytale.math.vector.Vector2d;
import io.github.itsverday.worldgenadditions.util.ModuloXZInt2BooleanCache;
import io.github.itsverday.worldgenadditions.util.ModuloXZInt2IntCache;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;

import javax.annotation.Nonnull;

public abstract class AbstractDistanceCondition extends ConditionalPipelineCartaTransform.Condition {
    @Nonnull
    private final ConditionalPipelineCartaTransform.Condition child;
    private final boolean fastMode;

    private final ModuloXZInt2IntCache distanceCache;
    private final ModuloXZInt2BooleanCache childCache;

    private final Vector2d rChildPosition;
    private final PipelineCartaTransform.Context rChildContext;

    public AbstractDistanceCondition(@Nonnull ConditionalPipelineCartaTransform.Condition child, boolean fastMode) {
        this.child = child;
        this.fastMode = fastMode;
        this.distanceCache = new ModuloXZInt2IntCache(8);
        this.childCache = new ModuloXZInt2BooleanCache(8);

        rChildPosition = new Vector2d();
        rChildContext = new PipelineCartaTransform.Context();
    }

    public abstract double getDistanceToQuery(PipelineCartaTransform.Context context);

    private boolean processChildWithOffset(@Nonnull PipelineCartaTransform.Context context, int dx, int dz) {
        rChildPosition.assign(context.position);
        rChildPosition.add(dx, dz);
        int x = (int) rChildPosition.x;
        int y = (int) rChildPosition.y;
        if (childCache.containsKey(x, y)) return childCache.get(x, y);

        rChildContext.assign(context);
        rChildContext.position = rChildPosition;
        boolean value = child.process(rChildContext);
        childCache.put(x, y, value);
        return value;
    }

    private boolean withinDistance(@Nonnull PipelineCartaTransform.Context context, double maxDistance) {
        int x = (int) context.position.x;
        int y = (int) context.position.y;
        if (distanceCache.containsKey(x, y)) return distanceCache.get(x, y) <= maxDistance * maxDistance;

        // Check if we are at a matching value
        if (child.process(context)) {
            distanceCache.put(x, y, 0);
            return true;
        }

        // Quickly find an upper bound on distance to matching value in cardinal directions, if possible
        for (int d = 1; d <= maxDistance; d++) {
            if (processChildWithOffset(context, d, 0) || processChildWithOffset(context, -d, 0) || processChildWithOffset(context, 0, d) || processChildWithOffset(context, 0, -d)) {
                distanceCache.put(x, y, d * d);
                return d <= maxDistance;
            }
        }

        // Check diagonals in the same way
        for (int d = 1; d * d * 2 <= maxDistance * maxDistance; d++) {
            if (processChildWithOffset(context, d, d) || processChildWithOffset(context, d, -d) || processChildWithOffset(context, -d, d) || processChildWithOffset(context, -d, -d)) {
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
                        if (processChildWithOffset(context, dx, dz)) foundDistance = distance;
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
    public boolean process(PipelineCartaTransform.Context context) {
        return withinDistance(context, getDistanceToQuery(context));
    }
}
