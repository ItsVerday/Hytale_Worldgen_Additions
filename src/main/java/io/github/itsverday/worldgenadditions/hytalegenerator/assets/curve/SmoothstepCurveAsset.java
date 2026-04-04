package io.github.itsverday.worldgenadditions.hytalegenerator.assets.curve;

import com.hypixel.hytale.builtin.hytalegenerator.assets.curves.CurveAsset;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;

public class SmoothstepCurveAsset extends CurveAsset {
    public static final BuilderCodec<SmoothstepCurveAsset> CODEC = BuilderCodec.builder(
            SmoothstepCurveAsset.class, SmoothstepCurveAsset::new, CurveAsset.ABSTRACT_CODEC
    )
            .append(new KeyedCodec<>("EdgeMin", Codec.DOUBLE, true), (asset, edgeMin) -> asset.edgeMin = edgeMin, asset -> asset.edgeMin)
            .add()
            .append(new KeyedCodec<>("EdgeMax", Codec.DOUBLE, true), (asset, edgeMax) -> asset.edgeMax = edgeMax, asset -> asset.edgeMax)
            .add()
            .append(new KeyedCodec<>("Low", Codec.DOUBLE, true), (asset, low) -> asset.low = low, asset -> asset.low)
            .add()
            .append(new KeyedCodec<>("High", Codec.DOUBLE, true), (asset, high) -> asset.high = high, asset -> asset.high)
            .add()
            .build();

    private double edgeMin = 0.0;
    private double edgeMax = 1.0;
    private double low = 0.0;
    private double high = 1.0;

    @Override
    public Double2DoubleFunction build() {
        if (low == high) return _ -> low;
        if (edgeMin == edgeMax) return in -> {
            if (in < edgeMin) return low;
            return high;
        };

        double normalizer = 1.0 / (edgeMax - edgeMin);
        double delta = high - low;
        return in -> {
            double x = (in - edgeMin) * normalizer;
            if (x < 0) return low;
            if (x > 1) return high;
            x = x * x * (3.0 - 2.0 * x);
            return low + delta * x;
        };
    }

    @Override
    public void cleanUp() {
    }
}
