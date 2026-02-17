package me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.validation.Validators;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.NonePipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.SmoothingPipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class SmoothingPipelineCartaTransformAsset extends PipelineCartaTransformAsset {
    public static final BuilderCodec<SmoothingPipelineCartaTransformAsset> CODEC = BuilderCodec.builder(SmoothingPipelineCartaTransformAsset.class, SmoothingPipelineCartaTransformAsset::new, PipelineCartaTransformAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("Radius", Codec.DOUBLE, true), (t, k) -> t.radius = k, t -> t.radius)
            .addValidator(Validators.greaterThan(0.0))
            .add()
            .append(new KeyedCodec<>("Threshold", Codec.DOUBLE, false), (t, k) -> t.threshold = k, t -> t.threshold)
            .addValidator(Validators.range(0.0, 1.0))
            .add()
            .build();

    private double radius;
    private double threshold = 0.5;

    @NonNullDecl
    @Override
    public PipelineCartaTransform<Integer> build(@NonNullDecl Argument arg) {
        if (isSkipped()) return new NonePipelineCartaTransform<>();

        PipelineCartaTransform<Integer> child = null;
        if (inputs().length > 0) {
            child = inputs()[0].build(arg);
        }

        return new SmoothingPipelineCartaTransform<>(child, radius, threshold);
    }
}
