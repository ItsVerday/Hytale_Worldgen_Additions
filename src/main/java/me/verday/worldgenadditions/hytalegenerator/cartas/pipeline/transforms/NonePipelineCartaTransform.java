package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.List;

public class NonePipelineCartaTransform<R> extends PipelineCartaTransform<R> {
    @NullableDecl
    @Override
    public R process(@NonNullDecl Context<R> context) {
        return null;
    }

    @Override
    public List<R> allPossibleValues() {
        return List.of();
    }
}
