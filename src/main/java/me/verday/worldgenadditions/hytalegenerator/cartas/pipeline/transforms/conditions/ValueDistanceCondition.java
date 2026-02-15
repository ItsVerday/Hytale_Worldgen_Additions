package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;

import javax.annotation.Nonnull;

public class ValueDistanceCondition<R> extends ConditionalPipelineCartaTransform.Condition<R> {
    @Nonnull
    private final R value;
    private final double distance;

    public ValueDistanceCondition(@Nonnull R value, double distance) {
        this.value = value;
        this.distance = distance;
    }

    @Override
    public boolean process(PipelineCartaTransform.Context<R> context) {
        return context.queryValueDistanceSquared(value) <= distance * distance;
    }

    @Override
    public int getMaxPipelineBiomeDistance() {
        return (int) Math.ceil(distance);
    }
}