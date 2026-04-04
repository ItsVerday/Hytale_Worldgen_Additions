package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import com.hypixel.hytale.math.vector.Vector2i;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.util.ModuloXZIntCache;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.Optional;

public class CachePipelineCartaTransform extends AbstractContextModificationPipelineCartaTransform {
    private final ModuloXZIntCache<Integer> cache;

    public CachePipelineCartaTransform(PipelineCartaTransform child) {
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
        }

        Integer value = cache.get(x, y);
        if (value == null) value = -1;
        return value;
    }
}
