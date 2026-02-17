package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline;

import com.hypixel.hytale.builtin.hytalegenerator.threadindexer.WorkerIndexer;
import com.hypixel.hytale.math.vector.Vector2i;
import me.verday.worldgenadditions.hytalegenerator.cartas.PipelineCarta;
import me.verday.worldgenadditions.util.ModuloVector2iCache;
import me.verday.worldgenadditions.util.WorkerIndexerData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class PipelineCartaStage<R> {
    private PipelineCarta<R> carta = null;
    private PipelineCartaStage<R> previousStage = null;
    private int stageIndex;

    private final PipelineCartaTransform<R> root;
    private final boolean skip;

    private final WorkerIndexerData<ModuloVector2iCache<Optional<R>>> valueCache;

    public PipelineCartaStage(PipelineCartaTransform<R> root, boolean skip) {
        this.root = root;
        this.skip = skip;

        valueCache = new WorkerIndexerData<>(() -> new ModuloVector2iCache<>(8));
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

    @Nullable
    public R queryValue(@Nonnull PipelineCartaTransform.Context<R> context) {
        R value = process(context);
        if (value != null) return value;
        if (!context.fallthrough) return null;

        return context.queryValue();
    }

    @Nullable
    public R process(@Nonnull PipelineCartaTransform.Context<R> context) {
        ModuloVector2iCache<Optional<R>> myValueCache = valueCache.get(context.workerId);
        Vector2i position = context.getIntPosition();
        if (!myValueCache.containsKey(position)) {
            R value = root.process(context);
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
