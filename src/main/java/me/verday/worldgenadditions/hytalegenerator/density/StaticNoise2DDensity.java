package me.verday.worldgenadditions.hytalegenerator.density;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.math.util.HashUtil;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class StaticNoise2DDensity extends Density {
    private long seed;

    public StaticNoise2DDensity(long seed) {
        this.seed = seed;
    }

    @Override
    public double process(@NonNullDecl Context context) {
        return HashUtil.random(seed, Double.doubleToLongBits(context.position.x), Double.doubleToLongBits(context.position.z));
    }

    @Override
    public void setInputs(Density[] inputs) {
    }
}
