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

    public DistanceDensityCondition(@Nonnull ConditionalPipelineCartaTransform.Condition child, double distanceMinimum, double distanceMaximum, @Nonnull Density density, boolean fastMode) {
        super(child, fastMode);
        this.distanceMinimum = distanceMinimum;
        this.distanceMaximum = distanceMaximum;
        this.density = density;
    }

    @Override
    public double getDistanceToQuery(PipelineCartaTransform.ContextStack stack) {
        Density.Context childContext = new Density.Context();
        Vector2d position = stack.getPosition();
        childContext.position = new Vector3d(position.x, 0, position.y);
        double densityValue = density.process(childContext);
        if (densityValue < -1) densityValue = -1;
        if (densityValue > 1) densityValue = 1;

        return Normalizer.normalize(-1, 1, distanceMinimum, distanceMaximum, densityValue);
    }
}
