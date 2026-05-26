package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.joml.Vector2d;
import org.joml.Vector3d;

import javax.annotation.Nonnull;
import java.util.List;

public class VectorWarpPipelineCartaTransform extends PipelineCartaTransform {
    @Nonnull
    private final PipelineCartaTransform child;
    @Nonnull
    private final Density warpField;
    private final double warpFactor;
    private final Vector2d warpVector;

    private final Vector3d rDensityChildPosition;
    private final Density.Context rDensityChildContext;

    private final Vector2d rChildPosition;
    private final Context rChildContext;

    public VectorWarpPipelineCartaTransform(@Nonnull PipelineCartaTransform child, @Nonnull Density warpField, double warpFactor, Vector2d warpVector) {
        this.child = child;
        this.warpField = warpField;
        this.warpFactor = warpFactor;
        this.warpVector = warpVector;

        rDensityChildPosition = new Vector3d();
        rDensityChildContext = new Density.Context();

        rChildPosition = new Vector2d();
        rChildContext = new Context();
    }

    @Override
    public int process(@NonNullDecl Context context) {
        rDensityChildPosition.set(context.position.x, 0, context.position.y);
        rDensityChildContext.position = rDensityChildPosition;

        double warp = warpField.process(rDensityChildContext);
        warp *= warpFactor;
        rChildPosition.set(warpVector);
        rChildPosition.normalize(1.0);
        rChildPosition.mul(warp);
        rChildPosition.add(context.position);

        rChildContext.assign(context);
        rChildContext.position = rChildPosition;
        return child.process(rChildContext);
    }

    @Override
    public List<Integer> allPossibleValues() {
        return child.allPossibleValues();
    }
}
