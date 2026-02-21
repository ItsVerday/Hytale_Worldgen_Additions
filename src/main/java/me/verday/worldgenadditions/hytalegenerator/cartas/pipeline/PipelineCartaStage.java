package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline;

import com.hypixel.hytale.math.vector.Vector2i;
import me.verday.worldgenadditions.util.ModuloVector2iCache;
import me.verday.worldgenadditions.util.WorkerIndexerData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class PipelineCartaStage<R> {
    private PipelineCartaStage<R> previousStage = null;

    private final PipelineCartaTransform<R> root;
    private final boolean skip;

    private final WorkerIndexerData<ModuloVector2iCache<R>> cache;

    public PipelineCartaStage(PipelineCartaTransform<R> root, boolean skip) {
        this.root = root;
        this.skip = skip;

        this.cache = new WorkerIndexerData<>(() -> new ModuloVector2iCache<>(6));
    }

    public void setPreviousStage(PipelineCartaStage<R> previousStage) {
        this.previousStage = previousStage;
    }

    @Nullable
    public R process(@Nonnull PipelineCartaTransform.Context<R> context) {
        context = context.withStage(this);
        ModuloVector2iCache<R> workerCache = cache.get(context.workerId);
        Vector2i position = context.getIntPosition();
        if (!workerCache.containsKey(position)) {
            R value = root.process(context);
            if (context.fallthrough && value == null) value = processPrevious(context);
            workerCache.put(position, value);
        }

        return workerCache.get(position);
    }

    @Nullable
    public R processPrevious(@Nonnull PipelineCartaTransform.Context<R> context) {
        return previousStage.process(context);
    }

    public List<R> allPossibleValues() {
        return root.allPossibleValues();
    }

    public boolean isSkipped() {
        return skip;
    }
}
