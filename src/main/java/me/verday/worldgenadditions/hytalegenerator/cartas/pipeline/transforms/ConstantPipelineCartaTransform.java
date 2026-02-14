package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.List;

public class ConstantPipelineCartaTransform<R> extends PipelineCartaTransform<R> {
    private final R value;

    public ConstantPipelineCartaTransform(R value) {
        this.value = value;
    }

    @NullableDecl
    @Override
    public R process(@NonNullDecl Context<R> ctx) {
        return value;
    }

    @Override
    public List<R> allPossibleValues() {
        return List.of(value);
    }
}
