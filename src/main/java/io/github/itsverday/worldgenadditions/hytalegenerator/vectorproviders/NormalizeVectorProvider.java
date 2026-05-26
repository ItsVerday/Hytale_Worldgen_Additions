package io.github.itsverday.worldgenadditions.hytalegenerator.vectorproviders;

import com.hypixel.hytale.builtin.hytalegenerator.vectorproviders.VectorProvider;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.joml.Vector3d;

import javax.annotation.Nonnull;

public class NormalizeVectorProvider extends VectorProvider {
    @Nonnull
    private final VectorProvider child;
    private final double magnitude;

    public NormalizeVectorProvider(@Nonnull VectorProvider child, double magnitude) {
        this.child = child;
        this.magnitude = magnitude;
    }

    @Override
    public void process(@NonNullDecl Context context, @NonNullDecl Vector3d vector_out) {
        child.process(context, vector_out);
        if (vector_out.lengthSquared() > 0.0) vector_out.normalize(magnitude);
    }
}
