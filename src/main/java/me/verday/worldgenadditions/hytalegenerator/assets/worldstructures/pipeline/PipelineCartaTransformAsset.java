package me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline;

import com.hypixel.hytale.assetstore.AssetExtraInfo;
import com.hypixel.hytale.assetstore.codec.AssetCodecMapCodec;
import com.hypixel.hytale.assetstore.codec.ContainedAssetCodec;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.assetstore.map.JsonAssetWithMap;
import com.hypixel.hytale.builtin.hytalegenerator.assets.Cleanable;
import com.hypixel.hytale.builtin.hytalegenerator.material.MaterialCache;
import com.hypixel.hytale.builtin.hytalegenerator.referencebundle.ReferenceBundle;
import com.hypixel.hytale.builtin.hytalegenerator.seed.SeedBox;
import com.hypixel.hytale.builtin.hytalegenerator.threadindexer.WorkerIndexer;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.util.FastReadIntegerCache;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class PipelineCartaTransformAsset implements Cleanable, JsonAssetWithMap<String, DefaultAssetMap<String, PipelineCartaTransformAsset>> {
    private static final PipelineCartaTransformAsset[] EMPTY_INPUTS = new PipelineCartaTransformAsset[0];
    public static final AssetCodecMapCodec<String, PipelineCartaTransformAsset> CODEC = new AssetCodecMapCodec<>(Codec.STRING, (t, k) -> t.id = k, t -> t.id, (t, k) -> t.data = k, t -> t.data);
    public static final Codec<String> CHILD_ASSET_CODEC = new ContainedAssetCodec<>(PipelineCartaTransformAsset.class, CODEC);
    public static final Codec<String[]> CHILD_ASSET_CODEC_ARRAY = new ArrayCodec<>(CHILD_ASSET_CODEC, String[]::new);
    public static final BuilderCodec<PipelineCartaTransformAsset> ABSTRACT_CODEC = BuilderCodec.abstractBuilder(PipelineCartaTransformAsset.class)
            .append(new KeyedCodec<>("Inputs", new ArrayCodec<>(CODEC, PipelineCartaTransformAsset[]::new), false), (t, k) -> t.inputs = k, t -> t.inputs)
            .add()
            .append(new KeyedCodec<>("Skip", Codec.BOOLEAN, false), (t, k) -> t.skip = k, t -> t.skip)
            .add()
            .build();

    private String id;
    private AssetExtraInfo.Data data;
    private PipelineCartaTransformAsset[] inputs = EMPTY_INPUTS;
    private boolean skip = false;

    protected PipelineCartaTransformAsset() {
    }

    @Nonnull
    public abstract PipelineCartaTransform<Integer> build(@Nonnull Argument arg);

    @Override
    public void cleanUp() {
        this.cleanUpInputs();
    }

    protected void cleanUpInputs() {
        for (PipelineCartaTransformAsset input: inputs) {
            input.cleanUp();
        }
    }

    public PipelineCartaTransformAsset[] inputs() {
        return inputs;
    }

    @Nonnull
    public List<PipelineCartaTransform<Integer>> buildInputs(@Nonnull Argument argument, boolean excludeSkipped) {
        ArrayList<PipelineCartaTransform<Integer>> nodes = new ArrayList<>();

        for (PipelineCartaTransformAsset asset: inputs) {
            if (!excludeSkipped || !asset.isSkipped()) {
                nodes.add(asset.build(argument));
            }
        }

        return nodes;
    }

    public boolean isSkipped() {
        return skip;
    }

    @Override
    public String getId() {
        return id;
    }

    public static class Argument {
        public MaterialCache materialCache;
        public SeedBox parentSeed;
        public ReferenceBundle referenceBundle;
        public WorkerIndexer workerIndexer;
        public FastReadIntegerCache<String> biomeIds;

        public Argument(@Nonnull MaterialCache materialCache, @Nonnull SeedBox parentSeed, @Nonnull ReferenceBundle referenceBundle, @Nonnull WorkerIndexer workerIndexer) {
            this.materialCache = materialCache;
            this.parentSeed = parentSeed;
            this.referenceBundle = referenceBundle;
            this.workerIndexer = workerIndexer;
            biomeIds = new FastReadIntegerCache<>();
        }

        public Argument(@Nonnull Argument argument) {
            this.materialCache = argument.materialCache;
            this.parentSeed = argument.parentSeed;
            this.referenceBundle = argument.referenceBundle;
            this.workerIndexer = argument.workerIndexer;
            this.biomeIds = argument.biomeIds;
        }

        public int cacheBiomeId(String biomeId) {
            return biomeIds.add(biomeId);
        }
    }
}
