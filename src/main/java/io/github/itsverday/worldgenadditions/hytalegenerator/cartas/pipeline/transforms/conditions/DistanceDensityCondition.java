package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.math.Normalizer;
import com.hypixel.hytale.math.vector.Vector2d;
import com.hypixel.hytale.math.vector.Vector3d;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;

import javax.annotation.Nonnull;

public class DistanceDensityCondition extends AbstractDistanceCondition {
    private final double distanceMinimum;
    private final double distanceMaximum;
    @Nonnull
    private final Density density;

    private final Vector3d rChildPosition;
    private final Density.Context rChildContext;

    public DistanceDensityCondition(@Nonnull ConditionalPipelineCartaTransform.Condition child, double distanceMinimum, double distanceMaximum, @Nonnull Density density, boolean fastMode) {
        super(child, fastMode);
        this.distanceMinimum = distanceMinimum;
        this.distanceMaximum = distanceMaximum;
        this.density = density;

        rChildPosition = new Vector3d();
        rChildContext = new Density.Context();
    }

    @Override
    public double getDistanceToQuery(PipelineCartaTransform.Context context) {
        rChildPosition.assign(context.position.x, 0, context.position.y);
        rChildContext.position = rChildPosition;
        double densityValue = density.process(rChildContext);
        if (densityValue < -1) densityValue = -1;
        if (densityValue > 1) densityValue = 1;

        return Normalizer.normalize(-1, 1, distanceMinimum, distanceMaximum, densityValue);
    }
}
