package me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms.conditions;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms.ConditionalPipelineCartaTransformAsset;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions.NoneCondition;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions.NotCondition;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class NotConditionAsset extends ConditionalPipelineCartaTransformAsset.ConditionAsset {
    public static final BuilderCodec<NotConditionAsset> CODEC = BuilderCodec.builder(NotConditionAsset.class, NotConditionAsset::new, ConditionalPipelineCartaTransformAsset.ConditionAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("Condition", ConditionalPipelineCartaTransformAsset.ConditionAsset.CODEC, false), (t, k) -> t.condition = k, t -> t.condition)
            .add()
            .build();

    private ConditionalPipelineCartaTransformAsset.ConditionAsset condition;

    @NonNullDecl
    @Override
    public ConditionalPipelineCartaTransform.Condition<Integer> build(@NonNullDecl PipelineCartaTransformAsset.Argument arg) {
        if (condition == null) return new NoneCondition<>();

        return new NotCondition<>(condition.build(arg));
    }

    @Override
    public void cleanUp() {
        condition.cleanUp();
    }
}
