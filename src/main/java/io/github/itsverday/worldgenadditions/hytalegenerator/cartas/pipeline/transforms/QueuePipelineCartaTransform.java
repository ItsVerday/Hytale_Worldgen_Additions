package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
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
    public R process(@NonNullDecl ContextStack<R> stack) {
        stack.pushWithFallthrough(false);
        for (PipelineCartaTransform<R> child: children) {
            R result = child.process(stack);
            if (result != null) {
                stack.pop();
                return result;
            }
        }

        stack.pop();
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
}
