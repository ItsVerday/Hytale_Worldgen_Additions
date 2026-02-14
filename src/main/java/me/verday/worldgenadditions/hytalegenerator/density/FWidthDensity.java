package me.verday.worldgenadditions.hytalegenerator.density;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.math.vector.Vector3d;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

public class FWidthDensity extends Density {
    private Density input;
    private final double sampleDistance;

    public FWidthDensity(@Nonnull Density input, double sampleDistance) {
        assert sampleDistance != 0.0;

        this.input = input;
        this.sampleDistance = Math.abs(sampleDistance);
    }

    @Override
    public double process(@NonNullDecl Context context) {
        double valueAtOrigin = input.process(context);

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

        return Math.abs(dx) + Math.abs(dy) + Math.abs(dz);
    }

    @Override
    public void setInputs(Density[] inputs) {
        this.input = inputs[0];
    }
}
