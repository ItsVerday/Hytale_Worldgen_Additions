package me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms.conditions;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms.ConditionalPipelineCartaTransformAsset;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions.AndCondition;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions.NoneCondition;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.ArrayList;

public class AndConditionAsset extends ConditionalPipelineCartaTransformAsset.ConditionAsset {
    public static final BuilderCodec<AndConditionAsset> CODEC = BuilderCodec.builder(AndConditionAsset.class, AndConditionAsset::new, ConditionalPipelineCartaTransformAsset.ConditionAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("Conditions", new ArrayCodec<>(ConditionalPipelineCartaTransformAsset.ConditionAsset.CODEC, ConditionalPipelineCartaTransformAsset.ConditionAsset[]::new), false), (t, k) -> t.conditions = k, t -> t.conditions)
            .add()
            .build();

    private ConditionalPipelineCartaTransformAsset.ConditionAsset[] conditions;

    @NonNullDecl
    @Override
    public ConditionalPipelineCartaTransform.Condition<Integer> build(@NonNullDecl PipelineCartaTransformAsset.Argument arg) {
        if (conditions == null) return new NoneCondition<>();

        ArrayList<ConditionalPipelineCartaTransform.Condition<Integer>> newConditions = new ArrayList<>();
        for (ConditionalPipelineCartaTransformAsset.ConditionAsset condition: conditions) {
            newConditions.add(condition.build(arg));
        }

        return new AndCondition<>(newConditions);
    }

    @Override
    public void cleanUp() {
        for (ConditionalPipelineCartaTransformAsset.ConditionAsset condition: conditions) {
            condition.cleanUp();
        }
    }
}
