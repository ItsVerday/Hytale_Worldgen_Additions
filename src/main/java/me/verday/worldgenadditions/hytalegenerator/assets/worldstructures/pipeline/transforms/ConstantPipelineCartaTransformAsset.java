package me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConstantPipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.NonePipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class ConstantPipelineCartaTransformAsset extends PipelineCartaTransformAsset {
    public static final BuilderCodec<ConstantPipelineCartaTransformAsset> CODEC = BuilderCodec.builder(ConstantPipelineCartaTransformAsset.class, ConstantPipelineCartaTransformAsset::new, PipelineCartaTransformAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("Value", Codec.STRING, true), (t, k) -> t.biomeId = k, t -> t.biomeId)
            .add()
            .build();

    private String biomeId;

    @NonNullDecl
    @Override
    public PipelineCartaTransform build(@NonNullDecl Argument arg) {
        if (isSkipped()) return new NonePipelineCartaTransform();
        return new ConstantPipelineCartaTransform(biomeId);
    }
}
