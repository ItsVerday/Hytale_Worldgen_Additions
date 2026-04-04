package io.github.itsverday.worldgenadditions.hytalegenerator.density;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.math.vector.Vector3d;
import io.github.itsverday.worldgenadditions.util.ModuloXZDoubleCache;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

public class SpatialCache2DDensity extends Density {
    private Density input;
    private final ModuloXZDoubleCache<Double> cache;
    private final double yOverride;

    public SpatialCache2DDensity(@Nonnull Density input, int moduloBits, double yOverride) {
        this.input = input;
        cache = new ModuloXZDoubleCache<>(moduloBits);
        this.yOverride = yOverride;
    }

    @Override
    public double process(@NonNullDecl Context context) {
        double x = context.position.x;
        double z = context.position.z;
        if (cache.containsKey(x, z)) return cache.get(x, z);

        Context childContext = new Context(context);
        childContext.position = new Vector3d(x, yOverride, z);
        double value = input.process(childContext);
        cache.put(x, z, value);
        return value;
    }

    @Override
    public void setInputs(Density[] inputs) {
        this.input = inputs[0];
    }
}
