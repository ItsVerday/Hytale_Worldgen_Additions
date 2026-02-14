package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ConditionalPipelineCartaTransform extends PipelineCartaTransform {
    private final Condition condition;
    @Nullable
    private final PipelineCartaTransform ifTrue;
    @Nullable
    private final PipelineCartaTransform ifFalse;

    public ConditionalPipelineCartaTransform(Condition condition, @Nullable PipelineCartaTransform ifTrue, @Nullable PipelineCartaTransform ifFalse) {
        this.condition = condition;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    @NullableDecl
    @Override
    public String process(@NonNullDecl Context ctx) {
        if (condition.process(ctx)) {
            if (ifTrue != null) return ifTrue.process(ctx);
        } else {
            if (ifFalse != null) return ifFalse.process(ctx);
        }

        return null;
    }

    @Override
    public List<String> allPossibleValues() {
        ArrayList<String> values = new ArrayList<>();
        if (ifTrue != null) values.addAll(ifTrue.allPossibleValues());
        if (ifFalse != null) values.addAll(ifFalse.allPossibleValues());
        return values;
    }

    @Override
    public int getMaxPipelineBiomeDistance() {
        int distance = 0;
        if (ifTrue != null) {
            int newDistance = ifTrue.getMaxPipelineBiomeDistance();
            if (newDistance > distance) distance = newDistance;
        }

        if (ifFalse != null) {
            int newDistance = ifFalse.getMaxPipelineBiomeDistance();
            if (newDistance > distance) distance = newDistance;
        }

        int newDistance = condition.getMaxPipelineBiomeDistance();
        if (newDistance > distance) distance = newDistance;

        return distance;
    }

    public abstract static class Condition {
        public abstract boolean process(Context context);

        public int getMaxPipelineBiomeDistance() {
            return 0;
        }
    }
}
