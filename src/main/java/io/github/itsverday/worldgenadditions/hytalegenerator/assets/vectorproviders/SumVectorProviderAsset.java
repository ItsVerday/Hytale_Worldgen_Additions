package io.github.itsverday.worldgenadditions.hytalegenerator.assets.vectorproviders;

import com.hypixel.hytale.builtin.hytalegenerator.assets.vectorproviders.VectorProviderAsset;
import com.hypixel.hytale.builtin.hytalegenerator.vectorproviders.ConstantVectorProvider;
import com.hypixel.hytale.builtin.hytalegenerator.vectorproviders.VectorProvider;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.math.vector.Vector3d;
import io.github.itsverday.worldgenadditions.hytalegenerator.vectorproviders.SumVectorProvider;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.ArrayList;

public class SumVectorProviderAsset extends VectorProviderAsset {
    public static final BuilderCodec<SumVectorProviderAsset> CODEC = BuilderCodec.builder(
            SumVectorProviderAsset.class, SumVectorProviderAsset::new, VectorProviderAsset.ABSTRACT_CODEC
    )
            .append(new KeyedCodec<>("Inputs", new ArrayCodec<>(VectorProviderAsset.CODEC, VectorProviderAsset[]::new), true), (t, k) -> t.children = k, t -> t.children)
            .add()
            .build();

    private VectorProviderAsset[] children = new VectorProviderAsset[0];

    @Override
    public VectorProvider build(@NonNullDecl Argument argument) {
        if (isSkipped()) return new ConstantVectorProvider(new Vector3d());

        ArrayList<VectorProvider> children = new ArrayList<>();
        for (VectorProviderAsset childAsset: this.children) {
            children.add(childAsset.build(argument));
        }

        return new SumVectorProvider(children.toArray(new VectorProvider[0]));
    }
}
