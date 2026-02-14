package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline;

import com.hypixel.hytale.math.vector.Vector2i;
import me.verday.worldgenadditions.hytalegenerator.cartas.PipelineCarta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PipelineCartaStage {
    private PipelineCarta carta = null;
    private PipelineCartaStage previousStage = null;
    private int stageIndex;

    private final PipelineCartaTransform root;
    private final boolean skip;

    private final ConcurrentHashMap<Vector2i, String> biomeCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ConcurrentHashMap<Vector2i, Integer>> biomeDistanceCache = new ConcurrentHashMap<>();

    public PipelineCartaStage(PipelineCartaTransform root, boolean skip) {
        this.root = root;
        this.skip = skip;
    }

    public void setCarta(PipelineCarta carta) {
        this.carta = carta;
    }

    public void setStageIndex(int stageIndex) {
        this.stageIndex = stageIndex;
    }

    public PipelineCartaStage getPrevious() {
        if (previousStage == null) previousStage = carta.getPreviousStage(stageIndex);
        return previousStage;
    }

    public int queryBiomeDistanceSquared(@Nonnull PipelineCartaTransform.Context ctx, String biomeId) {
        if (!biomeDistanceCache.containsKey(biomeId)) biomeDistanceCache.put(biomeId, new ConcurrentHashMap<>());

        ConcurrentHashMap<Vector2i, Integer> thisBiomeDistanceCache = biomeDistanceCache.get(biomeId);
        if (thisBiomeDistanceCache.containsKey(ctx.position)) return thisBiomeDistanceCache.get(ctx.position);

        return calculateBiomeDistance(ctx, thisBiomeDistanceCache, biomeId, carta.getMaxPipelineBiomeDistance());
    }

    private int calculateBiomeDistance(@Nonnull PipelineCartaTransform.Context ctx, ConcurrentHashMap<Vector2i, Integer> cache, String biomeId, int maximumDistance) {
        String biomeHere = queryBiome(ctx);
        if (biomeId.equals(biomeHere)) {
            cache.put(ctx.position, 0);
            return 0;
        }

        int x = ctx.position.x;
        int z = ctx.position.y;

        for (int range = 1; range <= maximumDistance; range++) {
            boolean foundBiome = false;
            int foundDistance = Integer.MAX_VALUE;
            for (int dx = -range; dx <= range; dx++) {
                for (int dz = -range; dz <= range; dz += Math.abs(dx) == range ? 1 : range * 2) {
                    PipelineCartaTransform.Context newCtx = ctx.withPosition(x + dx, z + dz);
                    String biomeThere = queryBiome(newCtx);
                    if (biomeId.equals(biomeThere)) {
                        foundBiome = true;
                        int distance = distanceSquared(ctx.position, newCtx.position);
                        if (distance < foundDistance) foundDistance = distance;
                    }
                }
            }

            if (foundBiome) {
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
    public String queryBiome(@Nonnull PipelineCartaTransform.Context ctx) {
        String biomeId = process(ctx);
        if (biomeId != null) return biomeId;
        if (!ctx.fallthrough) return null;

        return ctx.queryBiome();
    }

    @Nullable
    public String process(@Nonnull PipelineCartaTransform.Context ctx) {
        if (!biomeCache.containsKey(ctx.position)) {
            String biomeId = root.process(ctx);
            if (biomeId == null) biomeId = "";
            biomeCache.put(ctx.position, biomeId);
        }

        String biomeId = biomeCache.get(ctx.position);
        if (biomeId.isEmpty()) return null;

        return biomeId;
    }

    public List<String> allPossibleValues() {
        return root.allPossibleValues();
    }

    public boolean isSkipped() {
        return skip;
    }

    public int getMaxPipelineBiomeDistance() {
        return root.getMaxPipelineBiomeDistance();
    }
}
