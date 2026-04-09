package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline;

import com.hypixel.hytale.builtin.hytalegenerator.workerindexer.WorkerIndexer;
import com.hypixel.hytale.math.vector.Vector2d;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.CachePipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConstantPipelineCartaTransform;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class PipelineCartaTransform {
    public abstract int process(@Nonnull Context context);
    public abstract List<Integer> allPossibleValues();

    public static boolean shouldCacheTransform(PipelineCartaTransform transform) {
        if (transform instanceof CachePipelineCartaTransform) return false;
        if (transform instanceof ConstantPipelineCartaTransform) return false;

        return true;
    }

    public static class Context {
        public Vector2d position;
        public WorkerIndexer.Id workerId;

        public Context() {
            this(new Vector2d(), null);
        }

        public Context(Vector2d position, WorkerIndexer.Id workerId) {
            this.position = position;
            this.workerId = workerId;
        }

        public void assign(Context context) {
            this.position = context.position;
            this.workerId = context.workerId;
        }

        public void assign(Vector2d position, WorkerIndexer.Id workerId) {
            this.position = position;
            this.workerId = workerId;
        }
    }
}
