package io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.validation.Validators;
import io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.CachePipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.SmoothingPipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class SmoothingPipelineCartaTransformAsset extends PipelineCartaTransformAsset {
    public static final BuilderCodec<SmoothingPipelineCartaTransformAsset> CODEC = BuilderCodec.builder(SmoothingPipelineCartaTransformAsset.class, SmoothingPipelineCartaTransformAsset::new, PipelineCartaTransformAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("Radius", Codec.DOUBLE, true), (t, k) -> t.radius = k, t -> t.radius)
            .addValidator(Validators.greaterThanOrEqual(0.0))
            .add()
            .append(new KeyedCodec<>("Threshold", Codec.DOUBLE, false), (t, k) -> t.threshold = k, t -> t.threshold)
            .addValidator(Validators.range(0.0, 1.0))
            .add()
            .build();

    private double radius;
    private double threshold = 0.5;

    @NonNullDecl
    @Override
    public PipelineCartaTransform build(@NonNullDecl Argument arg, PipelineCartaTransform previous) {
        if (isSkipped()) return previous;

        PipelineCartaTransform child = previous;
        if (inputs().length > 0) {
            child = inputs()[0].build(arg, previous);
        }

        if (radius == 0.0) return child;
        if (PipelineCartaTransform.shouldCacheTransform(child)) child = new CachePipelineCartaTransform(child);
        return new SmoothingPipelineCartaTransform(previous, child, radius, threshold);
    }
}
