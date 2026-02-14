package me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms.conditions;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms.ConditionalPipelineCartaTransformAsset;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions.BiomeCondition;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class BiomeConditionAsset extends ConditionalPipelineCartaTransformAsset.ConditionAsset {
    public static final BuilderCodec<BiomeConditionAsset> CODEC = BuilderCodec.builder(BiomeConditionAsset.class, BiomeConditionAsset::new, ConditionalPipelineCartaTransformAsset.ConditionAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("Biome", Codec.STRING, true), (t, k) -> t.biomeId = k, t -> t.biomeId)
            .add()
            .build();

    private String biomeId;

    @NonNullDecl
    @Override
    public ConditionalPipelineCartaTransform.Condition build(@NonNullDecl PipelineCartaTransformAsset.Argument arg) {
        return new BiomeCondition(biomeId);
    }

    @Override
    public void cleanUp() {
    }
}
