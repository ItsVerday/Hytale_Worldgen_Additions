package io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms.conditions;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms.ConditionalPipelineCartaTransformAsset;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions.OrCondition;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.ArrayList;
import java.util.List;

public class OrConditionAsset extends ConditionalPipelineCartaTransformAsset.ConditionAsset {
    public static final BuilderCodec<OrConditionAsset> CODEC = BuilderCodec.builder(OrConditionAsset.class, OrConditionAsset::new, ConditionalPipelineCartaTransformAsset.ConditionAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("Conditions", new ArrayCodec<>(ConditionalPipelineCartaTransformAsset.ConditionAsset.CODEC, ConditionalPipelineCartaTransformAsset.ConditionAsset[]::new), false), (t, k) -> t.conditions = k, t -> t.conditions)
            .add()
            .build();

    private ConditionalPipelineCartaTransformAsset.ConditionAsset[] conditions;

    @NonNullDecl
    @Override
    public ConditionalPipelineCartaTransform.Condition build(@NonNullDecl PipelineCartaTransformAsset.Argument arg, PipelineCartaTransform previous) {
        if (conditions == null) return new OrCondition(List.of());

        ArrayList<ConditionalPipelineCartaTransform.Condition> newConditions = new ArrayList<>();
        for (ConditionalPipelineCartaTransformAsset.ConditionAsset condition: conditions) {
            newConditions.add(condition.build(arg, previous));
        }

        return new OrCondition(newConditions);
    }

    @Override
    public void cleanUp() {
        for (ConditionalPipelineCartaTransformAsset.ConditionAsset condition: conditions) {
            condition.cleanUp();
        }
    }
}
