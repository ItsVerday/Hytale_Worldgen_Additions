package io.github.itsverday.worldgenadditions.hytalegenerator.density;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.math.vector.Vector3d;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

public class FWidthDensity extends Density {
    private Density input;
    private final double sampleDistance;

    private final Context rChildContext;
    private final Vector3d rPosition;

    public FWidthDensity(@Nonnull Density input, double sampleDistance) {
        assert sampleDistance != 0.0;

        this.input = input;
        this.sampleDistance = Math.abs(sampleDistance);

        rChildContext = new Context();
        rPosition = new Vector3d();
    }

    @Override
    public double process(@NonNullDecl Context context) {
        rPosition.assign(context.position);
        rChildContext.assign(context);
        rChildContext.position = rPosition;
        double valueAtOrigin = input.process(rChildContext);
        double newX = context.position.x + sampleDistance;
        double newY = context.position.y + sampleDistance;
        double newZ = context.position.z + sampleDistance;

        rChildContext.position.assign(newX, context.position.y, context.position.z);
        double deltaX = input.process(rChildContext) - valueAtOrigin;
        double dx = deltaX / sampleDistance;

        rChildContext.position.assign(context.position.x, newY, context.position.z);
        double deltaY = input.process(rChildContext) - valueAtOrigin;
        double dy = deltaY / sampleDistance;

        rChildContext.position.assign(context.position.x, context.position.y, newZ);
        double deltaZ = input.process(rChildContext) - valueAtOrigin;
        double dz = deltaZ / sampleDistance;

        return Math.abs(dx) + Math.abs(dy) + Math.abs(dz);
    }

    @Override
    public void setInputs(Density[] inputs) {
        this.input = inputs[0];
    }
}
