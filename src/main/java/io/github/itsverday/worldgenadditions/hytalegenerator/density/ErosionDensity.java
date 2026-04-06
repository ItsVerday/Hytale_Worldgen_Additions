package io.github.itsverday.worldgenadditions.hytalegenerator.density;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.rng.RngField;
import com.hypixel.hytale.math.util.HashUtil;
import com.hypixel.hytale.math.vector.Vector2d;
import com.hypixel.hytale.math.vector.Vector3d;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

public class ErosionDensity extends Density {
    // The technique used in this Node is based on work by runevision and others: https://youtu.be/r4V21_uUK8Y and https://blog.runevision.com/2026/03/fast-and-gorgeous-erosion-filter.html
    @Nonnull
    private Density input;
    private final RngField rng;
    private final double inputSampleDistance;

    private final int octaves;
    private final double lacunarity;
    private final double persistence;

    private final double scale;
    private final double strength;
    private final double gullyWeight;
    private final double detail;
    private final double ridgeRounding;
    private final double creaseRounding;
    private final double roundingMultiplier;
    private final double initialOnset;
    private final double gullyOnset;
    private final double assumedSlope;
    private final double assumedSlopeBlending;
    private final double cellScale;
    private final double normalization;

    private final Context rChildContext;
    private final Vector3d rPosition;

    private double phacellePhaseX;
    private double phacellePhaseY;
    private double phacelleSideX;
    private double phacelleSideY;

    public ErosionDensity(@Nonnull Density input, int seed, double inputSampleDistance, int octaves, double lacunarity, double persistence, double scale, double strength, double gullyWeight, double detail, double ridgeRounding, double creaseRounding, double roundingMultiplier, double initialOnset, double gullyOnset, double assumedSlope, double assumedSlopeBlending, double cellScale, double normalization) {
        assert inputSampleDistance > 0.0;

        this.input = input;
        rng = new RngField(seed);
        this.inputSampleDistance = inputSampleDistance;

        this.octaves = octaves;
        this.lacunarity = lacunarity;
        this.persistence = persistence;

        this.scale = scale;
        this.strength = strength;
        this.gullyWeight = gullyWeight;
        this.detail = detail;
        this.ridgeRounding = ridgeRounding;
        this.creaseRounding = creaseRounding;
        this.roundingMultiplier = roundingMultiplier;
        this.initialOnset = initialOnset;
        this.gullyOnset = gullyOnset;
        this.assumedSlope = assumedSlope;
        this.assumedSlopeBlending = assumedSlopeBlending;
        this.cellScale = cellScale;
        this.normalization = normalization;

        rChildContext = new Context();
        rPosition = new Vector3d();
    }

