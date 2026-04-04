package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline;

import com.hypixel.hytale.math.vector.Vector2i;
import io.github.itsverday.worldgenadditions.util.ModuloVector2iCache;
import io.github.itsverday.worldgenadditions.util.WorkerIndexerData;

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

        this.cache = new WorkerIndexerData<>(() -> new ModuloVector2iCache<>(8));
    }

    public void setPreviousStage(PipelineCartaStage<R> previousStage) {
        this.previousStage = previousStage;
    }

    @Nullable
    public R process(@Nonnull PipelineCartaTransform.ContextStack<R> stack) {
        stack.pushWithStage(this);
        ModuloVector2iCache<R> workerCache = cache.get(stack.getWorkerId());
        Vector2i position = stack.getIntPosition();
        int x = position.x;
        int y = position.y;
        if (!workerCache.containsKey(x, y)) {
            R value = root.process(stack);
            if (stack.isFallthrough() && value == null) value = processPrevious(stack);
            workerCache.put(x, y, value);
        }

        stack.pop();
        return workerCache.get(x, y);
    }

    @Nullable
    public R processPrevious(@Nonnull PipelineCartaTransform.ContextStack<R> stack) {
        return previousStage.process(stack);
    }

    public List<R> allPossibleValues() {
        return root.allPossibleValues();
    }

    public boolean isSkipped() {
        return skip;
    }
}
