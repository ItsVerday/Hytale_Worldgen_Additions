package me.verday.worldgenadditions.hytalegenerator.assets.density;

import com.hypixel.hytale.builtin.hytalegenerator.assets.density.DensityAsset;
import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.density.nodes.ConstantValueDensity;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.validation.Validators;
import me.verday.worldgenadditions.hytalegenerator.density.FWidthDensity;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class FWidthDensityAsset extends DensityAsset {
    public static final BuilderCodec<FWidthDensityAsset> CODEC = BuilderCodec.builder(
            FWidthDensityAsset.class, FWidthDensityAsset::new, DensityAsset.ABSTRACT_CODEC
    )
            .append(new KeyedCodec<>("SampleDistance", Codec.DOUBLE, true), (asset, sampleDistance) -> asset.sampleDistance = sampleDistance, asset -> asset.sampleDistance)
            .addValidator(Validators.notEqual(0.0))
            .add()
            .build();

    private double sampleDistance = 1.0;

    @NonNullDecl
    @Override
    public Density build(@NonNullDecl Argument argument) {
        if (this.isSkipped()) return new ConstantValueDensity(0.0);

        return new FWidthDensity(this.buildFirstInput(argument), sampleDistance);
    }

    @Override
    public void cleanUp() {
        this.cleanUpInputs();
    }
}
