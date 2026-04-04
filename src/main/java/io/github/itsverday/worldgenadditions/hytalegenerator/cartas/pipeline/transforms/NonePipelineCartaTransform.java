package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.List;

public class NonePipelineCartaTransform extends PipelineCartaTransform {
    @Override
    public int process(@NonNullDecl ContextStack stack) {
        return -1;
    }

    @Override
    public List<Integer> allPossibleValues() {
        return List.of();
    }
}
