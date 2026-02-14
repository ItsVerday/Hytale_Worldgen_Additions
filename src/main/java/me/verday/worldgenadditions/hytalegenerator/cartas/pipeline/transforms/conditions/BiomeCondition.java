package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;

public class BiomeCondition extends ConditionalPipelineCartaTransform.Condition {
    private final String biomeId;

    public BiomeCondition(String biomeId) {
        this.biomeId = biomeId;
    }

    @Override
    public boolean process(PipelineCartaTransform.Context context) {
        return context.queryBiome().equals(biomeId);
    }
}
