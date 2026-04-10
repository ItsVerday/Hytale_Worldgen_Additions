package io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms;

import com.hypixel.hytale.builtin.hytalegenerator.assets.density.ConstantDensityAsset;
import com.hypixel.hytale.builtin.hytalegenerator.assets.density.DensityAsset;
import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.density.nodes.ConstantValueDensity;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.math.vector.Vector2d;
import io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.VectorWarpPipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class VectorWarpPipelineCartaTransformAsset extends PipelineCartaTransformAsset {
    public static final BuilderCodec<VectorWarpPipelineCartaTransformAsset> CODEC = BuilderCodec.builder(VectorWarpPipelineCartaTransformAsset.class, VectorWarpPipelineCartaTransformAsset::new, PipelineCartaTransformAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("WarpField", DensityAsset.CODEC, false), (t, k) -> t.warpField = k, t -> t.warpField)
            .add()
            .append(new KeyedCodec<>("WarpFactor", Codec.DOUBLE, true), (t, k) -> t.warpFactor = k, t -> t.warpFactor)
            .add()
            .append(new KeyedCodec<>("WarpVector", Vector2d.CODEC, false), (t, k) -> t.warpVector = k, t -> t.warpVector)
            .add()
            .build();

    private DensityAsset warpField = new ConstantDensityAsset();
    private double warpFactor = 1.0;
    private Vector2d warpVector = new Vector2d();

    @NonNullDecl
    @Override
    public PipelineCartaTransform build(@NonNullDecl Argument arg, PipelineCartaTransform previous) {
        if (isSkipped()) return previous;

        PipelineCartaTransform child = previous;
        if (inputs().length > 0) child = inputs()[0].build(arg, previous);
        if (warpFactor == 0) return child;

        Density warpFieldDensity = warpField != null ? warpField.build(new DensityAsset.Argument(arg.parentSeed, arg.referenceBundle, arg.workerId)) : new ConstantValueDensity(0.0);
        return new VectorWarpPipelineCartaTransform(child, warpFieldDensity, warpFactor, warpVector);
    }

    @Override
    public void cleanUp() {
        super.cleanUp();
        warpField.cleanUp();
    }
}
