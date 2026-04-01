package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ConditionalPipelineCartaTransform<R> extends PipelineCartaTransform<R> {
    private final Condition<R> condition;
    @Nullable
    private final PipelineCartaTransform<R> ifTrue;
    @Nullable
    private final PipelineCartaTransform<R> ifFalse;

    public ConditionalPipelineCartaTransform(Condition<R> condition, @Nullable PipelineCartaTransform<R> ifTrue, @Nullable PipelineCartaTransform<R> ifFalse) {
        this.condition = condition;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    @NullableDecl
    @Override
    public R process(@NonNullDecl ContextStack<R> stack) {
        if (condition != null && condition.process(stack)) {
            if (ifTrue != null) return ifTrue.process(stack);
        } else {
            if (ifFalse != null) return ifFalse.process(stack);
        }

        return null;
    }

    @Override
    public List<R> allPossibleValues() {
        ArrayList<R> values = new ArrayList<>();
        if (ifTrue != null) values.addAll(ifTrue.allPossibleValues());
        if (ifFalse != null) values.addAll(ifFalse.allPossibleValues());
        return values;
    }

    public abstract static class Condition<R> {
        public abstract boolean process(ContextStack<R> stack);
    }
}
