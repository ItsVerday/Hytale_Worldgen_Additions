package io.github.itsverday.worldgenadditions.hytalegenerator.density;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.math.vector.Vector3d;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

public class BasisChangeDensity extends Density {
    private Density input;
    private final Vector3d origin;
    private final Vector3d xAxis;
    private final Vector3d yAxis;
    private final Vector3d zAxis;

    private final Context rChildContext;
    private final Vector3d rPosition;
    private final Vector3d rTransformedPosition;
    private final Vector3d rAxis;

    public BasisChangeDensity(@Nonnull Density input, Vector3d origin, Vector3d xAxis, Vector3d yAxis, Vector3d zAxis) {
        this.input = input;
        this.origin = origin;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.zAxis = zAxis;

        rChildContext = new Context();
        rPosition = new Vector3d();
        rAxis = new Vector3d();
        rTransformedPosition = new Vector3d();
    }

    @Override
    public double process(@NonNullDecl Context context) {
        rPosition.assign(context.position);
        rPosition.subtract(origin);
        rTransformedPosition.assign(0, 0, 0);

        rAxis.assign(xAxis);
        rAxis.scale(rPosition.x);
        rTransformedPosition.add(rAxis);

        rAxis.assign(yAxis);
        rAxis.scale(rPosition.y);
        rTransformedPosition.add(rAxis);

        rAxis.assign(zAxis);
        rAxis.scale(rPosition.z);
        rTransformedPosition.add(rAxis);

        rTransformedPosition.add(origin);
        rChildContext.assign(context);
        rChildContext.position = rTransformedPosition;
        return input.process(rChildContext);
    }

    @Override
    public void setInputs(Density[] inputs) {
        this.input = inputs[0];
    }
}
