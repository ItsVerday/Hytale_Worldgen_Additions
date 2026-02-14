package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline;

import com.hypixel.hytale.math.vector.Vector2i;
import me.verday.worldgenadditions.hytalegenerator.cartas.PipelineCarta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PipelineCartaStage<R> {
    private PipelineCarta<R> carta = null;
    private PipelineCartaStage<R> previousStage = null;
    private int stageIndex;

    private final PipelineCartaTransform<R> root;
    private final boolean skip;

    private final ConcurrentHashMap<Vector2i, R> valueCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<R, ConcurrentHashMap<Vector2i, Integer>> valueDistanceCache = new ConcurrentHashMap<>();

    public PipelineCartaStage(PipelineCartaTransform<R> root, boolean skip) {
        this.root = root;
        this.skip = skip;
    }

    public void setCarta(PipelineCarta<R> carta) {
        this.carta = carta;
    }

    public void setStageIndex(int stageIndex) {
        this.stageIndex = stageIndex;
    }

    public PipelineCartaStage<R> getPrevious() {
        if (previousStage == null) previousStage = carta.getPreviousStage(stageIndex);
        return previousStage;
    }

    public int queryValueDistanceSquared(@Nonnull PipelineCartaTransform.Context<R> ctx, R value) {
        if (!valueDistanceCache.containsKey(value)) valueDistanceCache.put(value, new ConcurrentHashMap<>());

        ConcurrentHashMap<Vector2i, Integer> thisValueDistanceCache = valueDistanceCache.get(value);
        if (thisValueDistanceCache.containsKey(ctx.position)) return thisValueDistanceCache.get(ctx.position);

        return calculateValueDistance(ctx, thisValueDistanceCache, value, carta.getMaxPipelineValueDistance());
    }

    private int calculateValueDistance(@Nonnull PipelineCartaTransform.Context<R> ctx, ConcurrentHashMap<Vector2i, Integer> cache, R value, int maximumDistance) {
        R valueHere = queryValue(ctx);
        if (value.equals(valueHere)) {
            cache.put(ctx.position, 0);
            return 0;
        }

        int x = ctx.position.x;
        int z = ctx.position.y;

        for (int range = 1; range <= maximumDistance; range++) {
            boolean foundValue = false;
            int foundDistance = Integer.MAX_VALUE;
            for (int dx = -range; dx <= range; dx++) {
                for (int dz = -range; dz <= range; dz += Math.abs(dx) == range ? 1 : range * 2) {
                    PipelineCartaTransform.Context<R> newCtx = ctx.withPosition(x + dx, z + dz);
                    R valueThere = queryValue(newCtx);
                    if (value.equals(valueThere)) {
                        foundValue = true;
                        int distance = distanceSquared(ctx.position, newCtx.position);
                        if (distance < foundDistance) foundDistance = distance;
                    }
                }
            }

            if (foundValue) {
                cache.put(ctx.position, foundDistance);
                return foundDistance;
            }
        }

        cache.put(ctx.position, Integer.MAX_VALUE);
        return Integer.MAX_VALUE;
    }

    private int distanceSquared(Vector2i a, Vector2i b) {
        int dx = a.x - b.x;
        int dy = a.y - b.y;
        return dx * dx + dy * dy;
    }

    @Nullable
    public R queryValue(@Nonnull PipelineCartaTransform.Context<R> ctx) {
        R value = process(ctx);
        if (value != null) return value;
        if (!ctx.fallthrough) return null;

        return ctx.queryValue();
    }

    @Nullable
    public R process(@Nonnull PipelineCartaTransform.Context<R> ctx) {
        if (!valueCache.containsKey(ctx.position)) {
            R value = root.process(ctx);
            valueCache.put(ctx.position, value);
        }

        return valueCache.get(ctx.position);
    }

    public List<R> allPossibleValues() {
        return root.allPossibleValues();
    }

    public boolean isSkipped() {
        return skip;
    }

    public int getMaxPipelineValueDistance() {
        return root.getMaxPipelineValueDistance();
    }
}
