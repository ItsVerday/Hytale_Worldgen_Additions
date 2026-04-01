package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;

import javax.annotation.Nonnull;

public class ValueCondition<R> extends ConditionalPipelineCartaTransform.Condition<R> {
    @Nonnull
    private final R value;

    public ValueCondition(@Nonnull R value) {
        this.value = value;
    }

    @Override
    public boolean process(PipelineCartaTransform.ContextStack<R> stack) {
        return value.equals(stack.getStage().processPrevious(stack));
    }
}
