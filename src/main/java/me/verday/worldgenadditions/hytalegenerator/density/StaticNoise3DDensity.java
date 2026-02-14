package me.verday.worldgenadditions.hytalegenerator.density;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.math.util.HashUtil;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class StaticNoise3DDensity extends Density {
    private final long seed;

    public StaticNoise3DDensity(long seed) {
        this.seed = seed;
    }

    @Override
    public double process(@NonNullDecl Context context) {
        return HashUtil.random(seed, Double.doubleToLongBits(context.position.x), Double.doubleToLongBits(context.position.y), Double.doubleToLongBits(context.position.z));
    }

    @Override
    public void setInputs(Density[] inputs) {
    }
}
