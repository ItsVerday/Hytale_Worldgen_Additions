package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
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
    public R process(@NonNullDecl Context<R> context) {
        if (condition.process(context)) {
            if (ifTrue != null) return ifTrue.process(context);
        } else {
            if (ifFalse != null) return ifFalse.process(context);
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
        public abstract boolean process(Context<R> context);
    }
}
