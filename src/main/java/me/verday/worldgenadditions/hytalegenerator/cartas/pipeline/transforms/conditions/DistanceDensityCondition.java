package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.framework.math.Normalizer;
import com.hypixel.hytale.builtin.hytalegenerator.threadindexer.WorkerIndexer;
import com.hypixel.hytale.math.vector.Vector3d;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;

import javax.annotation.Nonnull;

public class DistanceDensityCondition<R> extends AbstractDistanceCondition<R> {
    private final double distanceMinimum;
    private final double distanceMaximum;
    @Nonnull
    private final Density density;

    public DistanceDensityCondition(WorkerIndexer workerIndexer, @Nonnull ConditionalPipelineCartaTransform.Condition<R> child, double distanceMinimum, double distanceMaximum, @Nonnull Density density) {
        super(workerIndexer, child);
        this.distanceMinimum = distanceMinimum;
        this.distanceMaximum = distanceMaximum;
        this.density = density;
    }

    @Override
    public double getDistanceToQuery(PipelineCartaTransform.Context<R> context) {
        Density.Context childContext = new Density.Context();
        childContext.position = new Vector3d(context.position.x, 0, context.position.y);
        childContext.workerId = context.workerId;
        double densityValue = density.process(childContext);
        if (densityValue < -1) densityValue = -1;
        if (densityValue > 1) densityValue = 1;

        return Normalizer.normalize(-1, 1, distanceMinimum, distanceMaximum, densityValue);
    }
}
