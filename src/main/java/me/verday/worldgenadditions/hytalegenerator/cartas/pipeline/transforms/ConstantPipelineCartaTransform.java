package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.List;

public class ConstantPipelineCartaTransform extends PipelineCartaTransform {
    private final String value;

    public ConstantPipelineCartaTransform(String value) {
        this.value = value;
    }

    @NullableDecl
    @Override
    public String process(@NonNullDecl Context ctx) {
        return value;
    }

    @Override
    public List<String> allPossibleValues() {
        return List.of(value);
    }
}
