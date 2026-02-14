package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;

public class OrCondition extends ConditionalPipelineCartaTransform.Condition {
    private final ConditionalPipelineCartaTransform.Condition[] conditions;

    public OrCondition(ConditionalPipelineCartaTransform.Condition[] conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean process(PipelineCartaTransform.Context context) {
        for (ConditionalPipelineCartaTransform.Condition condition: conditions) {
            if (condition.process(context)) return true;
        }

        return false;
    }

    @Override
    public int getMaxPipelineBiomeDistance() {
        int distance = 0;
        for (ConditionalPipelineCartaTransform.Condition condition: conditions) {
            int newDistance = condition.getMaxPipelineBiomeDistance();
            if (newDistance > distance) distance = newDistance;
        }

        return distance;
    }
}
