package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nullable;
import java.util.List;

public class RescalePipelineCartaTransform<R> extends PipelineCartaTransform<R> {
    @Nullable
    private final PipelineCartaTransform<R> child;
    private final double scalingFactor;

    public RescalePipelineCartaTransform(@Nullable PipelineCartaTransform<R> child, double scalingFactor) {
        this.child = child;
        this.scalingFactor = scalingFactor;
    }

    @NullableDecl
    @Override
    public R process(@NonNullDecl Context<R> ctx) {
        Context<R> childCtx = new Context<>(ctx);
        childCtx.position.scale(scalingFactor);

        if (child != null) return child.process(childCtx);
        return childCtx.queryValue();
    }

    @Override
    public List<R> allPossibleValues() {
        return List.of();
    }
}
