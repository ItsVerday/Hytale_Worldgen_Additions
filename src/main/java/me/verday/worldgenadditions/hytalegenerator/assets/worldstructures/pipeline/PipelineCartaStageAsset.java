package me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline;

import com.hypixel.hytale.assetstore.AssetExtraInfo;
import com.hypixel.hytale.assetstore.codec.AssetBuilderCodec;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.assetstore.map.JsonAssetWithMap;
import com.hypixel.hytale.builtin.hytalegenerator.assets.Cleanable;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaStage;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.CachePipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.NonePipelineCartaTransform;

public class PipelineCartaStageAsset implements Cleanable, JsonAssetWithMap<String, DefaultAssetMap<String, PipelineCartaStageAsset>> {
    public static final AssetBuilderCodec<String, PipelineCartaStageAsset> CODEC = AssetBuilderCodec.builder(PipelineCartaStageAsset.class, PipelineCartaStageAsset::new, Codec.STRING, (asset, id) -> asset.id = id, config -> config.id, (config, data) -> config.data = data, config -> config.data)
            .append(new KeyedCodec<>("Root", PipelineCartaTransformAsset.CODEC, false), (t, k) -> t.root = k, t -> t.root)
            .add()
            .append(new KeyedCodec<>("Skip", Codec.BOOLEAN, false), (t, k) -> t.skip = k, t -> t.skip)
            .add()
            .build();

    private String id;
    private AssetExtraInfo.Data data;

    private PipelineCartaTransformAsset root = null;
    private boolean skip;

    public PipelineCartaStage<Integer> build(PipelineCartaTransformAsset.Argument arg) {
        return new PipelineCartaStage<>(root != null ? new CachePipelineCartaTransform<>(root.build(arg)) : new NonePipelineCartaTransform<>(), isSkipped());
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void cleanUp() {
        if (root != null) root.cleanUp();
    }

    public boolean isSkipped() {
        return skip;
    }
}
