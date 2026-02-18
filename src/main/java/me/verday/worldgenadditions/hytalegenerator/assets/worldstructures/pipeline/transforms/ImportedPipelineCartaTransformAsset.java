package me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms;

import com.hypixel.hytale.builtin.hytalegenerator.LoggerUtil;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.NonePipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class ImportedPipelineCartaTransformAsset extends PipelineCartaTransformAsset {
    public static final BuilderCodec<ImportedPipelineCartaTransformAsset> CODEC = BuilderCodec.builder(ImportedPipelineCartaTransformAsset.class, ImportedPipelineCartaTransformAsset::new, PipelineCartaTransformAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("Name", Codec.STRING, true), (t, k) -> t.importedNodeName = k, t -> t.importedNodeName)
            .add()
            .build();

    private String importedNodeName = "";

    @NonNullDecl
    @Override
    public PipelineCartaTransform<Integer> build(@NonNullDecl Argument arg) {
        if (isSkipped()) return new NonePipelineCartaTransform<>();

        Exported exported = getExportedAsset(importedNodeName);
        if (exported == null) {
            LoggerUtil.getLogger().warning("Couldn't find PipelineCartaTransform asset exported with name: '" + importedNodeName + "'. Using empty Node instead.");
            return new NonePipelineCartaTransform<>();
        }

        return exported.asset.build(arg);
    }
}
