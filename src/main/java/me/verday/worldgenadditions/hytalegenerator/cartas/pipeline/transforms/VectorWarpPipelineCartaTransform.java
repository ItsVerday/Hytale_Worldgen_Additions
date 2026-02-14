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
    public R process(@NonNullDecl Context<R> ctx) {
        Density.Context densityCtx = new Density.Context();
        densityCtx.position = new Vector3d(ctx.position.x, 0, ctx.position.y);
        densityCtx.workerId = ctx.workerId;

        double valueAtOrigin = warpField.process(densityCtx);
        Density.Context densityChildCtx = new Density.Context(densityCtx);
        densityChildCtx.position = new Vector3d(densityCtx.position.x + sampleDistance, densityCtx.position.y, densityCtx.position.z);
        double deltaX = warpField.process(densityChildCtx) - valueAtOrigin;
        densityChildCtx.position = new Vector3d(densityCtx.position.x, densityCtx.position.y, densityCtx.position.z + sampleDistance);
        double deltaZ = warpField.process(densityChildCtx) - valueAtOrigin;
        double offsetX = deltaX * warpFactor / sampleDistance;
        double offsetZ = deltaZ * warpFactor / sampleDistance;

        Context<R> childCtx = ctx.withOffset(offsetX, offsetZ);

        if (child != null) return child.process(childCtx);
        return childCtx.queryValue();
    }

    @Override
    public List<R> allPossibleValues() {
        if (child != null) return child.allPossibleValues();
        return List.of();
    }

    @Override
    public int getMaxPipelineValueDistance() {
        if (child != null) return child.getMaxPipelineValueDistance();
        return 0;
    }
}
