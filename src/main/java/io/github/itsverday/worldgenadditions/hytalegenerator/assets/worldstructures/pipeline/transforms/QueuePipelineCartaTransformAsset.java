package io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.bouncycastle.util.Arrays;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.List;

public class QueuePipelineCartaTransformAsset extends PipelineCartaTransformAsset {
    public static final BuilderCodec<QueuePipelineCartaTransformAsset> CODEC = BuilderCodec.builder(QueuePipelineCartaTransformAsset.class, QueuePipelineCartaTransformAsset::new, PipelineCartaTransformAsset.ABSTRACT_CODEC)
            .build();

    @NonNullDecl
    @Override
    public PipelineCartaTransform build(@NonNullDecl Argument arg, PipelineCartaTransform previous) {
        if (isSkipped()) return previous;

        for (PipelineCartaTransformAsset asset: List.of(inputs()).reversed()) {
            if (asset.isSkipped()) continue;
            previous = asset.build(arg, previous);
        }

        return previous;
    }
}
