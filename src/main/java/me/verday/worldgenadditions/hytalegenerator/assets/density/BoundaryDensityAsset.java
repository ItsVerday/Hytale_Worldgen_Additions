package me.verday.worldgenadditions.hytalegenerator.assets.density;

import com.hypixel.hytale.builtin.hytalegenerator.assets.density.DensityAsset;
import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.density.nodes.ConstantValueDensity;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.validation.Validators;
import me.verday.worldgenadditions.hytalegenerator.density.BoundaryDensity;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class BoundaryDensityAsset extends DensityAsset {
    public static final BuilderCodec<BoundaryDensityAsset> CODEC = BuilderCodec.builder(
            BoundaryDensityAsset.class, BoundaryDensityAsset::new, DensityAsset.ABSTRACT_CODEC
    )
            .append(new KeyedCodec<>("Cutoff", Codec.DOUBLE, true), (asset, cutoff) -> asset.cutoff = cutoff, asset -> asset.cutoff)
            .add()
            .append(new KeyedCodec<>("Width", Codec.DOUBLE, true), (asset, width) -> asset.width = width, asset -> asset.width)
            .addValidator(Validators.greaterThanOrEqual(0.0))
            .add()
            .append(new KeyedCodec<>("Bias", Codec.DOUBLE, true), (asset, bias) -> asset.bias = bias, asset -> asset.bias)
            .addValidator(Validators.insideRange(0.0, 1.000000001))
            .add()
            .build();

    private double cutoff = 0.0;
    private double width = 1.0;
    private double bias = 0.5;

    @NonNullDecl
    @Override
    public Density build(@NonNullDecl Argument argument) {
        if (this.isSkipped()) return new ConstantValueDensity(0.0);

        return new BoundaryDensity(this.buildFirstInput(argument), cutoff, width, bias);
    }

    @Override
    public void cleanUp() {
        this.cleanUpInputs();
    }
}
