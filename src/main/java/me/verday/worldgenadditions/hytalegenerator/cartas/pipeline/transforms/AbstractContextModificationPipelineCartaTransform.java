package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractContextModificationPipelineCartaTransform<R> extends PipelineCartaTransform<R> {
    @Nullable
    private final PipelineCartaTransform<R> child;

    public AbstractContextModificationPipelineCartaTransform(@Nullable PipelineCartaTransform<R> child) {
        this.child = child;
    }

    public abstract Context<R> modifyChildContext(@Nonnull Context<R> context);

    @NullableDecl
    @Override
    public R process(@NonNullDecl Context<R> context) {
        Context<R> childContext = modifyChildContext(context);
        if (child != null) return child.process(childContext);
        return childContext.queryValue();
    }

    @Override
    public List<R> allPossibleValues() {
        if (child != null) return child.allPossibleValues();
        return List.of();
    }
}
