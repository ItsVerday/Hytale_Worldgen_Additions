package me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms;

import com.hypixel.hytale.assetstore.AssetExtraInfo;
import com.hypixel.hytale.assetstore.codec.AssetBuilderCodec;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.assetstore.map.JsonAssetWithMap;
import com.hypixel.hytale.builtin.hytalegenerator.assets.Cleanable;
import com.hypixel.hytale.builtin.hytalegenerator.assets.density.ConstantDensityAsset;
import com.hypixel.hytale.builtin.hytalegenerator.assets.density.DensityAsset;
import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.FieldFunctionPipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.NonePipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.ArrayList;

public class FieldFunctionPipelineCartaTransformAsset extends PipelineCartaTransformAsset {
    public static final BuilderCodec<FieldFunctionPipelineCartaTransformAsset> CODEC = BuilderCodec.builder(FieldFunctionPipelineCartaTransformAsset.class, FieldFunctionPipelineCartaTransformAsset::new, PipelineCartaTransformAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("FieldFunction", DensityAsset.CODEC, true), (t, k) -> t.densityAsset = k, t -> t.densityAsset)
            .add()
            .append(new KeyedCodec<>("Delimiters", new ArrayCodec<>(DelimiterAsset.CODEC, DelimiterAsset[]::new),true),
                    (t, k) -> t.delimiterAssets = k,
                    k -> k.delimiterAssets
            )
            .add()
            .build();

    private DensityAsset densityAsset = new ConstantDensityAsset();
    private DelimiterAsset[] delimiterAssets = new DelimiterAsset[0];

    @NonNullDecl
    @Override
    public PipelineCartaTransform<String> build(@NonNullDecl Argument arg) {
        if (isSkipped()) return new NonePipelineCartaTransform<>();

        Density functionTree = densityAsset.build(new DensityAsset.Argument(arg.parentSeed, arg.referenceBundle, arg.workerIndexer));
        ArrayList<FieldFunctionPipelineCartaTransform.FieldDelimiter<String>> delimiters = new ArrayList<>(delimiterAssets.length);

        for (DelimiterAsset delimiterAsset: delimiterAssets) {
            PipelineCartaTransform<String> node = delimiterAsset.transform.build(arg);
            FieldFunctionPipelineCartaTransform.FieldDelimiter<String> delimiter = new FieldFunctionPipelineCartaTransform.FieldDelimiter<>(node, delimiterAsset.from, delimiterAsset.to);
            delimiters.add(delimiter);
        }

        return new FieldFunctionPipelineCartaTransform<>(functionTree, delimiters);
    }

    @Override
    public void cleanUp() {
        this.densityAsset.cleanUp();

        for (DelimiterAsset delimiterAsset: delimiterAssets) {
            delimiterAsset.cleanUp();
        }
    }

    public static class DelimiterAsset implements Cleanable, JsonAssetWithMap<String, DefaultAssetMap<String, DelimiterAsset>> {
        public static final AssetBuilderCodec<String, DelimiterAsset> CODEC = AssetBuilderCodec.builder(DelimiterAsset.class, DelimiterAsset::new, Codec.STRING, (asset, id) -> asset.id = id, config -> config.id, (config, data) -> config.data = data, config -> config.data)
                .append(new KeyedCodec<>("From", Codec.DOUBLE, true), (t, y) -> t.from = y, t -> t.from)
                .add()
                .append(new KeyedCodec<>("To", Codec.DOUBLE, true), (t, out) -> t.to = out, t -> t.to)
                .add()
                .append(new KeyedCodec<>("Biome", PipelineCartaTransformAsset.CODEC, true), (t, out) -> t.transform = out, t -> t.transform)
                .add()
                .build();

        private String id;
        private AssetExtraInfo.Data data;
        private double from = 0.0;
        private double to = 0.0;
        private PipelineCartaTransformAsset transform = new ConstantPipelineCartaTransformAsset();

        public String getId() {
            return this.id;
        }

        @Override
        public void cleanUp() {
            this.transform.cleanUp();
        }
    }
}
