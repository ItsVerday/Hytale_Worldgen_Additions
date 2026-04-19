package io.github.itsverday.worldgenadditions.hytalegenerator.assets.density;

import com.hypixel.hytale.builtin.hytalegenerator.assets.density.DensityAsset;
import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.density.nodes.ConstantValueDensity;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.math.vector.Vector3d;
import io.github.itsverday.worldgenadditions.hytalegenerator.density.BasisChangeDensity;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

//
public class BasisChangeDensityAsset extends DensityAsset {
    public static final BuilderCodec<BasisChangeDensityAsset> CODEC = BuilderCodec.builder(
            BasisChangeDensityAsset.class, BasisChangeDensityAsset::new, DensityAsset.ABSTRACT_CODEC
    )
            .append(new KeyedCodec<>("Origin", Vector3d.CODEC, false), (t, k) -> t.origin = k, t -> t.origin)
            .add()
            .append(new KeyedCodec<>("XAxis", Vector3d.CODEC, false), (t, k) -> t.xAxis = k, t -> t.xAxis)
            .add()
            .append(new KeyedCodec<>("YAxis", Vector3d.CODEC, false), (t, k) -> t.yAxis = k, t -> t.yAxis)
            .add()
            .append(new KeyedCodec<>("ZAxis", Vector3d.CODEC, false), (t, k) -> t.zAxis = k, t -> t.zAxis)
            .add()
            .append(new KeyedCodec<>("Normalized", Codec.BOOLEAN, true), (t, k) -> t.normalized = k, t -> t.normalized)
            .add()
            .build();

    private Vector3d origin = new Vector3d(0, 0, 0);
    private Vector3d xAxis = new Vector3d(1, 0, 0);
    private Vector3d yAxis = new Vector3d(0, 1, 0);
    private Vector3d zAxis = new Vector3d(0, 0, 1);
    private boolean normalized = false;

    @NonNullDecl
    @Override
    public Density build(@NonNullDecl Argument argument) {
        if (this.isSkipped()) return new ConstantValueDensity(0.0);

        Density child = this.buildFirstInput(argument);
        if (child == null) return new ConstantValueDensity(0.0);

        if (normalized) {
            xAxis.normalize();
            yAxis.normalize();
            zAxis.normalize();
        }

        return new BasisChangeDensity(child, origin, xAxis, yAxis, zAxis);
    }

    @Override
    public void cleanUp() {
        cleanUpInputs();
    }
}
