package io.github.itsverday.worldgenadditions.hytalegenerator.assets.vectorproviders;

import com.hypixel.hytale.builtin.hytalegenerator.assets.density.DensityAsset;
import com.hypixel.hytale.builtin.hytalegenerator.assets.vectorproviders.VectorProviderAsset;
import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.density.nodes.ConstantValueDensity;
import com.hypixel.hytale.builtin.hytalegenerator.vectorproviders.ConstantVectorProvider;
import com.hypixel.hytale.builtin.hytalegenerator.vectorproviders.VectorProvider;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.math.vector.Vector3d;
import io.github.itsverday.worldgenadditions.hytalegenerator.vectorproviders.DensityScalerVectorProvider;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class DensityScalerVectorProviderAsset extends VectorProviderAsset {
    public static final BuilderCodec<DensityScalerVectorProviderAsset> CODEC = BuilderCodec.builder(
                    DensityScalerVectorProviderAsset.class, DensityScalerVectorProviderAsset::new, VectorProviderAsset.ABSTRACT_CODEC
    )
            .append(new KeyedCodec<>("Input", VectorProviderAsset.CODEC, false), (t, k) -> t.child = k, t -> t.child)
            .add()
            .append(new KeyedCodec<>("ScaleField", DensityAsset.CODEC, false), (t, k) -> t.scaleField = k, t -> t.scaleField)
            .add()
            .build();

    private VectorProviderAsset child = null;
    private DensityAsset scaleField = null;

    @Override
    public VectorProvider build(@NonNullDecl Argument argument) {
        if (isSkipped()) return new ConstantVectorProvider(new Vector3d());
        if (child == null) return new ConstantVectorProvider(new Vector3d());

        Density scaleField = this.scaleField != null ? this.scaleField.build(DensityAsset.from(argument)) : new ConstantValueDensity(1.0);
        return new DensityScalerVectorProvider(child.build(argument), scaleField);
    }
}
