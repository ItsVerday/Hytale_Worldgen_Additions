package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.framework.math.Normalizer;
import com.hypixel.hytale.math.vector.Vector3d;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;

import javax.annotation.Nonnull;

public class ValueDistanceDensityCondition<R> extends ConditionalPipelineCartaTransform.Condition<R> {
    @Nonnull
    private final R value;
    private final double distanceMinimum;
    private final double distanceMaximum;
    @Nonnull
    private final Density density;

    public ValueDistanceDensityCondition(@Nonnull R value, double distanceMinimum, double distanceMaximum, @Nonnull Density density) {
        this.value = value;
        this.distanceMinimum = distanceMinimum;
        this.distanceMaximum = distanceMaximum;
        this.density = density;
    }

    @Override
    public boolean process(PipelineCartaTransform.Context<R> context) {
        Density.Context childContext = new Density.Context();
        childContext.position = new Vector3d(context.position.x, 0, context.position.y);
        childContext.workerId = context.workerId;
        double densityValue = density.process(childContext);
        if (densityValue < -1) densityValue = -1;
        if (densityValue > 1) densityValue = 1;

        double distance = Normalizer.normalize(-1, 1, distanceMinimum, distanceMaximum, densityValue);
        return context.queryValueDistanceSquared(value) <= distance * distance;
    }

    @Override
    public int getMaxPipelineBiomeDistance() {
        return (int) Math.ceil(distanceMaximum);
    }
}
