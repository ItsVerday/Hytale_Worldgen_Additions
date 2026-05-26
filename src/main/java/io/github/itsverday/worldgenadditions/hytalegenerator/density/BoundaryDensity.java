package io.github.itsverday.worldgenadditions.hytalegenerator.density;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.joml.Vector3d;

import javax.annotation.Nonnull;

public class BoundaryDensity extends Density {
    private Density input;
    private final double cutoff;
    private final double width;
    private final double bias;

    private final Context rChildContext;
    private final Vector3d rPosition;

    public BoundaryDensity(@Nonnull Density input, double cutoff, double width, double bias) {
        assert width >= 0.0;
        assert bias >= 0.0 && bias <= 1.0;

        this.input = input;
        this.cutoff = cutoff;
        this.width = Math.abs(width);
        this.bias = bias;

        rChildContext = new Context();
        rPosition = new Vector3d();
    }

    @Override
    public double process(@NonNullDecl Context context) {
        final double sampleDistance = 1.0;

        rPosition.set(context.position);
        rChildContext.assign(context);
        rChildContext.position = rPosition;

        double valueAtOrigin = input.process(rChildContext);
        if (width == 0.0) {
            if (valueAtOrigin >= cutoff) return 1.0;
            return 0.0;
        }

        double newX = context.position.x + sampleDistance;
        double newY = context.position.y + sampleDistance;
        double newZ = context.position.z + sampleDistance;

        rChildContext.position.set(newX, context.position.y, context.position.z);
        double deltaX = input.process(rChildContext) - valueAtOrigin;
        double dx = deltaX / sampleDistance;

        rChildContext.position.set(context.position.x, newY, context.position.z);
        double deltaY = input.process(rChildContext) - valueAtOrigin;
        double dy = deltaY / sampleDistance;

        rChildContext.position.set(context.position.x, context.position.y, newZ);
        double deltaZ = input.process(rChildContext) - valueAtOrigin;
        double dz = deltaZ / sampleDistance;

        double fwidth = Math.abs(dx) + Math.abs(dy) + Math.abs(dz);
        return Math.clamp((valueAtOrigin - cutoff + fwidth * width * (1.0 - bias)) / (fwidth * width), 0.0, 1.0);
    }

    @Override
    public void setInputs(Density[] inputs) {
        this.input = inputs[0];
    }
}
