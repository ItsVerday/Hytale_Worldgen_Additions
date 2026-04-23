package io.github.itsverday.worldgenadditions.hytalegenerator.vectorproviders;

import com.hypixel.hytale.builtin.hytalegenerator.vectorproviders.VectorProvider;
import com.hypixel.hytale.math.vector.Vector3d;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

public class ClampMagnitudeVectorProvider extends VectorProvider {
    @Nonnull
    private final VectorProvider child;
    private final double magnitude;

    public ClampMagnitudeVectorProvider(@Nonnull VectorProvider child, double magnitude) {
        this.child = child;
        this.magnitude = magnitude;
    }

    @Override
    public void process(@NonNullDecl VectorProvider.Context context, @NonNullDecl Vector3d vector_out) {
        child.process(context, vector_out);
        if (vector_out.squaredLength() > magnitude * magnitude) vector_out.setLength(magnitude);
    }
}
