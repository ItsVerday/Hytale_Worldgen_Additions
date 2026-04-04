package io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms;

import com.hypixel.hytale.builtin.hytalegenerator.LoggerUtil;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class ImportedPipelineCartaTransformAsset extends PipelineCartaTransformAsset {
    public static final BuilderCodec<ImportedPipelineCartaTransformAsset> CODEC = BuilderCodec.builder(ImportedPipelineCartaTransformAsset.class, ImportedPipelineCartaTransformAsset::new, PipelineCartaTransformAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("Name", Codec.STRING, true), (t, k) -> t.importedNodeName = k, t -> t.importedNodeName)
            .add()
            .build();

    private String importedNodeName = "";

    @NonNullDecl
    @Override
    public PipelineCartaTransform build(@NonNullDecl Argument arg, PipelineCartaTransform previous) {
        if (isSkipped()) return previous;

        Exported exported = getExportedAsset(importedNodeName);
        if (exported == null) {
            LoggerUtil.getLogger().warning("Couldn't find PipelineCartaTransform asset exported with name: '" + importedNodeName + "'. Using empty Node instead.");
            return previous;
        }

        return exported.asset.build(arg, previous);
    }
}
