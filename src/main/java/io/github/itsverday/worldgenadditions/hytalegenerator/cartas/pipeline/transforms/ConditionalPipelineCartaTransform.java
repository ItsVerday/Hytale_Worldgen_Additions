package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ConditionalPipelineCartaTransform extends PipelineCartaTransform {
    @Nonnull
    private final PipelineCartaTransform previous;
    private final Condition condition;
    @Nullable
    private final PipelineCartaTransform ifTrue;
    @Nullable
    private final PipelineCartaTransform ifFalse;

    public ConditionalPipelineCartaTransform(@NonNullDecl PipelineCartaTransform previous, Condition condition, @Nullable PipelineCartaTransform ifTrue, @Nullable PipelineCartaTransform ifFalse) {
        this.previous = previous;
        this.condition = condition;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    @Override
    public int process(@NonNullDecl ContextStack stack) {
        if (condition != null && condition.process(stack)) {
            if (ifTrue != null) return ifTrue.process(stack);
        } else {
            if (ifFalse != null) return ifFalse.process(stack);
        }

        return previous.process(stack);
    }

    @Override
    public List<Integer> allPossibleValues() {
        ArrayList<Integer> values = new ArrayList<>();
        if (ifTrue != null) values.addAll(ifTrue.allPossibleValues());
        if (ifFalse != null) values.addAll(ifFalse.allPossibleValues());
        return values;
    }

    public abstract static class Condition {
        public abstract boolean process(ContextStack stack);
    }
}
