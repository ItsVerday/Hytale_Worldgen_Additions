package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.FieldFunctionPipelineCartaTransform;

import java.util.List;

public class AndCondition<R> extends ConditionalPipelineCartaTransform.Condition<R> {
    private final List<ConditionalPipelineCartaTransform.Condition<R>> conditions;

    public AndCondition(List<ConditionalPipelineCartaTransform.Condition<R>> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean process(PipelineCartaTransform.Context<R> context) {
        for (ConditionalPipelineCartaTransform.Condition<R> condition: conditions) {
            if (!condition.process(context)) return false;
        }

        return true;
    }

    @Override
    public int getMaxPipelineBiomeDistance() {
        int distance = 0;
        for (ConditionalPipelineCartaTransform.Condition<R> condition: conditions) {
            int newDistance = condition.getMaxPipelineBiomeDistance();
            if (newDistance > distance) distance = newDistance;
        }

        return distance;
    }
}
