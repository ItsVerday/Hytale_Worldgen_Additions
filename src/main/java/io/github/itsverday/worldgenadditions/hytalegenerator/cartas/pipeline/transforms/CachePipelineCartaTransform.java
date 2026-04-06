package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.util.ModuloXZInt2IntCache;
import io.github.itsverday.worldgenadditions.util.ModuloXZIntCache;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;
import java.util.List;

public class CachePipelineCartaTransform extends PipelineCartaTransform {
    @Nonnull
    private final PipelineCartaTransform child;
    private final ModuloXZInt2IntCache cache;

    public CachePipelineCartaTransform(@Nonnull PipelineCartaTransform child) {
        this.child = child;
        cache = new ModuloXZInt2IntCache(8);
    }

    @Override
    public int process(@NonNullDecl Context context) {
        int x = (int) context.position.x;
        int y = (int) context.position.y;
        if (!cache.containsKey(x, y)) {
            int value = child.process(context);
            cache.put(x, y, value);
            return value;
        }

        return cache.get(x, y);
    }

    @Override
    public List<Integer> allPossibleValues() {
        return child.allPossibleValues();
    }
}
