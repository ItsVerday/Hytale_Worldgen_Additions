package me.verday.worldgenadditions.hytalegenerator.density;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.math.vector.Vector3d;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

public class BoundaryDensity extends Density {
    private Density input;
    private final double cutoff;
    private final double width;
    private final double bias;

    public BoundaryDensity(@Nonnull Density input, double cutoff, double width, double bias) {
        assert width >= 0.0;
        assert bias >= 0.0 && bias <= 1.0;

        this.input = input;
        this.cutoff = cutoff;
        this.width = Math.abs(width);
        this.bias = bias;
    }

    @Override
    public double process(@NonNullDecl Context context) {
        final double sampleDistance = 1.0;

        double valueAtOrigin = input.process(context);
        if (width == 0.0) {
            if (valueAtOrigin >= cutoff) return 1.0;

            return 0.0;
        }

        double newX = context.position.x + sampleDistance;
        double newY = context.position.y + sampleDistance;
        double newZ = context.position.z + sampleDistance;
        Context childContext = new Context(context);

        childContext.position = new Vector3d(newX, context.position.y, context.position.z);
        double deltaX = input.process(childContext) - valueAtOrigin;
        double dx = deltaX / sampleDistance;

        childContext.position = new Vector3d(context.position.x, newY, context.position.z);
        double deltaY = input.process(childContext) - valueAtOrigin;
        double dy = deltaY / sampleDistance;

        childContext.position = new Vector3d(context.position.x, context.position.y, newZ);
        double deltaZ = input.process(childContext) - valueAtOrigin;
        double dz = deltaZ / sampleDistance;

        double fwidth = Math.abs(dx) + Math.abs(dy) + Math.abs(dz);
        return Math.clamp((valueAtOrigin - cutoff + fwidth * width * (1.0 - bias)) / (fwidth * width), 0.0, 1.0);
    }

    @Override
    public void setInputs(Density[] inputs) {
        this.input = inputs[0];
    }
}
