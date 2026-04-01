package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;

public class NoneCondition<R> extends ConditionalPipelineCartaTransform.Condition<R> {
    @Override
    public boolean process(PipelineCartaTransform.ContextStack<R> stack) {
        return false;
    }
}
