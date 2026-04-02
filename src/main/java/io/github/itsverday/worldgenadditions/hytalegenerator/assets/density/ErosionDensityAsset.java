package io.github.itsverday.worldgenadditions.hytalegenerator.assets.density;

import com.hypixel.hytale.builtin.hytalegenerator.assets.density.DensityAsset;
import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.density.nodes.ConstantValueDensity;
import com.hypixel.hytale.builtin.hytalegenerator.rng.SeedBox;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import io.github.itsverday.worldgenadditions.hytalegenerator.density.ErosionDensity;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class ErosionDensityAsset extends DensityAsset {
    public static final BuilderCodec<ErosionDensityAsset> CODEC = BuilderCodec.builder(
                    ErosionDensityAsset.class, ErosionDensityAsset::new, DensityAsset.ABSTRACT_CODEC
            )
            .append(new KeyedCodec<>("Seed", Codec.STRING, true), (asset, seed) -> asset.seedKey = seed, asset -> asset.seedKey)
            .add()
            .append(new KeyedCodec<>("SampleDistance", Codec.DOUBLE, true), (asset, inputSampleDistance) -> asset.inputSampleDistance = inputSampleDistance, asset -> asset.inputSampleDistance)
            .add()
            .append(new KeyedCodec<>("Octaves", Codec.INTEGER, true), (asset, octaves) -> asset.octaves = octaves, asset -> asset.octaves)
            .add()
            .append(new KeyedCodec<>("Lacunarity", Codec.DOUBLE, true), (asset, lacunarity) -> asset.lacunarity = lacunarity, asset -> asset.lacunarity)
            .add()
            .append(new KeyedCodec<>("Persistence", Codec.DOUBLE, true), (asset, persistence) -> asset.persistence = persistence, asset -> asset.persistence)
            .add()
            .append(new KeyedCodec<>("Scale", Codec.DOUBLE, true), (asset, scale) -> asset.scale = scale, asset -> asset.scale)
            .add()
            .append(new KeyedCodec<>("Strength", Codec.DOUBLE, true), (asset, strength) -> asset.strength = strength, asset -> asset.strength)
            .add()
            .append(new KeyedCodec<>("GullyWeight", Codec.DOUBLE, true), (asset, gullyWeight) -> asset.gullyWeight = gullyWeight, asset -> asset.gullyWeight)
            .add()
            .append(new KeyedCodec<>("Detail", Codec.DOUBLE, true), (asset, detail) -> asset.detail = detail, asset -> asset.detail)
            .add()
            .append(new KeyedCodec<>("RidgeRounding", Codec.DOUBLE, true), (asset, ridgeRounding) -> asset.ridgeRounding = ridgeRounding, asset -> asset.ridgeRounding)
            .add()
            .append(new KeyedCodec<>("CreaseRounding", Codec.DOUBLE, true), (asset, creaseRounding) -> asset.creaseRounding = creaseRounding, asset -> asset.creaseRounding)
            .add()
            .append(new KeyedCodec<>("RoundingMultiplier", Codec.DOUBLE, true), (asset, roundingMultiplier) -> asset.roundingMultiplier = roundingMultiplier, asset -> asset.roundingMultiplier)
            .add()
            .append(new KeyedCodec<>("InitialOnset", Codec.DOUBLE, true), (asset, initialOnset) -> asset.initialOnset = initialOnset, asset -> asset.initialOnset)
            .add()
            .append(new KeyedCodec<>("GullyOnset", Codec.DOUBLE, true), (asset, gullyOnset) -> asset.gullyOnset = gullyOnset, asset -> asset.gullyOnset)
            .add()
            .append(new KeyedCodec<>("CellScale", Codec.DOUBLE, true), (asset, cellScale) -> asset.cellScale = cellScale, asset -> asset.cellScale)
            .add()
            .append(new KeyedCodec<>("Normalization", Codec.DOUBLE, true), (asset, normalization) -> asset.normalization = normalization, asset -> asset.normalization)
            .add()
            .build();

    private String seedKey = "A";
    private double inputSampleDistance = 1.0;

    private int octaves = 5;
    private double lacunarity = 2.0;
    private double persistence = 0.5;

    private double scale = 50;
    private double strength = 0.2;
    private double gullyWeight = 0.5;
    private double detail = 1.5;
    private double ridgeRounding = 0.1;
    private double creaseRounding = 0.0;
    private double roundingMultiplier = 0.1;
    private double initialOnset = 1.25;
    private double gullyOnset = 1.25;
    private double assumedSlope = 0.7;
    private double assumedSlopeBlending = 1.0;
    private double cellScale = 0.7;
    private double normalization = 0.5;

    @NonNullDecl
    @Override
    public Density build(@NonNullDecl Argument argument) {
        if (this.isSkipped()) return new ConstantValueDensity(0.0);

        Density child = this.buildFirstInput(argument);
        if (child == null) return new ConstantValueDensity(0.0);

        SeedBox childSeed = argument.parentSeed.child(this.seedKey);
        return new ErosionDensity(child, childSeed.createSupplier().get(), inputSampleDistance, octaves, lacunarity, persistence, scale, strength, gullyWeight, detail, ridgeRounding, creaseRounding, roundingMultiplier, initialOnset, gullyOnset, assumedSlope, assumedSlopeBlending, cellScale, normalization);
    }

    @Override
    public void cleanUp() {
        this.cleanUpInputs();
    }
}
