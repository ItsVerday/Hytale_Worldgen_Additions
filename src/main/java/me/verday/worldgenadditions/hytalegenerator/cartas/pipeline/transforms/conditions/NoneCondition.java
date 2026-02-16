package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;

public class NoneCondition<R> extends ConditionalPipelineCartaTransform.Condition<R> {
    @Override
    public boolean process(PipelineCartaTransform.Context<R> context) {
        return false;
    }
}
