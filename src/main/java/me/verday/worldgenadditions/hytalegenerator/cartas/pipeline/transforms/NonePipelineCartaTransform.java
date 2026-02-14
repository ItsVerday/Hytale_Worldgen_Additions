package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.List;

public class NonePipelineCartaTransform extends PipelineCartaTransform {
    @NullableDecl
    @Override
    public String process(@NonNullDecl Context ctx) {
        return null;
    }

    @Override
    public List<String> allPossibleValues() {
        return List.of();
    }
}
