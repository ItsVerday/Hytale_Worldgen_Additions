package me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms;

import com.hypixel.hytale.assetstore.AssetExtraInfo;
import com.hypixel.hytale.assetstore.codec.AssetBuilderCodec;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.assetstore.map.JsonAssetWithMap;
import com.hypixel.hytale.builtin.hytalegenerator.assets.Cleanable;
import com.hypixel.hytale.builtin.hytalegenerator.assets.density.positions.distancefunctions.DistanceFunctionAsset;
import com.hypixel.hytale.builtin.hytalegenerator.assets.positionproviders.PositionProviderAsset;
import com.hypixel.hytale.builtin.hytalegenerator.seed.SeedBox;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.NonePipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.PositionsCellNoisePipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.ArrayList;

public class PositionsCellNoisePipelineCartaTransformAsset extends PipelineCartaTransformAsset {
    public static final BuilderCodec<PositionsCellNoisePipelineCartaTransformAsset> CODEC = BuilderCodec.builder(PositionsCellNoisePipelineCartaTransformAsset.class, PositionsCellNoisePipelineCartaTransformAsset::new, PipelineCartaTransformAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("Seed", Codec.STRING, true), (t, k) -> t.seed = k, t -> t.seed)
            .add()
            .append(new KeyedCodec<>("Positions", PositionProviderAsset.CODEC, true), (t, k) -> t.positions = k, t -> t.positions)
            .add()
            .append(new KeyedCodec<>("DistanceFunction", DistanceFunctionAsset.CODEC, true), (t, k) -> t.distanceFunction = k, t -> t.distanceFunction)
            .add()
            .append(new KeyedCodec<>("CellValues", new ArrayCodec<>(CellValueAsset.CODEC, CellValueAsset[]::new), true), (t, k) -> t.cellValues = k, t -> t.cellValues)
            .add()
            .append(new KeyedCodec<>("MaxDistance", Codec.DOUBLE, true), (t, k) -> t.maxDistance = k, t -> t.maxDistance)
            .add()
            .build();

    private String seed;
    private PositionProviderAsset positions;
    private DistanceFunctionAsset distanceFunction;
    private CellValueAsset[] cellValues;
    private double maxDistance;

    @NonNullDecl
    @Override
    public PipelineCartaTransform<Integer> build(@NonNullDecl Argument arg) {
        if (isSkipped()) return new NonePipelineCartaTransform<>();

        SeedBox childSeed = arg.parentSeed.child(seed);
        ArrayList<PositionsCellNoisePipelineCartaTransform.CellValue<Integer>> finalCellValues = new ArrayList<>();
        for (CellValueAsset cellValue: cellValues) {
            finalCellValues.add(new PositionsCellNoisePipelineCartaTransform.CellValue<>(cellValue.weight, cellValue.transform != null ? cellValue.transform.build(arg) : new NonePipelineCartaTransform<>()));
        }

        return new PositionsCellNoisePipelineCartaTransform<>(childSeed.createSupplier().get(), positions.build(new PositionProviderAsset.Argument(arg.parentSeed, arg.referenceBundle, arg.workerIndexer)), distanceFunction.build(arg.parentSeed, maxDistance), finalCellValues, maxDistance);
    }

    @Override
    public void cleanUp() {
        positions.cleanUp();
        for (CellValueAsset cellValue: cellValues) {
            cellValue.cleanUp();
        }
    }

    public static class CellValueAsset implements Cleanable, JsonAssetWithMap<String, DefaultAssetMap<String, CellValueAsset>> {
        public static final AssetBuilderCodec<String, CellValueAsset> CODEC = AssetBuilderCodec.builder(CellValueAsset.class, CellValueAsset::new, Codec.STRING, (asset, id) -> asset.id = id, config -> config.id, (config, data) -> config.data = data, config -> config.data)
                .append(new KeyedCodec<>("Biome", PipelineCartaTransformAsset.CODEC, false), (t, out) -> t.transform = out, t -> t.transform)
                .add()
                .append(new KeyedCodec<>("Weight", Codec.DOUBLE, true), (t, k) -> t.weight = k, t -> t.weight)
                .add()
                .build();

        private String id;
        private AssetExtraInfo.Data data;

        private double weight;
        private PipelineCartaTransformAsset transform;

        public String getId() {
            return this.id;
        }

        @Override
        public void cleanUp() {
            if (transform != null) transform.cleanUp();
        }
    }
}
