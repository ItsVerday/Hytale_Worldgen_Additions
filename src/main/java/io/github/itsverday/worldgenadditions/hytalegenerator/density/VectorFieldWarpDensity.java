package io.github.itsverday.worldgenadditions.hytalegenerator.density;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.vectorproviders.VectorProvider;
import com.hypixel.hytale.math.vector.Vector3d;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

public class VectorFieldWarpDensity extends Density {
    private Density input;
    private final VectorProvider warpField;
    private final double scale;

    private final Context rChildContext;
    private final VectorProvider.Context rChildVectorContext;
    private final Vector3d rPosition;

    public VectorFieldWarpDensity(@Nonnull Density input, @Nonnull VectorProvider warpField, double scale) {
        this.input = input;
        this.warpField = warpField;
        this.scale = scale;

        rChildContext = new Context();
        rChildVectorContext = new VectorProvider.Context(rChildContext);
        rPosition = new Vector3d();
    }

    @Override
    public double process(@NonNullDecl Context context) {
        rChildVectorContext.assign(context);
        warpField.process(rChildVectorContext, rPosition);
        rPosition.scale(scale);
        rPosition.add(context.position);
        rChildContext.assign(context);
        rChildContext.position = rPosition;
        return input.process(rChildContext);
    }

    @Override
    public void setInputs(Density[] inputs) {
        input = inputs[0];
    }
}
