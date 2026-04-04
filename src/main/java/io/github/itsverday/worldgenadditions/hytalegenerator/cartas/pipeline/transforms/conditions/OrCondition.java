package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;

import java.util.List;

public class OrCondition extends ConditionalPipelineCartaTransform.Condition {
    private final List<ConditionalPipelineCartaTransform.Condition> conditions;

    public OrCondition(List<ConditionalPipelineCartaTransform.Condition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean process(PipelineCartaTransform.ContextStack stack) {
        for (ConditionalPipelineCartaTransform.Condition condition: conditions) {
            if (condition.process(stack)) return true;
        }

        return false;
    }
}
