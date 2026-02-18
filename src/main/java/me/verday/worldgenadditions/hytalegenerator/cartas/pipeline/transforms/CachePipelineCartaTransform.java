package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import com.hypixel.hytale.math.vector.Vector2i;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import me.verday.worldgenadditions.util.ModuloVector2iCache;
import me.verday.worldgenadditions.util.WorkerIndexerData;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.Optional;

public class CachePipelineCartaTransform<R> extends AbstractContextModificationPipelineCartaTransform<R> {
    private final WorkerIndexerData<ModuloVector2iCache<Optional<R>>> cache;

    public CachePipelineCartaTransform(PipelineCartaTransform<R> child) {
        super(child);
        cache = new WorkerIndexerData<>(() -> new ModuloVector2iCache<>(6));
    }

    @NullableDecl
    @Override
    public R process(@NonNullDecl Context<R> context) {
        ModuloVector2iCache<Optional<R>> myValueCache = cache.get(context.workerId);
        Vector2i position = context.getIntPosition();
        if (!myValueCache.containsKey(position)) {
            R value = processChild(context);
            myValueCache.put(position, Optional.ofNullable(value));
        }

        return myValueCache.get(position).orElse(null);
    }
}
