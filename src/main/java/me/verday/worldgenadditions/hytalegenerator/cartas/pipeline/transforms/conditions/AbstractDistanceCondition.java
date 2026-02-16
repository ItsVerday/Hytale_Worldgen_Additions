package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import com.hypixel.hytale.builtin.hytalegenerator.threadindexer.WorkerIndexer;
import com.hypixel.hytale.math.vector.Vector2i;
import me.verday.worldgenadditions.util.ModuloVector2iCache;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;

import javax.annotation.Nonnull;

public abstract class AbstractDistanceCondition<R> extends ConditionalPipelineCartaTransform.Condition<R> {
    @Nonnull
    private final ConditionalPipelineCartaTransform.Condition<R> child;
    private final WorkerIndexer.Data<ModuloVector2iCache<Integer>> distanceCache;

    public AbstractDistanceCondition(WorkerIndexer workerIndexer, @Nonnull ConditionalPipelineCartaTransform.Condition<R> child) {
        this.child = child;
        this.distanceCache = new WorkerIndexer.Data<>(workerIndexer.getWorkerCount(), () -> new ModuloVector2iCache<>(6));
    }

    public abstract double getDistanceToQuery(PipelineCartaTransform.Context<R> context);

    private double calculateValueDistance(@Nonnull PipelineCartaTransform.Context<R> context, double expectedDistance) {
        int maximumDistance = (int) Math.ceil(expectedDistance);
        ModuloVector2iCache<Integer> thisValueDistanceCache = distanceCache.get(context.workerId);
        Vector2i position = context.getIntPosition();
        if (thisValueDistanceCache.containsKey(position)) return thisValueDistanceCache.get(position);

        // Check if we are at a matching value
        if (child.process(context)) {
            thisValueDistanceCache.put(position, 0);
            return 0;
        }

        // Quickly find an upper bound on distance to matching value, if possible
        int distanceEstimate = maximumDistance;
        for (int d = 1; d < maximumDistance; d++) {
            if (child.process(context.withOffset(d, 0)) || child.process(context.withOffset(-d, 0)) || child.process(context.withOffset(0, d)) || child.process(context.withOffset(0, -d))) {
                if (d < expectedDistance) {
                    thisValueDistanceCache.put(position, d * d);
                    return d * d;
                }

                distanceEstimate = d;
                break;
            }
        }

        // More thorough check for matching values
        int foundDistance = Integer.MAX_VALUE;
        for (int range = 1; range <= distanceEstimate; range++) {
            for (int dx = -range; dx <= range; dx++) {
                for (int dz = -range; dz <= range; dz += Math.abs(dx) == range ? 1 : range * 2) {
                    int distance = dx * dx + dz * dz;
                    if (distance > distanceEstimate * distanceEstimate || distance > foundDistance) continue;
                    if (child.process(context.withOffset(dx, dz))) foundDistance = distance;
                }
            }

            if (foundDistance < Integer.MAX_VALUE && range * range >= 2 * foundDistance) {
                thisValueDistanceCache.put(position, foundDistance);
                return foundDistance;
            }
        }

        thisValueDistanceCache.put(position, Integer.MAX_VALUE);
        return Integer.MAX_VALUE;
    }

    private boolean withinDistance(@Nonnull PipelineCartaTransform.Context<R> context, double distance) {
        return calculateValueDistance(context, distance) <= distance * distance;
    }

    @Override
    public boolean process(PipelineCartaTransform.Context<R> context) {
        return withinDistance(context, getDistanceToQuery(context));
    }
}
