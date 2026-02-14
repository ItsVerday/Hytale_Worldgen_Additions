package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;

public class ValueCondition<R> extends ConditionalPipelineCartaTransform.Condition<R> {
    private final R value;

    public ValueCondition(R value) {
        this.value = value;
    }

    @Override
    public boolean process(PipelineCartaTransform.Context<R> context) {
        return context.queryValue().equals(value);
    }
}
