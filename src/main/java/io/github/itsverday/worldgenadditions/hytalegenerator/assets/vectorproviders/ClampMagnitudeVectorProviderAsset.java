package io.github.itsverday.worldgenadditions.hytalegenerator.assets.vectorproviders;

import com.hypixel.hytale.builtin.hytalegenerator.assets.vectorproviders.VectorProviderAsset;
import com.hypixel.hytale.builtin.hytalegenerator.vectorproviders.ConstantVectorProvider;
import com.hypixel.hytale.builtin.hytalegenerator.vectorproviders.VectorProvider;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.math.vector.Vector3d;
import io.github.itsverday.worldgenadditions.hytalegenerator.vectorproviders.ClampMagnitudeVectorProvider;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class ClampMagnitudeVectorProviderAsset extends VectorProviderAsset {
    public static final BuilderCodec<ClampMagnitudeVectorProviderAsset> CODEC = BuilderCodec.builder(
            ClampMagnitudeVectorProviderAsset.class, ClampMagnitudeVectorProviderAsset::new, VectorProviderAsset.ABSTRACT_CODEC
    )
    .append(new KeyedCodec<>("Input", VectorProviderAsset.CODEC, false), (t, k) -> t.child = k, t -> t.child)
    .add()
    .append(new KeyedCodec<>("Magnitude", Codec.DOUBLE, true), (t, k) -> t.magnitude = k, t -> t.magnitude)
    .add()
    .build();

    private VectorProviderAsset child = null;
    private double magnitude = 1.0;

    @Override
    public VectorProvider build(@NonNullDecl Argument argument) {
        if (isSkipped()) return new ConstantVectorProvider(new Vector3d());
        if (child == null) return new ConstantVectorProvider(new Vector3d());

        return new ClampMagnitudeVectorProvider(child.build(argument), magnitude);
    }
}
