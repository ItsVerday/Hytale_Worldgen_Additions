package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline;

import com.hypixel.hytale.builtin.hytalegenerator.threadindexer.WorkerIndexer;
import com.hypixel.hytale.math.vector.Vector2d;
import com.hypixel.hytale.math.vector.Vector2i;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class PipelineCartaTransform<R> {
    @Nullable
    public abstract R process(@Nonnull Context<R> context);
    public abstract List<R> allPossibleValues();

    public static class Context<R> {
        @Nonnull
        public Vector2d position;
        @Nonnull
        public WorkerIndexer.Id workerId;
        @Nonnull
        public PipelineCartaStage<R> stage;
        public boolean fallthrough;

        public Context(@Nonnull Vector2d position, @Nonnull WorkerIndexer.Id workerId, @Nonnull PipelineCartaStage<R> stage, boolean fallthrough) {
            this.position = position;
            this.workerId = workerId;
            this.stage = stage;
            this.fallthrough = fallthrough;
        }

        public Context(@Nonnull Context<R> context) {
            this.position = new Vector2d(context.position);
            this.workerId = context.workerId;
            this.stage = context.stage;
            this.fallthrough = context.fallthrough;
        }

        public Context<R> withStage(PipelineCartaStage<R> newStage) {
            return new Context<>(new Vector2d(position), workerId, newStage, fallthrough);
        }

        public Context<R> withOffset(double x, double z) {
            return new Context<>(new Vector2d(position.x + x, position.y + z), workerId, stage, fallthrough);
        }

        public Vector2i getIntPosition() {
            return new Vector2i((int) position.x, (int) position.y);
        }
    }
}
