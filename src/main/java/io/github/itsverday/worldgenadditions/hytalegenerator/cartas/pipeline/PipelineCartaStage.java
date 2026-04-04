package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline;

import com.hypixel.hytale.math.vector.Vector2i;
import io.github.itsverday.worldgenadditions.util.ModuloXZIntCache;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class PipelineCartaStage {
    private PipelineCartaStage previousStage = null;

    private final PipelineCartaTransform root;
    private final boolean skip;

    private final ModuloXZIntCache<Integer> cache;

    public PipelineCartaStage(PipelineCartaTransform root, boolean skip) {
        this.root = root;
        this.skip = skip;

        this.cache = new ModuloXZIntCache<>(8);
    }

    public void setPreviousStage(PipelineCartaStage previousStage) {
        this.previousStage = previousStage;
    }

    public int process(@Nonnull PipelineCartaTransform.ContextStack stack) {
        stack.pushWithStage(this);
        Vector2i position = stack.getIntPosition();
        int x = position.x;
        int y = position.y;
        if (!cache.containsKey(x, y)) {
            int value = root.process(stack);
            if (stack.isFallthrough() && value == -1) value = processPrevious(stack);
            cache.put(x, y, value);
        }

        stack.pop();
        Integer value = cache.get(x, y);
        if (value == null) value = -1;
        return value;
    }

    public int processPrevious(@Nonnull PipelineCartaTransform.ContextStack stack) {
        return previousStage.process(stack);
    }

    public List<Integer> allPossibleValues() {
        return root.allPossibleValues();
    }

    public boolean isSkipped() {
        return skip;
    }
}
