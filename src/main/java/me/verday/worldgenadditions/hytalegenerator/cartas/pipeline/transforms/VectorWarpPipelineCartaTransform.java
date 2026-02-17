package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.math.vector.Vector3d;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class VectorWarpPipelineCartaTransform<R> extends PipelineCartaTransform<R> {
    @Nullable
    private final PipelineCartaTransform<R> child;
    @Nonnull
    private final Density warpField;
    private final double sampleDistance;
    private final double warpFactor;

    public VectorWarpPipelineCartaTransform(@Nullable PipelineCartaTransform<R> child, @Nonnull Density warpField, double sampleDistance, double warpFactor) {
        this.child = child;
        this.warpField = warpField;
        this.sampleDistance = sampleDistance;
        this.warpFactor = warpFactor;
    }

    @NullableDecl
    @Override
    public R process(@NonNullDecl Context<R> context) {
        Density.Context densityContext = new Density.Context();
        densityContext.position = new Vector3d(context.position.x, 0, context.position.y);

        double valueAtOrigin = warpField.process(densityContext);
        Density.Context densityChildContext = new Density.Context(densityContext);
        densityChildContext.position = new Vector3d(densityContext.position.x + sampleDistance, densityContext.position.y, densityContext.position.z);
        double deltaX = warpField.process(densityChildContext) - valueAtOrigin;
        densityChildContext.position = new Vector3d(densityContext.position.x, densityContext.position.y, densityContext.position.z + sampleDistance);
        double deltaZ = warpField.process(densityChildContext) - valueAtOrigin;
        double offsetX = deltaX * warpFactor / sampleDistance;
        double offsetZ = deltaZ * warpFactor / sampleDistance;

        Context<R> childContext = context.withOffset(offsetX, offsetZ);

        if (child != null) return child.process(childContext);
        return childContext.queryValue();
    }

    @Override
    public List<R> allPossibleValues() {
        if (child != null) return child.allPossibleValues();
        return List.of();
    }
}
