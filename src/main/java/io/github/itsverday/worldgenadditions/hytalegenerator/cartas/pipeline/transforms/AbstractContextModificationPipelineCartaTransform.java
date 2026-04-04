package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractContextModificationPipelineCartaTransform extends PipelineCartaTransform {
    @Nullable
    private final PipelineCartaTransform child;

    public AbstractContextModificationPipelineCartaTransform(@Nullable PipelineCartaTransform child) {
        this.child = child;
    }

    protected int processChild(@Nonnull ContextStack stack) {
        if (child != null) return child.process(stack);
        return stack.getStage().processPrevious(stack);
    }

    @Override
    public List<Integer> allPossibleValues() {
        if (child != null) return child.allPossibleValues();
        return List.of();
    }
}
