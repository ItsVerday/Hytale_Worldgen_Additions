package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class QueuePipelineCartaTransform extends PipelineCartaTransform {
    @Nonnull
    private PipelineCartaTransform[] children;

    public QueuePipelineCartaTransform() {
    }

    @Override
    public void setInputs(PipelineCartaTransform[] inputs) {
        this.children = inputs;
    }

    @NullableDecl
    @Override
    public String process(@NonNullDecl Context ctx) {
        Context childCtx = new Context(ctx);
        childCtx.fallthrough = false;
        for (PipelineCartaTransform child: children) {
            String result = child.process(childCtx);
            if (result != null) return result;
        }

        return null;
    }

    @Override
    public List<String> allPossibleValues() {
        ArrayList<String> values = new ArrayList<>();

        for (PipelineCartaTransform child: children) {
            for (String possibility: child.allPossibleValues()) {
                if (!values.contains(possibility)) {
                    values.add(possibility);
                }
            }
        }

        return values;
    }

    @Override
    public int getMaxPipelineBiomeDistance() {
        int distance = 0;
        for (PipelineCartaTransform child: children) {
            int newDistance = child.getMaxPipelineBiomeDistance();
            if (newDistance > distance) distance = newDistance;
        }

        return distance;
    }
}
