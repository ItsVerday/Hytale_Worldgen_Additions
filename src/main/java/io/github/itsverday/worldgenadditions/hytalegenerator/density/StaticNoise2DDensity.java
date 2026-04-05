package io.github.itsverday.worldgenadditions.hytalegenerator.density;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.rng.RngField;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class StaticNoise2DDensity extends Density {
    private final RngField rng;

    public StaticNoise2DDensity(int seed) {
        rng = new RngField(seed);
    }

    @Override
    public double process(@NonNullDecl Context context) {
        return rng.get(context.position.x, context.position.z);
    }
}
