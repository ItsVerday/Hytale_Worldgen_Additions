package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions;

import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

public class ValueCondition extends ConditionalPipelineCartaTransform.Condition {
    private final int value;
    @Nonnull
    private final PipelineCartaTransform previous;

    public ValueCondition(int value, @NonNullDecl PipelineCartaTransform previous) {
        this.value = value;
        this.previous = previous;
    }

    @Override
    public boolean process(PipelineCartaTransform.Context context) {
        return value == previous.process(context);
    }
}
