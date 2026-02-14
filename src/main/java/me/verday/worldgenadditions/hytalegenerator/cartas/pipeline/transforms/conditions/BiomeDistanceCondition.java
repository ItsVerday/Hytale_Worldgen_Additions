package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;

public class BiomeDistanceCondition extends ConditionalPipelineCartaTransform.Condition {
    private final String biomeId;
    private final double distance;

    public BiomeDistanceCondition(String biomeId, double distance) {
        this.biomeId = biomeId;
        this.distance = distance;
    }

    @Override
    public boolean process(PipelineCartaTransform.Context context) {
        return context.queryBiomeDistanceSquared(biomeId) <= distance * distance;
    }

    @Override
    public int getMaxPipelineBiomeDistance() {
        return (int) Math.ceil(distance);
    }
}