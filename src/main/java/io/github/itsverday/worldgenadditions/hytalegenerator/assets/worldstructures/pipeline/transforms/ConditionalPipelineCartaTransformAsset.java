package io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms;

import com.hypixel.hytale.assetstore.AssetExtraInfo;
import com.hypixel.hytale.assetstore.codec.AssetCodecMapCodec;
import com.hypixel.hytale.assetstore.codec.ContainedAssetCodec;
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

    @Nullable
    private ConditionAsset condition;
    @Nullable
    private PipelineCartaTransformAsset ifTrue;
    @Nullable
    private PipelineCartaTransformAsset ifFalse;

    @NonNullDecl
    @Override
    public PipelineCartaTransform build(@NonNullDecl Argument arg, PipelineCartaTransform previous) {
        if (isSkipped()) return previous;
        if (!(previous instanceof CachePipelineCartaTransform)) previous = new CachePipelineCartaTransform(previous);
        return new ConditionalPipelineCartaTransform(previous, condition != null ? condition.build(arg, previous) : null, ifTrue != null ? ifTrue.build(arg, previous) : previous, ifFalse != null ? ifFalse.build(arg, previous) : previous);
    }

    @Override
    public void cleanUp() {
        super.cleanUp();
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
        public abstract ConditionalPipelineCartaTransform.Condition build(@Nonnull Argument arg, PipelineCartaTransform previous);

        public String getId() {
            return this.id;
        }
    }
}
