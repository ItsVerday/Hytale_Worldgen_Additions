package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;

public class NotCondition<R> extends ConditionalPipelineCartaTransform.Condition<R> {
    private final ConditionalPipelineCartaTransform.Condition<R> condition;

    public NotCondition(ConditionalPipelineCartaTransform.Condition<R> condition) {
        this.condition = condition;
    }

    @Override
    public boolean process(PipelineCartaTransform.ContextStack<R> stack) {
        return !condition.process(stack);
    }
}
