package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import com.hypixel.hytale.math.vector.Vector2d;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nullable;

public class RescalePipelineCartaTransform<R> extends AbstractContextModificationPipelineCartaTransform<R> {
    private final double scalingFactor;

    public RescalePipelineCartaTransform(@Nullable PipelineCartaTransform<R> child, double scalingFactor) {
        super(child);
        this.scalingFactor = scalingFactor;
    }

    @NullableDecl
    @Override
    public R process(@NonNullDecl ContextStack<R> stack) {
        Vector2d position = new Vector2d(stack.getPosition());
        position.scale(scalingFactor);
        stack.pushWithPosition(position);
        R value = processChild(stack);
        stack.pop();
        return value;
    }
}
