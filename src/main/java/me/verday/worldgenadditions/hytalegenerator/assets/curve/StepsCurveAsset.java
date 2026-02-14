package me.verday.worldgenadditions.hytalegenerator.assets.curve;

import com.hypixel.hytale.builtin.hytalegenerator.assets.curves.CurveAsset;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.validation.Validators;
import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;

public class StepsCurveAsset extends CurveAsset {
    public static final BuilderCodec<StepsCurveAsset> CODEC = BuilderCodec.builder(
            StepsCurveAsset.class, StepsCurveAsset::new, CurveAsset.ABSTRACT_CODEC
    )
            .append(new KeyedCodec<>("FromMin", Codec.DOUBLE, true), (a, v) -> a.fromMin = v, a -> a.fromMin)
            .add()
            .append(new KeyedCodec<>("FromMax", Codec.DOUBLE, true), (a, v) -> a.fromMax = v, a -> a.fromMax)
            .add()
            .append(new KeyedCodec<>("ToMin", Codec.DOUBLE, true), (a, v) -> a.toMin = v, a -> a.toMin)
            .add()
            .append(new KeyedCodec<>("ToMax", Codec.DOUBLE, true), (a, v) -> a.toMax = v, a -> a.toMax)
            .add()
            .append(new KeyedCodec<>("StepCount", Codec.INTEGER, true), (a, v) -> a.stepCount = v, a -> a.stepCount)
            .addValidator(Validators.greaterThan(0))
            .add()
            .append(new KeyedCodec<>("WallWidth", Codec.DOUBLE, true), (a, v) -> a.wallWidth = v, a -> a.wallWidth)
            .addValidator(Validators.min(0.0))
            .addValidator(Validators.max(1.0))
            .add()
            .append(new KeyedCodec<>("StepSlope", Codec.DOUBLE, true), (a, v) -> a.stepSlope = v, a -> a.stepSlope)
            .addValidator(Validators.min(0.0))
            .addValidator(Validators.max(1.0))
            .add()
            .append(new KeyedCodec<>("CurveExponent", Codec.DOUBLE, true), (a, v) -> a.curveExponent = v, a -> a.curveExponent)
            .addValidator(Validators.greaterThan(0.0))
            .add()
            .build();

    private double fromMin = 0.0;
    private double fromMax = 1.0;
    private double toMin = 0.0;
    private double toMax = 1.0;

    private double curveExponent = 1.0;

    private int stepCount = 1;
    private double wallWidth = 1.0;
    private double stepSlope = 1.0;

    @Override
    public Double2DoubleFunction build() {
        return in -> {
            double normalized = normalize(fromMin, fromMax, 0, 1, in);

            if (normalized >= 0 && normalized <= 1) {
                normalized = Math.pow(normalized, curveExponent);
                normalized *= stepCount + (1.0 - wallWidth);

                double normalizedFloor = Math.floor(normalized);
                double normalizedFract = normalized - normalizedFloor;
                if (normalizedFract > 1.0 - wallWidth) {
                    normalizedFract = normalize(1.0 - wallWidth, 1.0, stepSlope, 1.0, normalizedFract);
                } else {
                    normalizedFract = normalize(0, 1.0 - wallWidth, 0.0, stepSlope, normalizedFract);
                }

                normalized = normalizedFloor + normalizedFract;
                normalized /= stepCount + stepSlope;
            }

            return normalize(0, 1, toMin, toMax, normalized);
        };
    }

    @Override
    public void cleanUp() {
    }

    private static double normalize(double fromMin, double fromMax, double toMin, double toMax, double input) {
        if (toMin == toMax) return toMin;
        if (fromMin == fromMax) throw new IllegalArgumentException("fromMin == fromMax");
        input -= fromMin;
        input /= fromMax - fromMin;
        input *= toMax - toMin;
        input += toMin;
        return input;
    }
}
