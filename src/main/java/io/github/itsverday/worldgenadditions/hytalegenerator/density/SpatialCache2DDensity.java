package io.github.itsverday.worldgenadditions.hytalegenerator.density;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.math.vector.Vector3d;
import io.github.itsverday.worldgenadditions.util.ModuloXZDouble2DoubleCache;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

public class SpatialCache2DDensity extends Density {
    private Density input;
    private final ModuloXZDouble2DoubleCache cache;
    private final double yOverride;

    private final Context rChildContext;
    private final Vector3d rPosition;

    public SpatialCache2DDensity(@Nonnull Density input, int moduloBits, double yOverride) {
        this.input = input;
        cache = new ModuloXZDouble2DoubleCache(moduloBits);
        this.yOverride = yOverride;

        rChildContext = new Context();
        rPosition = new Vector3d();
    }

    @Override
    public double process(@NonNullDecl Context context) {
        double x = context.position.x;
        double z = context.position.z;
        if (cache.containsKey(x, z)) return cache.get(x, z);

        rPosition.assign(context.position);
        rPosition.y = yOverride;
        rChildContext.assign(context);
        rChildContext.position = rPosition;
        double value = input.process(rChildContext);
        cache.put(x, z, value);
        return value;
    }

    @Override
    public void setInputs(Density[] inputs) {
        this.input = inputs[0];
    }
}
