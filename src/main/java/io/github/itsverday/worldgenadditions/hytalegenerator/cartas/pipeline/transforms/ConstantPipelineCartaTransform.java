package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.List;

public class ConstantPipelineCartaTransform extends PipelineCartaTransform {
    private final int value;

    public ConstantPipelineCartaTransform(int value) {
        this.value = value;
    }

    @Override
    public int process(@NonNullDecl Context context) {
        return value;
    }

    @Override
    public List<Integer> allPossibleValues() {
        return List.of(value);
    }
}
