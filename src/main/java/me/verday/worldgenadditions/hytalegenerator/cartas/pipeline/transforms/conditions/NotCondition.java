package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;

public class NotCondition extends ConditionalPipelineCartaTransform.Condition {
    private final ConditionalPipelineCartaTransform.Condition condition;

    public NotCondition(ConditionalPipelineCartaTransform.Condition condition) {
        this.condition = condition;
    }

    @Override
    public boolean process(PipelineCartaTransform.Context context) {
        return !condition.process(context);
    }

    @Override
    public int getMaxPipelineBiomeDistance() {
        return condition.getMaxPipelineBiomeDistance();
    }
}
