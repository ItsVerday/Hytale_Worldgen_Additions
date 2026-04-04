package io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.NonePipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.QueuePipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class QueuePipelineCartaTransformAsset extends PipelineCartaTransformAsset {
    public static final BuilderCodec<QueuePipelineCartaTransformAsset> CODEC = BuilderCodec.builder(QueuePipelineCartaTransformAsset.class, QueuePipelineCartaTransformAsset::new, PipelineCartaTransformAsset.ABSTRACT_CODEC)
            .build();

    @NonNullDecl
    @Override
    public PipelineCartaTransform build(@NonNullDecl Argument arg) {
        if (isSkipped()) return new NonePipelineCartaTransform();
        return new QueuePipelineCartaTransform(buildInputs(arg, true));
    }
}
