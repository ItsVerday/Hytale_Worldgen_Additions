package me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms.conditions;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms.ConditionalPipelineCartaTransformAsset;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions.DistanceCondition;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions.NoneCondition;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class DistanceConditionAsset extends ConditionalPipelineCartaTransformAsset.ConditionAsset {
    public static final BuilderCodec<DistanceConditionAsset> CODEC = BuilderCodec.builder(DistanceConditionAsset.class, DistanceConditionAsset::new, ConditionalPipelineCartaTransformAsset.ConditionAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("Condition", ConditionalPipelineCartaTransformAsset.ConditionAsset.CODEC, false), (t, k) -> t.condition = k, t -> t.condition)
            .add()
            .append(new KeyedCodec<>("Distance", Codec.DOUBLE, true), (t, k) -> t.distance = k, t -> t.distance)
            .add()
            .build();

    private ConditionalPipelineCartaTransformAsset.ConditionAsset condition;
    private double distance;

    @NonNullDecl
    @Override
    public ConditionalPipelineCartaTransform.Condition<Integer> build(@NonNullDecl PipelineCartaTransformAsset.Argument arg) {
        if (condition == null) return new NoneCondition<>();

        return new DistanceCondition<>(arg.workerIndexer, condition.build(arg), distance);
    }

    @Override
    public void cleanUp() {
    }
}
