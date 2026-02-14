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
    public R process(@NonNullDecl Context<R> ctx) {
        if (condition.process(ctx)) {
            if (ifTrue != null) return ifTrue.process(ctx);
        } else {
            if (ifFalse != null) return ifFalse.process(ctx);
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

    @Override
    public int getMaxPipelineValueDistance() {
        int distance = 0;
        if (ifTrue != null) {
            int newDistance = ifTrue.getMaxPipelineValueDistance();
            if (newDistance > distance) distance = newDistance;
        }

        if (ifFalse != null) {
            int newDistance = ifFalse.getMaxPipelineValueDistance();
            if (newDistance > distance) distance = newDistance;
        }

        int newDistance = condition.getMaxPipelineBiomeDistance();
        if (newDistance > distance) distance = newDistance;

        return distance;
    }

    public abstract static class Condition<R> {
        public abstract boolean process(Context<R> context);

        public int getMaxPipelineBiomeDistance() {
            return 0;
        }
    }
}
