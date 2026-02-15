package me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms.conditions;

import com.hypixel.hytale.builtin.hytalegenerator.assets.density.DensityAsset;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms.ConditionalPipelineCartaTransformAsset;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions.ValueDistanceDensityCondition;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class BiomeDistanceDensityConditionAsset extends ConditionalPipelineCartaTransformAsset.ConditionAsset {
    public static final BuilderCodec<BiomeDistanceDensityConditionAsset> CODEC = BuilderCodec.builder(BiomeDistanceDensityConditionAsset.class, BiomeDistanceDensityConditionAsset::new, ConditionalPipelineCartaTransformAsset.ConditionAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("Biome", Codec.STRING, true), (t, k) -> t.biomeId = k, t -> t.biomeId)
            .add()
            .append(new KeyedCodec<>("DistanceMin", Codec.DOUBLE, true), (t, k) -> t.distanceMin = k, t -> t.distanceMin)
            .add()
            .append(new KeyedCodec<>("DistanceMax", Codec.DOUBLE, true), (t, k) -> t.distanceMax = k, t -> t.distanceMax)
            .add()
            .append(new KeyedCodec<>("Density", DensityAsset.CODEC, true), (t, k) -> t.densityAsset = k, t -> t.densityAsset)
            .add()
            .build();

    private String biomeId;
    private double distanceMin;
    private double distanceMax;
    private DensityAsset densityAsset;

    @NonNullDecl
    @Override
    public ConditionalPipelineCartaTransform.Condition<String> build(@NonNullDecl PipelineCartaTransformAsset.Argument arg) {
        return new ValueDistanceDensityCondition<>(biomeId, distanceMin, distanceMax, densityAsset.build(new DensityAsset.Argument(arg.parentSeed, arg.referenceBundle, arg.workerIndexer)));
    }

    @Override
    public void cleanUp() {
        densityAsset.cleanUp();
    }
}
