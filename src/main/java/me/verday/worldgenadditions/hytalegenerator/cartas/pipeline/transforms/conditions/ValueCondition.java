package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;

import javax.annotation.Nonnull;

public class ValueCondition<R> extends ConditionalPipelineCartaTransform.Condition<R> {
    @Nonnull
    private final R value;

    public ValueCondition(@Nonnull R value) {
        this.value = value;
    }

    @Override
    public boolean process(PipelineCartaTransform.Context<R> context) {
        return value.equals(context.stage.processPrevious(context));
    }
}
