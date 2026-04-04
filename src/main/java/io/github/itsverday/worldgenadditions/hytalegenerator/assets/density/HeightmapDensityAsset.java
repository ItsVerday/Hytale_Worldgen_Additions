package io.github.itsverday.worldgenadditions.hytalegenerator.assets.density;

import com.hypixel.hytale.builtin.hytalegenerator.assets.density.DensityAsset;
import com.hypixel.hytale.builtin.hytalegenerator.assets.framework.DecimalConstantsFrameworkAsset;
import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.density.nodes.*;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import io.github.itsverday.worldgenadditions.hytalegenerator.density.SpatialCache2DDensity;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.List;

public class HeightmapDensityAsset extends DensityAsset {
    public static final BuilderCodec<HeightmapDensityAsset> CODEC = BuilderCodec.builder(
            HeightmapDensityAsset.class, HeightmapDensityAsset::new, DensityAsset.ABSTRACT_CODEC
    )
            .append(new KeyedCodec<>("BaseHeightName", Codec.STRING, true), (asset, baseHeightName) -> asset.baseHeightName = baseHeightName, asset -> asset.baseHeightName)
            .add()
            .append(new KeyedCodec<>("VerticalScale", Codec.DOUBLE, true), (asset, verticalScale) -> asset.verticalScale = verticalScale, asset -> asset.verticalScale)
            .add()
            .append(new KeyedCodec<>("Invert", Codec.BOOLEAN, true), (asset, invert) -> asset.invert = invert, asset -> asset.invert)
            .add()
            .append(new KeyedCodec<>("YOverride", Codec.DOUBLE, true), (asset, yOverride) -> asset.yOverride = yOverride, asset -> asset.yOverride)
            .add()
            .build();

    private String baseHeightName = "";
    private double verticalScale = 1.0;
    private double yOverride = 0.0;
    private boolean invert = false;

    @NonNullDecl
    @Override
    public Density build(@NonNullDecl Argument argument) {
        if (isSkipped()) return new ConstantValueDensity(0.0);

        Density child = this.buildFirstInput(argument);
        if (child == null) return new ConstantValueDensity(0.0);

        Double baseHeight = DecimalConstantsFrameworkAsset.Entries.get(baseHeightName, argument.referenceBundle);
        if (baseHeight == null) baseHeight = 0.0;

        Density baseHeightDensity = new InverterDensity(new BaseHeightDensity(baseHeight, true));
        if (verticalScale != 1.0) baseHeightDensity = new MultiplierDensity(List.of(baseHeightDensity, new ConstantValueDensity(verticalScale)));

        Density spatialCacheDensity = new SpatialCache2DDensity(child, 7, yOverride);
        Density sum = new SumDensity(List.of(baseHeightDensity, spatialCacheDensity));
        if (invert) sum = new InverterDensity(sum);
        return sum;
    }

    @Override
    public void cleanUp() {
        cleanUpInputs();
    }
}
