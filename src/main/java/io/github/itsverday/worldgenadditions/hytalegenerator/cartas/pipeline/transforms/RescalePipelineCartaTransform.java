package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import com.hypixel.hytale.math.vector.Vector2d;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nullable;

public class RescalePipelineCartaTransform extends AbstractContextModificationPipelineCartaTransform {
    private final double scalingFactor;

    public RescalePipelineCartaTransform(@Nullable PipelineCartaTransform child, double scalingFactor) {
        super(child);
        this.scalingFactor = scalingFactor;
    }

    @Override
    public int process(@NonNullDecl ContextStack stack) {
        Vector2d position = new Vector2d(stack.getPosition());
        position.scale(scalingFactor);
        stack.pushWithPosition(position);
        int value = processChild(stack);
        stack.pop();
        return value;
    }
}
