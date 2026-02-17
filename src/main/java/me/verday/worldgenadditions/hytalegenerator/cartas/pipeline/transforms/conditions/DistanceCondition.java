package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;

import javax.annotation.Nonnull;

public class DistanceCondition<R> extends AbstractDistanceCondition<R> {
    private final double distance;

    public DistanceCondition(@Nonnull ConditionalPipelineCartaTransform.Condition<R> child, double distance, boolean fastMode) {
        super(child, fastMode);
        this.distance = distance;
    }

    @Override
    public double getDistanceToQuery(PipelineCartaTransform.Context<R> context) {
        return distance;
    }
}