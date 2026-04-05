package io.github.itsverday.worldgenadditions.hytalegenerator.assets.density;

import com.hypixel.hytale.builtin.hytalegenerator.assets.density.DensityAsset;
import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.density.nodes.ConstantValueDensity;
import com.hypixel.hytale.builtin.hytalegenerator.rng.SeedBox;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import io.github.itsverday.worldgenadditions.hytalegenerator.density.StaticNoise2DDensity;

import javax.annotation.Nonnull;

public class StaticNoise2DDensityAsset extends DensityAsset {
    public static final BuilderCodec<StaticNoise2DDensityAsset> CODEC = BuilderCodec.builder(
            StaticNoise2DDensityAsset.class, StaticNoise2DDensityAsset::new, DensityAsset.ABSTRACT_CODEC
    )
            .append(new KeyedCodec<>("Seed", Codec.STRING, true), (asset, seed) -> asset.seedKey = seed, asset -> asset.seedKey)
            .add()
            .append(new KeyedCodec<>("Rounding", Codec.DOUBLE, true), (asset, rounding) -> asset.rounding = rounding, asset -> asset.rounding)
            .add()
            .build();

    private String seedKey = "A";
    private double rounding = 0.0;

    @Nonnull
    @Override
    public Density build(@Nonnull Argument argument) {
        if (this.isSkipped()) return new ConstantValueDensity(0.0);

        SeedBox childSeed = argument.parentSeed.child(this.seedKey);
        return new StaticNoise2DDensity(childSeed.createSupplier().get(), rounding);
    }
}
