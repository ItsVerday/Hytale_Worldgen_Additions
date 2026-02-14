package me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms.conditions;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms.ConditionalPipelineCartaTransformAsset;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions.ValueDistanceCondition;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class BiomeDistanceConditionAsset extends ConditionalPipelineCartaTransformAsset.ConditionAsset {
    public static final BuilderCodec<BiomeDistanceConditionAsset> CODEC = BuilderCodec.builder(BiomeDistanceConditionAsset.class, BiomeDistanceConditionAsset::new, ConditionalPipelineCartaTransformAsset.ConditionAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("Biome", Codec.STRING, true), (t, k) -> t.biomeId = k, t -> t.biomeId)
            .add()
            .append(new KeyedCodec<>("Distance", Codec.DOUBLE, true), (t, k) -> t.distance = k, t -> t.distance)
            .add()
            .build();

    private String biomeId;
    private double distance;

    @NonNullDecl
    @Override
    public ConditionalPipelineCartaTransform.Condition<String> build(@NonNullDecl PipelineCartaTransformAsset.Argument arg) {
        return new ValueDistanceCondition<>(biomeId, distance);
    }

    @Override
    public void cleanUp() {
    }
}
