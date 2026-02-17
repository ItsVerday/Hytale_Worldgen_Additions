package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nullable;
import java.util.List;

public class RescalePipelineCartaTransform<R> extends AbstractContextModificationPipelineCartaTransform<R> {
    private final double scalingFactor;

    public RescalePipelineCartaTransform(@Nullable PipelineCartaTransform<R> child, double scalingFactor) {
        super(child);
        this.scalingFactor = scalingFactor;
    }

    @Override
    public Context<R> modifyChildContext(@NonNullDecl Context<R> context) {
        Context<R> childContext = new Context<>(context);
        childContext.position.scale(scalingFactor);
        return childContext;
    }
}
