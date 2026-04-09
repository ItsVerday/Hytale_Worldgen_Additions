package io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms;

import com.hypixel.hytale.assetstore.AssetExtraInfo;
import com.hypixel.hytale.assetstore.codec.AssetBuilderCodec;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.assetstore.map.JsonAssetWithMap;
import com.hypixel.hytale.builtin.hytalegenerator.assets.Cleanable;
import com.hypixel.hytale.builtin.hytalegenerator.assets.density.DensityAsset;
import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.MultiFieldFunctionPipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.ArrayList;
import java.util.List;

public class MultiFieldFunctionPipelineCartaTransformAsset extends PipelineCartaTransformAsset {
    public static final BuilderCodec<MultiFieldFunctionPipelineCartaTransformAsset> CODEC = BuilderCodec.builder(MultiFieldFunctionPipelineCartaTransformAsset.class, MultiFieldFunctionPipelineCartaTransformAsset::new, PipelineCartaTransformAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("FieldFunctions", new ArrayCodec<>(DensityAsset.CODEC, DensityAsset[]::new), true), (t, k) -> t.dimensions = k, t -> t.dimensions)
            .add()
            .append(new KeyedCodec<>("Entries", new ArrayCodec<>(EntryAsset.CODEC, EntryAsset[]::new), true), (t, k) -> t.entryAssets = k, t -> t.entryAssets)
            .add()
            .build();

    private DensityAsset[] dimensions = new DensityAsset[0];
    private EntryAsset[] entryAssets = new EntryAsset[0];

    @NonNullDecl
    @Override
    public PipelineCartaTransform build(@NonNullDecl Argument arg, PipelineCartaTransform previous) {
        if (isSkipped()) return previous;

        DensityAsset.Argument densityArg = new DensityAsset.Argument(arg.parentSeed, arg.referenceBundle, arg.workerId);

        List<Density> dimensions = new ArrayList<>();
        for (DensityAsset densityAsset: this.dimensions) {
            dimensions.add(densityAsset.build(densityArg));
        }

        List<MultiFieldFunctionPipelineCartaTransform.Entry> entries = new ArrayList<>();
        for (EntryAsset entryAsset: entryAssets) {
            PipelineCartaTransform node = entryAsset.transform != null ? entryAsset.transform.build(arg, previous) : previous;
            MultiFieldFunctionPipelineCartaTransform.Entry entry = new MultiFieldFunctionPipelineCartaTransform.Entry(node, entryAsset.center, entryAsset.maximumDistance);
            entries.add(entry);
        }

        return new MultiFieldFunctionPipelineCartaTransform(previous, dimensions, entries);
    }

    @Override
    public void cleanUp() {
        super.cleanUp();

        for (EntryAsset entryAsset: entryAssets) {
            entryAsset.cleanUp();
        }
    }

    public static class EntryAsset implements Cleanable, JsonAssetWithMap<String, DefaultAssetMap<String, FieldFunctionPipelineCartaTransformAsset.DelimiterAsset>> {
        public static final AssetBuilderCodec<String, EntryAsset> CODEC = AssetBuilderCodec.builder(EntryAsset.class, EntryAsset::new, Codec.STRING, (asset, id) -> asset.id = id, config -> config.id, (config, data) -> config.data = data, config -> config.data)
                .append(new KeyedCodec<>("Center", Codec.DOUBLE_ARRAY, true), (t, center) -> t.center = center, t -> t.center)
                .add()
                .append(new KeyedCodec<>("MaximumDistance", Codec.DOUBLE, true), (t, maximumDistance) -> t.maximumDistance = maximumDistance, t -> t.maximumDistance)
                .add()
                .append(new KeyedCodec<>("Biome", PipelineCartaTransformAsset.CODEC, false), (t, out) -> t.transform = out, t -> t.transform)
                .add()
                .build();

        private String id;
        private AssetExtraInfo.Data data;
        private double[] center;
        private double maximumDistance;
        private PipelineCartaTransformAsset transform = new ConstantPipelineCartaTransformAsset();

        @Override
        public String getId() {
            return id;
        }

        @Override
        public void cleanUp() {
            if (transform != null) transform.cleanUp();
        }
    }
}
