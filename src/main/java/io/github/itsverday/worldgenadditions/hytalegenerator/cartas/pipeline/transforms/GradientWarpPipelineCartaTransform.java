package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.math.vector.Vector2d;
import com.hypixel.hytale.math.vector.Vector3d;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GradientWarpPipelineCartaTransform extends AbstractContextModificationPipelineCartaTransform {
    @Nonnull
    private final Density warpField;
    private final double sampleDistance;
    private final double warpFactor;

    public GradientWarpPipelineCartaTransform(@Nullable PipelineCartaTransform child, @Nonnull Density warpField, double sampleDistance, double warpFactor) {
        super(child);
        this.warpField = warpField;
        this.sampleDistance = sampleDistance;
        this.warpFactor = warpFactor;
    }

    @Override
    public int process(@NonNullDecl ContextStack stack) {
        Density.Context densityContext = new Density.Context();
        Vector2d position = stack.getPosition();
        densityContext.position = new Vector3d(position.x, 0, position.y);

        double valueAtOrigin = warpField.process(densityContext);
        Density.Context densityChildContext = new Density.Context(densityContext);
        densityChildContext.position = new Vector3d(densityContext.position.x + sampleDistance, densityContext.position.y, densityContext.position.z);
        double deltaX = warpField.process(densityChildContext) - valueAtOrigin;
        densityChildContext.position = new Vector3d(densityContext.position.x, densityContext.position.y, densityContext.position.z + sampleDistance);
        double deltaZ = warpField.process(densityChildContext) - valueAtOrigin;
        double offsetX = deltaX * warpFactor / sampleDistance;
        double offsetZ = deltaZ * warpFactor / sampleDistance;

        stack.pushWithOffset(offsetX, offsetZ);
        int value = processChild(stack);
        stack.pop();
        return value;
    }
}
