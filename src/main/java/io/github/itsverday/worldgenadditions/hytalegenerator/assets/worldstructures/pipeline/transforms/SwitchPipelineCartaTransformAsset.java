package io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms;

import com.hypixel.hytale.assetstore.AssetExtraInfo;
import com.hypixel.hytale.assetstore.codec.AssetBuilderCodec;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.assetstore.map.JsonAssetWithMap;
import com.hypixel.hytale.builtin.hytalegenerator.assets.Cleanable;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.CachePipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.List;

public class SwitchPipelineCartaTransformAsset extends PipelineCartaTransformAsset {
    public static final BuilderCodec<SwitchPipelineCartaTransformAsset> CODEC = BuilderCodec.builder(SwitchPipelineCartaTransformAsset.class, SwitchPipelineCartaTransformAsset::new, PipelineCartaTransformAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("Cases", new ArrayCodec<>(CaseAsset.CODEC, CaseAsset[]::new), true), (t, k) -> t.cases = k, t -> t.cases)
            .add()
            .build();

    private CaseAsset[] cases = new CaseAsset[0];

    @NonNullDecl
    @Override
    public PipelineCartaTransform build(@NonNullDecl Argument arg, PipelineCartaTransform previous) {
        if (isSkipped()) return previous;
        if (!(previous instanceof CachePipelineCartaTransform)) previous = new CachePipelineCartaTransform(previous);

        PipelineCartaTransform ifFalse = previous;
        for (CaseAsset caseAsset: List.of(cases).reversed()) {
            if (caseAsset.skip) continue;
            ifFalse = new ConditionalPipelineCartaTransform(previous, caseAsset.condition.build(arg, previous), caseAsset.ifTrue.build(arg, previous), ifFalse);
        }

        return ifFalse;
    }

    @Override
    public void cleanUp() {
        for (CaseAsset caseAsset: cases) {
            caseAsset.cleanUp();
        }
    }

    public static class CaseAsset implements Cleanable, JsonAssetWithMap<String, DefaultAssetMap<String, SwitchPipelineCartaTransformAsset.CaseAsset>> {
        public static final AssetBuilderCodec<String, SwitchPipelineCartaTransformAsset.CaseAsset> CODEC = AssetBuilderCodec.builder(SwitchPipelineCartaTransformAsset.CaseAsset.class, SwitchPipelineCartaTransformAsset.CaseAsset::new, Codec.STRING, (asset, id) -> asset.id = id, config -> config.id, (config, data) -> config.data = data, config -> config.data)
                .append(new KeyedCodec<>("Skip", Codec.BOOLEAN, false), (t, k) -> t.skip = k, t -> t.skip)
                .add()
                .append(new KeyedCodec<>("Condition", ConditionalPipelineCartaTransformAsset.ConditionAsset.CODEC, false), (t, k) -> t.condition = k, t -> t.condition)
                .add()
                .append(new KeyedCodec<>("IfTrue", PipelineCartaTransformAsset.CODEC, false), (t, k) -> t.ifTrue = k, t -> t.ifTrue)
                .add()
                .build();

        private String id;
        private AssetExtraInfo.Data data;

        private boolean skip;
        private ConditionalPipelineCartaTransformAsset.ConditionAsset condition;
        private PipelineCartaTransformAsset ifTrue;

        @Override
        public String getId() {
            return id;
        }

        @Override
        public void cleanUp() {
            if (condition != null) condition.cleanUp();
            if (ifTrue != null) ifTrue.cleanUp();
        }
    }
}
