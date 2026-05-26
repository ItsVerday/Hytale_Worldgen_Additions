package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.joml.Vector2d;
import org.joml.Vector3d;

import javax.annotation.Nonnull;
import java.util.List;

public class GradientWarpPipelineCartaTransform extends PipelineCartaTransform {
    @Nonnull
    private final PipelineCartaTransform child;
    @Nonnull
    private final Density warpField;
    private final double sampleDistance;
    private final double warpFactor;

    private final Vector3d rDensityChildPosition;
    private final Density.Context rDensityChildContext;

    private final Vector2d rChildPosition;
    private final Context rChildContext;

    public GradientWarpPipelineCartaTransform(@Nonnull PipelineCartaTransform child, @Nonnull Density warpField, double sampleDistance, double warpFactor) {
        this.child = child;
        this.warpField = warpField;
        this.sampleDistance = sampleDistance;
        this.warpFactor = warpFactor;

        rDensityChildPosition = new Vector3d();
        rDensityChildContext = new Density.Context();

        rChildPosition = new Vector2d();
        rChildContext = new Context();
    }

    @Override
    public int process(@NonNullDecl Context context) {
        rDensityChildPosition.set(context.position.x, 0, context.position.y);
        rDensityChildContext.position = rDensityChildPosition;

        double valueAtOrigin = warpField.process(rDensityChildContext);
        rDensityChildContext.position.set(context.position.x + sampleDistance, rDensityChildPosition.y, context.position.y);
        double deltaX = warpField.process(rDensityChildContext) - valueAtOrigin;
        rDensityChildContext.position.set(context.position.x, rDensityChildPosition.y, context.position.y + sampleDistance);
        double deltaZ = warpField.process(rDensityChildContext) - valueAtOrigin;
        double offsetX = deltaX * warpFactor / sampleDistance;
        double offsetZ = deltaZ * warpFactor / sampleDistance;

        rChildPosition.set(context.position);
        rChildPosition.add(offsetX, offsetZ);
        rChildContext.assign(context);
        rChildContext.position = rChildPosition;
        return child.process(rChildContext);
    }

    @Override
    public List<Integer> allPossibleValues() {
        return child.allPossibleValues();
    }
}
