package io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.CachePipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.NonePipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class CachePipelineCartaTransformAsset extends PipelineCartaTransformAsset {
    public static final BuilderCodec<CachePipelineCartaTransformAsset> CODEC = BuilderCodec.builder(CachePipelineCartaTransformAsset.class, CachePipelineCartaTransformAsset::new, PipelineCartaTransformAsset.ABSTRACT_CODEC)
            .build();

    @NonNullDecl
    @Override
    public PipelineCartaTransform build(@NonNullDecl Argument arg) {
        if (isSkipped()) return new NonePipelineCartaTransform();

        PipelineCartaTransform child = null;
        if (inputs().length > 0) {
            child = inputs()[0].build(arg);
        }

        return new CachePipelineCartaTransform(child);
    }
}
