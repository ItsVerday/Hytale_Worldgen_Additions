package me.verday.worldgenadditions.hytalegenerator.assets.density;

import com.hypixel.hytale.builtin.hytalegenerator.assets.density.DensityAsset;
import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.density.nodes.ConstantValueDensity;
import com.hypixel.hytale.builtin.hytalegenerator.seed.SeedBox;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import me.verday.worldgenadditions.hytalegenerator.density.StaticNoise3DDensity;

import javax.annotation.Nonnull;

public class StaticNoise3DDensityAsset extends DensityAsset {
    public static final BuilderCodec<StaticNoise3DDensityAsset> CODEC = BuilderCodec.builder(
            StaticNoise3DDensityAsset.class, StaticNoise3DDensityAsset::new, DensityAsset.ABSTRACT_CODEC
    )
            .append(new KeyedCodec<>("Seed", Codec.STRING, true), (asset, seed) -> asset.seedKey = seed, asset -> asset.seedKey)
            .add()
            .build();

    private String seedKey = "A";

    @Nonnull
    @Override
    public Density build(@Nonnull Argument argument) {
        if (this.isSkipped()) return new ConstantValueDensity(0.0);

        SeedBox childSeed = argument.parentSeed.child(this.seedKey);
        return new StaticNoise3DDensity(childSeed.createSupplier().get());
    }
}
