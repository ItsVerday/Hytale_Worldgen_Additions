package io.github.itsverday.worldgenadditions.hytalegenerator.vectorproviders;

import com.hypixel.hytale.builtin.hytalegenerator.vectorproviders.VectorProvider;
import com.hypixel.hytale.math.vector.Vector3d;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class SumVectorProvider extends VectorProvider {
    private final VectorProvider[] children;

    private final Vector3d rChildVector;

    public SumVectorProvider(VectorProvider[] children) {
        this.children = children;

        rChildVector = new Vector3d();
    }

    @Override
    public void process(@NonNullDecl Context context, @NonNullDecl Vector3d vector_out) {
        vector_out.assign(0, 0, 0);

        for (VectorProvider child: children) {
            child.process(context, rChildVector);
            vector_out.add(rChildVector);
        }
    }
}
