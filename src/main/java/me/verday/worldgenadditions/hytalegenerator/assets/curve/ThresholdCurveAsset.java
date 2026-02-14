package me.verday.worldgenadditions.hytalegenerator.assets.curve;

import com.hypixel.hytale.builtin.hytalegenerator.assets.curves.CurveAsset;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;

public class ThresholdCurveAsset extends CurveAsset {
    public static final BuilderCodec<ThresholdCurveAsset> CODEC = BuilderCodec.builder(
            ThresholdCurveAsset.class, ThresholdCurveAsset::new, CurveAsset.ABSTRACT_CODEC
    )
            .append(new KeyedCodec<>("Low", Codec.DOUBLE, true), (asset, low) -> asset.low = low, asset -> asset.low)
            .add()
            .append(new KeyedCodec<>("High", Codec.DOUBLE, true), (asset, high) -> asset.high = high, asset -> asset.high)
            .add()
            .append(new KeyedCodec<>("Curoff", Codec.DOUBLE, true), (asset, cutoff) -> asset.cutoff = cutoff, asset -> asset.cutoff)
            .add()
            .build();

    private double low = 0.0;
    private double high = 1.0;
    private double cutoff = 0.0;

    @Override
    public Double2DoubleFunction build() {
        return in -> {
            if (in >= cutoff) return high;
            return low;
        };
    }

    @Override
    public void cleanUp() {
    }
}
