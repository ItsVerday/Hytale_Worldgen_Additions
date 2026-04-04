package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;

import javax.annotation.Nonnull;

public class DistanceCondition extends AbstractDistanceCondition {
    private final double distance;

    public DistanceCondition(@Nonnull ConditionalPipelineCartaTransform.Condition child, double distance, boolean fastMode) {
        super(child, fastMode);
        this.distance = distance;
    }

    @Override
    public double getDistanceToQuery(PipelineCartaTransform.ContextStack stack) {
        return distance;
    }
}