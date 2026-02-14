package me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.NonePipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.QueuePipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class QueuePipelineCartaTransformAsset extends PipelineCartaTransformAsset {
    public static final BuilderCodec<QueuePipelineCartaTransformAsset> CODEC = BuilderCodec.builder(QueuePipelineCartaTransformAsset.class, QueuePipelineCartaTransformAsset::new, PipelineCartaTransformAsset.ABSTRACT_CODEC)
            .build();

    @NonNullDecl
    @Override
    public PipelineCartaTransform<String> build(@NonNullDecl Argument arg) {
        if (isSkipped()) return new NonePipelineCartaTransform<>();
        return new QueuePipelineCartaTransform<>(buildInputs(arg, false));
    }
}
