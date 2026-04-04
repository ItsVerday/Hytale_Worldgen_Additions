package io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.NonePipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.RescalePipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class RescalePipelineCartaTransformAsset extends PipelineCartaTransformAsset {
    public static final BuilderCodec<RescalePipelineCartaTransformAsset> CODEC = BuilderCodec.builder(RescalePipelineCartaTransformAsset.class, RescalePipelineCartaTransformAsset::new, PipelineCartaTransformAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("Scale", Codec.DOUBLE, true), (t, k) -> t.scalingFactor = k, t -> t.scalingFactor)
            .add()
            .build();

    private double scalingFactor;

    @NonNullDecl
    @Override
    public PipelineCartaTransform build(@NonNullDecl Argument arg) {
        if (isSkipped()) return new NonePipelineCartaTransform();

        PipelineCartaTransform child = null;
        if (inputs().length > 0) {
            child = inputs()[0].build(arg);
        }

        return new RescalePipelineCartaTransform(child, scalingFactor);
    }
}
