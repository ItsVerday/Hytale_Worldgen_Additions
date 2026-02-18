package me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.CachePipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.NonePipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class CachePipelineCartaTransformAsset extends PipelineCartaTransformAsset {
    public static final BuilderCodec<CachePipelineCartaTransformAsset> CODEC = BuilderCodec.builder(CachePipelineCartaTransformAsset.class, CachePipelineCartaTransformAsset::new, PipelineCartaTransformAsset.ABSTRACT_CODEC)
            .build();

    @NonNullDecl
    @Override
    public PipelineCartaTransform<Integer> build(@NonNullDecl Argument arg) {
        if (isSkipped()) return new NonePipelineCartaTransform<>();

        PipelineCartaTransform<Integer> child = null;
        if (inputs().length > 0) {
            child = inputs()[0].build(arg);
        }

        return new CachePipelineCartaTransform<>(child);
    }
}
