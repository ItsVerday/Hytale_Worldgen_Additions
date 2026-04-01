package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;

import java.util.List;

public class OrCondition<R> extends ConditionalPipelineCartaTransform.Condition<R> {
    private final List<ConditionalPipelineCartaTransform.Condition<R>> conditions;

    public OrCondition(List<ConditionalPipelineCartaTransform.Condition<R>> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean process(PipelineCartaTransform.ContextStack<R> stack) {
        for (ConditionalPipelineCartaTransform.Condition<R> condition: conditions) {
            if (condition.process(stack)) return true;
        }

        return false;
    }
}
