package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractContextModificationPipelineCartaTransform<R> extends PipelineCartaTransform<R> {
    @Nullable
    private final PipelineCartaTransform<R> child;

    public AbstractContextModificationPipelineCartaTransform(@Nullable PipelineCartaTransform<R> child) {
        this.child = child;
    }

    protected R processChild(@Nonnull Context<R> childContext) {
        if (child != null) return child.process(childContext);
        return childContext.queryValue();
    }

    @Override
    public List<R> allPossibleValues() {
        if (child != null) return child.allPossibleValues();
        return List.of();
    }
}
