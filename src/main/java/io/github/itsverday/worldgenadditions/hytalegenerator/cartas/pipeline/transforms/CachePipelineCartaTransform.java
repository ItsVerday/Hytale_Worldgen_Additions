package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import com.hypixel.hytale.math.vector.Vector2i;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.util.ModuloVector2iCache;
import io.github.itsverday.worldgenadditions.util.WorkerIndexerData;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.Optional;

public class CachePipelineCartaTransform<R> extends AbstractContextModificationPipelineCartaTransform<R> {
    private final ModuloVector2iCache<Optional<R>> cache;

    public CachePipelineCartaTransform(PipelineCartaTransform<R> child) {
        super(child);
        cache = new ModuloVector2iCache<>(8);
    }

    @NullableDecl
    @Override
    public R process(@NonNullDecl ContextStack<R> stack) {
        Vector2i position = stack.getIntPosition();
        int x = position.x;
        int y = position.y;
        if (!cache.containsKey(x, y)) {
            R value = processChild(stack);
            cache.put(x, y, Optional.ofNullable(value));
        }

        return cache.get(x, y).orElse(null);
    }
}