    @Override
    public double process(@NonNullDecl Context context) {
        Vector2d position2d = new Vector2d(context.position.x, context.position.z);

        rPosition.assign(context.position);
        rChildContext.assign(context);
        rChildContext.position = rPosition;

        double valueAtOrigin = input.process(rChildContext);
        double newX = context.position.x + inputSampleDistance;
        double newZ = context.position.z + inputSampleDistance;

        rChildContext.position.assign(newX, context.position.y, context.position.z);
        double deltaX = input.process(rChildContext) - valueAtOrigin;
        double dx = deltaX / inputSampleDistance;
        dx *= scale;

        rChildContext.position.assign(context.position.x, context.position.y, newZ);
        double deltaZ = input.process(rChildContext) - valueAtOrigin;
        double dz = deltaZ / inputSampleDistance;
        dz *= scale;

        double height = valueAtOrigin;
        double initialHeight = height;
        double strength = this.strength;
        double fadeTarget = height;

        if (fadeTarget > 1.0) fadeTarget = 1.0;
        if (fadeTarget < -1.0) fadeTarget = -1.0;

        double frequency = 1.0 / (scale * cellScale);
        double slopeLength = Math.max(Math.hypot(dx, dz), 1e-10);
        double magnitude = 0.0;
        double roundingMult = 1.0;

        double roundingForInput = mix(creaseRounding, ridgeRounding, clamp01(fadeTarget + 0.5)) * roundingMultiplier;
        double combinedMask = easeOut(smoothStart(slopeLength * initialOnset, roundingForInput * initialOnset));

        double gullyDx = mix(dx, dx / slopeLength * assumedSlope, assumedSlopeBlending);
        double gullyDz = mix(dz, dz / slopeLength * assumedSlope, assumedSlopeBlending);

        for (int iter = 0; iter < octaves; iter++) {
            double safeGullyDx = gullyDx;
            double safeGullyDz = gullyDz;
            double gullyDLength = Math.hypot(gullyDx, gullyDz);
            if (gullyDLength > 1e-10) {
                safeGullyDx /= gullyDLength;
                safeGullyDz /= gullyDLength;
            }

            phacelleNoise(position2d.x * frequency, position2d.y * frequency, safeGullyDx, safeGullyDz, cellScale, 0.25, normalization);
            phacelleSideX *= -frequency;
            phacelleSideY *= -frequency;
            double sloping = Math.abs(phacellePhaseY);
            double sign = Math.signum(phacellePhaseY);
            gullyDx += sign * phacelleSideX * strength * gullyWeight;
            gullyDz += sign * phacelleSideY * strength * gullyWeight;

            double gullies = mix(fadeTarget, phacellePhaseX * gullyWeight, combinedMask);
            height += gullies * strength;
            magnitude += strength;
            fadeTarget = gullies;

            double roundingForOctave = mix(creaseRounding, ridgeRounding, clamp01(phacellePhaseX + 0.5)) * roundingMult;
            double newMask = easeOut(smoothStart(sloping * gullyOnset, roundingForOctave * gullyOnset));
            combinedMask = powInverse(combinedMask, detail) * newMask;

            strength *= persistence;
            frequency *= lacunarity;
            roundingMult *= lacunarity;
        }

        double heightDelta = height - initialHeight;
        return height + heightDelta * magnitude;
    }

    private void phacelleNoise(double x, double y, double normX, double normY, double frequency, double offset, double normalization) {
        phacelleSideX = -normY * frequency * Math.TAU;
        phacelleSideY = normX * frequency * Math.TAU;
        offset *= Math.TAU;

        double xFloor = Math.floor(x);
        double xFract = x - xFloor;
        double yFloor = Math.floor(y);
        double yFract = y - yFloor;
        double phaseX = 0;
        double phaseY = 0;

        double weightSum = 0.0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                double xFromCellPoint = xFract - i - rng.get(xFloor + i, yFloor + j);
                double yFromCellPoint = yFract - j - rng.get(xFloor + i + 0.5, yFloor + j);

                double distanceSquared = xFromCellPoint * xFromCellPoint + yFromCellPoint * yFromCellPoint;
                double weight = Math.max(Math.exp(-distanceSquared * 2.0) - 0.01111, 0.0);
                weightSum += weight;

                double waveInput = xFromCellPoint * phacelleSideX + yFromCellPoint * phacelleSideY + offset;
                phaseX += Math.cos(waveInput) * weight;
                phaseY += Math.sin(waveInput) * weight;
            }
        }

        double interpolatedX = phaseX / weightSum;
        double interpolatedY = phaseY / weightSum;
        double magnitude = Math.hypot(interpolatedX, interpolatedY);
        magnitude = Math.max(1.0 - normalization, magnitude);
        phacellePhaseX = interpolatedX / magnitude;
        phacellePhaseY = interpolatedY / magnitude;
    }

    private double mix(double a, double b, double weight) {
        return a * (1 - weight) + b * weight;
    }

    private double clamp01(double value) {
        if (value > 1.0) return 1.0;
        if (value < 0.0) return 0.0;
        return value;
    }

    private double easeOut(double t) {
        double v = 1.0 - clamp01(t);
        return 1.0 - v * v;
    }

    private double smoothStart(double t, double smoothing) {
        if (t >= smoothing) return t - 0.5 * smoothing;
        return 0.5 * t * t / smoothing;
    }

    private double powInverse(double t, double power) {
        return 1.0 - Math.pow(1.0 - clamp01(t), power);
    }

    @Override
    public void setInputs(Density[] inputs) {
        this.input = inputs[0];
    }
}
