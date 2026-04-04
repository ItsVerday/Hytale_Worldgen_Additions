package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;

import javax.annotation.Nonnull;

public class ValueCondition extends ConditionalPipelineCartaTransform.Condition {
    private final int value;

    public ValueCondition(int value) {
        this.value = value;
    }

    @Override
    public boolean process(PipelineCartaTransform.ContextStack stack) {
        return value == stack.getStage().processPrevious(stack);
    }
}
