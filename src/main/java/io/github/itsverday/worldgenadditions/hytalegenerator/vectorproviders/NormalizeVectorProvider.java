package io.github.itsverday.worldgenadditions.hytalegenerator.vectorproviders;

import com.hypixel.hytale.builtin.hytalegenerator.vectorproviders.VectorProvider;
import com.hypixel.hytale.math.vector.Vector3d;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

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
        if (vector_out.squaredLength() > 0.0) vector_out.setLength(magnitude);
    }
}
