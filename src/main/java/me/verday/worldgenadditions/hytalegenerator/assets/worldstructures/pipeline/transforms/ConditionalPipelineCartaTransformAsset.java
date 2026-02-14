package me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms;

import com.hypixel.hytale.assetstore.AssetExtraInfo;
import com.hypixel.hytale.assetstore.codec.AssetBuilderCodec;
import com.hypixel.hytale.assetstore.codec.AssetCodecMapCodec;
import com.hypixel.hytale.assetstore.codec.ContainedAssetCodec;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.assetstore.map.JsonAssetWithMap;
import com.hypixel.hytale.builtin.hytalegenerator.assets.Cleanable;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConditionalPipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.NonePipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ConditionalPipelineCartaTransformAsset extends PipelineCartaTransformAsset {
    public static final BuilderCodec<ConditionalPipelineCartaTransformAsset> CODEC = BuilderCodec.builder(ConditionalPipelineCartaTransformAsset.class, ConditionalPipelineCartaTransformAsset::new, PipelineCartaTransformAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("Condition", ConditionAsset.CODEC, true), (t, k) -> t.condition = k, t -> t.condition)
            .add()
            .append(new KeyedCodec<>("IfTrue", PipelineCartaTransformAsset.CODEC, false), (t, k) -> t.ifTrue = k, t -> t.ifTrue)
            .add()
            .append(new KeyedCodec<>("IfFalse", PipelineCartaTransformAsset.CODEC, false), (t, k) -> t.ifFalse = k, t -> t.ifFalse)
            .add()
            .build();

    private ConditionAsset condition;
    @Nullable
    private PipelineCartaTransformAsset ifTrue;
    @Nullable
    private PipelineCartaTransformAsset ifFalse;

    @NonNullDecl
    @Override
    public PipelineCartaTransform build(@NonNullDecl Argument arg) {
        if (isSkipped()) return new NonePipelineCartaTransform();
        return new ConditionalPipelineCartaTransform(condition.build(arg), ifTrue != null ? ifTrue.build(arg) : null, ifFalse != null ? ifFalse.build(arg) : null);
    }

    @Override
    public void cleanUp() {
        if (ifTrue != null) ifTrue.cleanUp();
        if (ifFalse != null) ifFalse.cleanUp();
        condition.cleanUp();
    }

    public abstract static class ConditionAsset implements Cleanable, JsonAssetWithMap<String, DefaultAssetMap<String, ConditionAsset>> {
        public static final AssetCodecMapCodec<String, ConditionAsset> CODEC = new AssetCodecMapCodec<>(Codec.STRING, (t, k) -> t.id = k, t -> t.id, (t, k) -> t.data = k, t -> t.data);
        public static final Codec<String> CHILD_ASSET_CODEC = new ContainedAssetCodec<>(ConditionAsset.class, CODEC);
        public static final Codec<String[]> CHILD_ASSET_CODEC_ARRAY = new ArrayCodec<>(CHILD_ASSET_CODEC, String[]::new);
        public static final BuilderCodec<ConditionAsset> ABSTRACT_CODEC = BuilderCodec.abstractBuilder(ConditionAsset.class)
                .build();

        private String id;
        private AssetExtraInfo.Data data;

        protected ConditionAsset() {
        }

        @Nonnull
        public abstract ConditionalPipelineCartaTransform.Condition build(@Nonnull Argument arg);

        public String getId() {
            return this.id;
        }
    }
}
