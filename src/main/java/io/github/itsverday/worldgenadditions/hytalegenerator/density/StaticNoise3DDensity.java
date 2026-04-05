package io.github.itsverday.worldgenadditions.hytalegenerator.density;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.rng.RngField;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class StaticNoise3DDensity extends Density {
    private final RngField rng;
    private final double rounding;

    public StaticNoise3DDensity(int seed, double rounding) {
        rng = new RngField(seed);
        this.rounding = Math.abs(rounding);
    }

    private double round(double x) {
        if (rounding == 0) return x;
        return Math.floor(x / rounding) * rounding;
    }

    @Override
    public double process(@NonNullDecl Context context) {
        return rng.get(round(context.position.x), round(context.position.y), round(context.position.z));
    }
}
