package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class QueuePipelineCartaTransform extends PipelineCartaTransform {
    @Nonnull
    private final List<PipelineCartaTransform> children;

    public QueuePipelineCartaTransform(@NonNullDecl List<PipelineCartaTransform> children) {
        this.children = children;
    }

    @Override
    public int process(@NonNullDecl ContextStack stack) {
        stack.pushWithFallthrough(false);
        for (PipelineCartaTransform child: children) {
            int result = child.process(stack);
            if (result != -1) {
                stack.pop();
                return result;
            }
        }

        stack.pop();
        return -1;
    }

    @Override
    public List<Integer> allPossibleValues() {
        ArrayList<Integer> values = new ArrayList<>();

        for (PipelineCartaTransform child: children) {
            for (Integer possibility: child.allPossibleValues()) {
                if (!values.contains(possibility)) {
                    values.add(possibility);
                }
            }
        }

        return values;
    }
}
