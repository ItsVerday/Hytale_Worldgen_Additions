package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline;

import com.hypixel.hytale.builtin.hytalegenerator.workerindexer.WorkerIndexer;
import com.hypixel.hytale.math.vector.Vector2d;
import com.hypixel.hytale.math.vector.Vector2i;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class PipelineCartaTransform {
    public abstract int process(@Nonnull Context context);
    public abstract List<Integer> allPossibleValues();

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
