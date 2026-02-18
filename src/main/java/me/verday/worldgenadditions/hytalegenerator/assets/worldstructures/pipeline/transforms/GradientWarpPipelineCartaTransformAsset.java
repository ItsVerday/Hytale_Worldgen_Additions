package me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms;

import com.hypixel.hytale.builtin.hytalegenerator.assets.density.ConstantDensityAsset;
import com.hypixel.hytale.builtin.hytalegenerator.assets.density.DensityAsset;
import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.density.nodes.ConstantValueDensity;
import com.hypixel.hytale.builtin.hytalegenerator.density.nodes.MultiCacheDensity;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.validation.Validators;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.NonePipelineCartaTransform;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.GradientWarpPipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class GradientWarpPipelineCartaTransformAsset extends PipelineCartaTransformAsset {
    public static final BuilderCodec<GradientWarpPipelineCartaTransformAsset> CODEC = BuilderCodec.builder(GradientWarpPipelineCartaTransformAsset.class, GradientWarpPipelineCartaTransformAsset::new, PipelineCartaTransformAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("WarpField", DensityAsset.CODEC, false), (t, k) -> t.warpField = k, t -> t.warpField)
            .add()
            .append(new KeyedCodec<>("SampleDistance", Codec.DOUBLE, false), (t, k) -> t.sampleDistance = k, t -> t.sampleDistance)
            .addValidator(Validators.greaterThan(0.0))
            .add()
            .append(new KeyedCodec<>("WarpFactor", Codec.DOUBLE, false), (t, k) -> t.warpFactor = k, t -> t.warpFactor)
            .add()
            .build();

    private DensityAsset warpField = new ConstantDensityAsset();
    private double sampleDistance = 1;
    private double warpFactor = 1;

    @NonNullDecl
    @Override
    public PipelineCartaTransform<Integer> build(@NonNullDecl Argument arg) {
        if (isSkipped()) return new NonePipelineCartaTransform<>();

        Density warpFieldDensity = warpField != null ? new MultiCacheDensity(warpField.build(new DensityAsset.Argument(arg.parentSeed, arg.referenceBundle, arg.workerId)), 256) : new ConstantValueDensity(0.0);
        PipelineCartaTransform<Integer> child = null;
        if (inputs().length > 0) {
            child = inputs()[0].build(arg);
        }

        return new GradientWarpPipelineCartaTransform<>(child, warpFieldDensity, sampleDistance, warpFactor);
    }

    @Override
    public void cleanUp() {
        super.cleanUp();
        if (warpField != null) warpField.cleanUp();
    }
}
