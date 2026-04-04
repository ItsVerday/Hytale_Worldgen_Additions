package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import com.hypixel.hytale.math.vector.Vector2i;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.util.ModuloXZIntCache;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;

public class CachePipelineCartaTransform extends AbstractContextModificationPipelineCartaTransform {
    private final ModuloXZIntCache<Integer> cache;

    public CachePipelineCartaTransform(@Nonnull PipelineCartaTransform child) {
        super(child);
        cache = new ModuloXZIntCache<>(8);
    }

    @Override
    public int process(@NonNullDecl ContextStack stack) {
        Vector2i position = stack.getIntPosition();
        int x = position.x;
        int y = position.y;
        if (!cache.containsKey(x, y)) {
            int value = processChild(stack);
            cache.put(x, y, value);
            return value;
        }

        return cache.get(x, y);
    }
}
