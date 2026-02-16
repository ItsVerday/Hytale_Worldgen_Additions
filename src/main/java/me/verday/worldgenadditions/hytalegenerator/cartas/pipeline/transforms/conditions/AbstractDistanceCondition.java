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
    private final int maximumDistance;

    public AbstractDistanceCondition(WorkerIndexer workerIndexer, @Nonnull ConditionalPipelineCartaTransform.Condition<R> child, int maximumDistance) {
        this.child = child;
        this.distanceCache = new WorkerIndexer.Data<>(workerIndexer.getWorkerCount(), () -> new ModuloVector2iCache<>(6));
        this.maximumDistance = maximumDistance;
    }

    public abstract double getDistanceToQuery(PipelineCartaTransform.Context<R> context);

    private int calculateValueDistance(@Nonnull PipelineCartaTransform.Context<R> ctx) {
        ModuloVector2iCache<Integer> thisValueDistanceCache = distanceCache.get(ctx.workerId);
        Vector2i position = ctx.getIntPosition();
        if (thisValueDistanceCache.containsKey(position)) return thisValueDistanceCache.get(position);
        if (child.process(ctx)) {
            thisValueDistanceCache.put(position, 0);
            return 0;
        }

        int foundDistance = Integer.MAX_VALUE;
        for (int range = 1; range <= maximumDistance; range++) {
            for (int dx = -range; dx <= range; dx++) {
                for (int dz = -range; dz <= range; dz += Math.abs(dx) == range ? 1 : range * 2) {
                    if (dx * dx + dz * dz > maximumDistance * maximumDistance) continue;
                    PipelineCartaTransform.Context<R> newCtx = ctx.withOffset(dx, dz);
                    if (child.process(newCtx)) {
                        int distance = distanceSquared(position, newCtx.getIntPosition());
                        if (distance < foundDistance) foundDistance = distance;
                    }
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

    private int distanceSquared(Vector2i a, Vector2i b) {
        int dx = a.x - b.x;
        int dy = a.y - b.y;
        return dx * dx + dy * dy;
    }

    @Override
    public boolean process(PipelineCartaTransform.Context<R> context) {
        double distance = getDistanceToQuery(context);
        return calculateValueDistance(context) <= distance * distance;
    }

    @Override
    public int getMaxPipelineBiomeDistance() {
        return maximumDistance;
    }
}
