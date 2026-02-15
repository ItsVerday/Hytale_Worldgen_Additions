package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline;

import com.hypixel.hytale.builtin.hytalegenerator.threadindexer.WorkerIndexer;
import com.hypixel.hytale.math.vector.Vector2i;
import me.verday.worldgenadditions.hytalegenerator.cartas.PipelineCarta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class PipelineCartaStage<R> {
    private PipelineCarta<R> carta = null;
    private PipelineCartaStage<R> previousStage = null;
    private int stageIndex;

    private final PipelineCartaTransform<R> root;
    private final boolean skip;

    private final WorkerIndexer.Data<ModuloVector2iCache<Optional<R>>> valueCache;
    private final WorkerIndexer.Data<HashMap<R, ModuloVector2iCache<Integer>>> valueDistanceCache;

    public PipelineCartaStage(PipelineCartaTransform<R> root, boolean skip, WorkerIndexer indexer) {
        this.root = root;
        this.skip = skip;

        valueCache = new WorkerIndexer.Data<>(indexer.getWorkerCount(), () -> new ModuloVector2iCache<>(6));
        valueDistanceCache = new WorkerIndexer.Data<>(indexer.getWorkerCount(), HashMap::new);
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
        HashMap<R, ModuloVector2iCache<Integer>> myValueDistanceCache = valueDistanceCache.get(ctx.workerId);
        if (!myValueDistanceCache.containsKey(value)) myValueDistanceCache.put(value, new ModuloVector2iCache<>(6));

        ModuloVector2iCache<Integer> thisValueDistanceCache = myValueDistanceCache.get(value);
        Vector2i position = ctx.getIntPosition();
        if (thisValueDistanceCache.containsKey(position)) return thisValueDistanceCache.get(position);

        return calculateValueDistance(ctx, thisValueDistanceCache, value, root.getMaxPipelineValueDistance());
    }

    private int calculateValueDistance(@Nonnull PipelineCartaTransform.Context<R> ctx, ModuloVector2iCache<Integer> cache, R value, int maximumDistance) {
        R valueHere = queryValue(ctx);
        Vector2i position = ctx.getIntPosition();
        if (value.equals(valueHere)) {
            cache.put(position, 0);
            return 0;
        }

        int x = position.x;
        int z = position.y;

        for (int range = 1; range <= maximumDistance; range++) {
            boolean foundValue = false;
            int foundDistance = Integer.MAX_VALUE;
            for (int dx = -range; dx <= range; dx++) {
                for (int dz = -range; dz <= range; dz += Math.abs(dx) == range ? 1 : range * 2) {
                    PipelineCartaTransform.Context<R> newCtx = ctx.withOffset(dx, dz);
                    R valueThere = queryValue(newCtx);
                    if (value.equals(valueThere)) {
                        foundValue = true;
                        int distance = distanceSquared(position, newCtx.getIntPosition());
                        if (distance < foundDistance) foundDistance = distance;
                    }
                }
            }

            if (foundValue) {
                cache.put(position, foundDistance);
                return foundDistance;
            }
        }

        cache.put(position, Integer.MAX_VALUE);
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
        ModuloVector2iCache<Optional<R>> myValueCache = valueCache.get(ctx.workerId);
        Vector2i position = ctx.getIntPosition();
        if (!myValueCache.containsKey(position)) {
            R value = root.process(ctx);
            myValueCache.put(position, Optional.ofNullable(value));
        }

        Optional<R> value = myValueCache.get(position);
        return value.orElse(null);
    }

    public List<R> allPossibleValues() {
        return root.allPossibleValues();
    }

    public boolean isSkipped() {
        return skip;
    }
}
