package io.github.itsverday.worldgenadditions.hytalegenerator.vectorproviders;

import com.hypixel.hytale.builtin.hytalegenerator.vectorproviders.VectorProvider;
import com.hypixel.hytale.math.vector.Vector3d;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

public class ScalerVectorProvider extends VectorProvider {
    @Nonnull
    private final VectorProvider child;
    private final double scale;

    public ScalerVectorProvider(@Nonnull VectorProvider child, double scale) {
        this.child = child;
        this.scale = scale;
    }

    @Override
    public void process(@NonNullDecl Context context, @NonNullDecl Vector3d vector_out) {
        child.process(context, vector_out);
        vector_out.scale(scale);
    }
}
