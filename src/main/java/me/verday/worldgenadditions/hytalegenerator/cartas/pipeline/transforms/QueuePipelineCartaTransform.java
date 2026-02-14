package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class QueuePipelineCartaTransform<R> extends PipelineCartaTransform<R> {
    @Nonnull
    private final List<PipelineCartaTransform<R>> children;

    public QueuePipelineCartaTransform(@NonNullDecl List<PipelineCartaTransform<R>> children) {
        this.children = children;
    }

    @NullableDecl
    @Override
    public R process(@NonNullDecl Context<R> ctx) {
        Context<R> childCtx = new Context<>(ctx);
        childCtx.fallthrough = false;
        for (PipelineCartaTransform<R> child: children) {
            R result = child.process(childCtx);
            if (result != null) return result;
        }

        return null;
    }

    @Override
    public List<R> allPossibleValues() {
        ArrayList<R> values = new ArrayList<>();

        for (PipelineCartaTransform<R> child: children) {
            for (R possibility: child.allPossibleValues()) {
                if (!values.contains(possibility)) {
                    values.add(possibility);
                }
            }
        }

        return values;
    }

    @Override
    public int getMaxPipelineValueDistance() {
        int distance = 0;
        for (PipelineCartaTransform<R> child: children) {
            int newDistance = child.getMaxPipelineValueDistance();
            if (newDistance > distance) distance = newDistance;
        }

        return distance;
    }
}
