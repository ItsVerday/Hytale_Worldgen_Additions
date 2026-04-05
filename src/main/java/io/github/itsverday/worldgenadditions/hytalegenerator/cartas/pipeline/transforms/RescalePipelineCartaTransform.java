package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import com.hypixel.hytale.math.vector.Vector2d;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class RescalePipelineCartaTransform extends PipelineCartaTransform {
    @Nonnull
    private final PipelineCartaTransform child;
    private final double scalingFactor;

    private final Vector2d rChildPosition;
    private final Context rChildContext;

    public RescalePipelineCartaTransform(@Nonnull PipelineCartaTransform child, double scalingFactor) {
        this.child = child;
        this.scalingFactor = scalingFactor;

        rChildPosition = new Vector2d();
        rChildContext = new Context();
    }

    @Override
    public int process(@NonNullDecl Context context) {
        rChildPosition.assign(context.position);
        rChildPosition.scale(scalingFactor);
        rChildContext.assign(context);
        rChildContext.position = rChildPosition;
        return child.process(rChildContext);
    }

    @Override
    public List<Integer> allPossibleValues() {
        return child.allPossibleValues();
    }
}
