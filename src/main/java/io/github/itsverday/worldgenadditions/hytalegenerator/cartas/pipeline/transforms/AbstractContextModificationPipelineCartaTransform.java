package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractContextModificationPipelineCartaTransform extends PipelineCartaTransform {
    @Nonnull
    private final PipelineCartaTransform child;

    public AbstractContextModificationPipelineCartaTransform(@Nonnull PipelineCartaTransform child) {
        this.child = child;
    }

    protected int processChild(@Nonnull ContextStack stack) {
        return child.process(stack);
    }

    @Override
    public List<Integer> allPossibleValues() {
        return child.allPossibleValues();
    }
}
