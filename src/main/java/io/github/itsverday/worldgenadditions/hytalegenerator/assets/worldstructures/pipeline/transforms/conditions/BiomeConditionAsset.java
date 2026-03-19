package io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms.conditions;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms.ConditionalPipelineCartaTransformAsset;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions.ValueCondition;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class BiomeConditionAsset extends ConditionalPipelineCartaTransformAsset.ConditionAsset {
    public static final BuilderCodec<BiomeConditionAsset> CODEC = BuilderCodec.builder(BiomeConditionAsset.class, BiomeConditionAsset::new, ConditionalPipelineCartaTransformAsset.ConditionAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("Biome", Codec.STRING, true), (t, k) -> t.biomeId = k, t -> t.biomeId)
            .add()
            .build();

    private String biomeId;

    @NonNullDecl
    @Override
    public ConditionalPipelineCartaTransform.Condition<Integer> build(@NonNullDecl PipelineCartaTransformAsset.Argument arg) {
        return new ValueCondition<>(arg.cacheBiomeId(biomeId));
    }

    @Override
    public void cleanUp() {
    }
}
