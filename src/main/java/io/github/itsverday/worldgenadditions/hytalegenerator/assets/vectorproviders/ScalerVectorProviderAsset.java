package io.github.itsverday.worldgenadditions.hytalegenerator.assets.vectorproviders;

import com.hypixel.hytale.builtin.hytalegenerator.assets.vectorproviders.VectorProviderAsset;
import com.hypixel.hytale.builtin.hytalegenerator.vectorproviders.ConstantVectorProvider;
import com.hypixel.hytale.builtin.hytalegenerator.vectorproviders.VectorProvider;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import io.github.itsverday.worldgenadditions.hytalegenerator.vectorproviders.ScalerVectorProvider;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.joml.Vector3d;

public class ScalerVectorProviderAsset extends VectorProviderAsset {
    public static final BuilderCodec<ScalerVectorProviderAsset> CODEC = BuilderCodec.builder(
            ScalerVectorProviderAsset.class, ScalerVectorProviderAsset::new, VectorProviderAsset.ABSTRACT_CODEC
    )
            .append(new KeyedCodec<>("Input", VectorProviderAsset.CODEC, false), (t, k) -> t.child = k, t -> t.child)
            .add()
            .append(new KeyedCodec<>("Scale", Codec.DOUBLE, true), (t, k) -> t.scale = k, t -> t.scale)
            .add()
            .build();

    private VectorProviderAsset child = null;
    private double scale = 1.0;

    @Override
    public VectorProvider build(@NonNullDecl Argument argument) {
        if (isSkipped()) return new ConstantVectorProvider(new Vector3d());
        if (child == null) return new ConstantVectorProvider(new Vector3d());

        return new ScalerVectorProvider(child.build(argument), scale);
    }
}
