package io.github.itsverday.worldgenadditions.hytalegenerator.vectorproviders;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.vectorproviders.VectorProvider;
import com.hypixel.hytale.math.vector.Vector3d;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

public class DensityScalerVectorProvider extends VectorProvider {
    @Nonnull
    private final VectorProvider child;
    @Nonnull
    private final Density scaleField;

    private final Density.Context rDensityContext;

    public DensityScalerVectorProvider(@Nonnull VectorProvider child, @Nonnull Density scaleField) {
        this.child = child;
        this.scaleField = scaleField;

        rDensityContext = new Density.Context();
    }

    @Override
    public void process(@NonNullDecl Context context, @NonNullDecl Vector3d vector_out) {
        rDensityContext.assign(context);
        child.process(context, vector_out);
        vector_out.scale(scaleField.process(rDensityContext));
    }
}
