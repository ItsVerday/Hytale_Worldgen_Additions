package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.math.vector.Vector3d;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class VectorWarpPipelineCartaTransform extends PipelineCartaTransform {
    @Nullable
    private final PipelineCartaTransform child;
    @Nonnull
    private final Density warpField;
    private final double sampleDistance;
    private final double warpFactor;

    public VectorWarpPipelineCartaTransform(@Nullable PipelineCartaTransform child, @Nonnull Density warpField, double sampleDistance, double warpFactor) {
        this.child = child;
        this.warpField = warpField;
        this.sampleDistance = sampleDistance;
        this.warpFactor = warpFactor;
    }

    @NullableDecl
    @Override
    public String process(@NonNullDecl Context ctx) {
        double offsetX = 0.0;
        double offsetZ = 0.0;
        if (warpField != null) {
            Density.Context densityCtx = new Density.Context();
            densityCtx.position = new Vector3d(ctx.position.x, 0, ctx.position.y);
            densityCtx.workerId = ctx.workerId;

            double valueAtOrigin = warpField.process(densityCtx);
            Density.Context densityChildCtx = new Density.Context(densityCtx);
            densityChildCtx.position = new Vector3d(densityCtx.position.x + sampleDistance, densityCtx.position.y, densityCtx.position.z);
            double deltaX = warpField.process(densityChildCtx) - valueAtOrigin;
            densityChildCtx.position = new Vector3d(densityCtx.position.x, densityCtx.position.y, densityCtx.position.z + sampleDistance);
            double deltaZ = warpField.process(densityChildCtx) - valueAtOrigin;
            offsetX = deltaX * warpFactor / sampleDistance;
            offsetZ = deltaZ * warpFactor / sampleDistance;
        }

        Context childCtx = ctx.withOffset(offsetX, offsetZ);

        if (child != null) return child.process(childCtx);
        return childCtx.queryBiome();
    }

    @Override
    public List<String> allPossibleValues() {
        if (child != null) return child.allPossibleValues();
        return List.of();
    }

    @Override
    public int getMaxPipelineBiomeDistance() {
        if (child != null) return child.getMaxPipelineBiomeDistance();
        return 0;
    }
}
