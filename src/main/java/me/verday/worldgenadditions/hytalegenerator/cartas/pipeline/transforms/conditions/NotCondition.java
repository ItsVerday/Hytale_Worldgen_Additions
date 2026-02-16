package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;

public class NotCondition<R> extends ConditionalPipelineCartaTransform.Condition<R> {
    private final ConditionalPipelineCartaTransform.Condition<R> condition;

    public NotCondition(ConditionalPipelineCartaTransform.Condition<R> condition) {
        this.condition = condition;
    }

    @Override
    public boolean process(PipelineCartaTransform.Context<R> context) {
        return !condition.process(context);
    }
}
