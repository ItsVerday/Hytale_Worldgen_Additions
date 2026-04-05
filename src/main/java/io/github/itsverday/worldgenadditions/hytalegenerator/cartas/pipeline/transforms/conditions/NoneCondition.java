package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;

public class NoneCondition extends ConditionalPipelineCartaTransform.Condition {
    @Override
    public boolean process(PipelineCartaTransform.Context context) {
        return false;
    }
}
