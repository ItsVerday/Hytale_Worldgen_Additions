package io.github.itsverday.worldgenadditions.hytalegenerator.assets.density;

import com.hypixel.hytale.builtin.hytalegenerator.assets.density.DensityAsset;
import com.hypixel.hytale.builtin.hytalegenerator.assets.vectorproviders.ConstantVectorProviderAsset;
import com.hypixel.hytale.builtin.hytalegenerator.assets.vectorproviders.VectorProviderAsset;
import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.density.nodes.ConstantValueDensity;
import com.hypixel.hytale.builtin.hytalegenerator.vectorproviders.VectorProvider;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import io.github.itsverday.worldgenadditions.hytalegenerator.density.VectorFieldWarpDensity;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class VectorFieldWarpDensityAsset extends DensityAsset {
    public static final BuilderCodec<VectorFieldWarpDensityAsset> CODEC = BuilderCodec.builder(
            VectorFieldWarpDensityAsset.class, VectorFieldWarpDensityAsset::new, DensityAsset.ABSTRACT_CODEC
    )
            .append(new KeyedCodec<>("WarpField", VectorProviderAsset.CODEC, false), (t, k) -> t.warpField = k, t -> t.warpField)
            .add()
            .append(new KeyedCodec<>("Scale", Codec.DOUBLE, true), (t, k) -> t.scale = k, t -> t.scale)
            .add()
            .build();

    private VectorProviderAsset warpField = new ConstantVectorProviderAsset();
    private double scale;

    @NonNullDecl
    @Override
    public Density build(@NonNullDecl Argument argument) {
        if (isSkipped()) return new ConstantValueDensity(0.0);

        Density child = this.buildFirstInput(argument);
        if (child == null) return new ConstantValueDensity(0.0);

        VectorProvider warpField = this.warpField.build(new VectorProviderAsset.Argument(argument));
        return new VectorFieldWarpDensity(child, warpField, scale);
    }
}
