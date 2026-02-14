package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline;

import com.hypixel.hytale.builtin.hytalegenerator.threadindexer.WorkerIndexer;
import com.hypixel.hytale.math.vector.Vector2i;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class PipelineCartaTransform<R> {
    @Nullable
    public abstract R process(@Nonnull Context<R> ctx);
    public abstract List<R> allPossibleValues();

    public int getMaxPipelineValueDistance() {
        return 0;
    }

    public static class Context<R> {
        @Nonnull
        public Vector2i position;
        @Nonnull
        public WorkerIndexer.Id workerId;
        @Nonnull
        public PipelineCartaStage<R> stage;
        public boolean fallthrough;

        public Context(@Nonnull Vector2i position, @Nonnull WorkerIndexer.Id workerId, @Nonnull PipelineCartaStage<R> stage, boolean fallthrough) {
            this.position = position;
            this.workerId = workerId;
            this.stage = stage;
            this.fallthrough = fallthrough;
        }

        public Context(@Nonnull Context<R> ctx) {
            this.position = ctx.position;
            this.workerId = ctx.workerId;
            this.stage = ctx.stage;
            this.fallthrough = ctx.fallthrough;
        }

        public Context<R> withStage(PipelineCartaStage<R> newStage) {
            return new Context<>(position, workerId, newStage, fallthrough);
        }

        public Context<R> withPosition(int x, int z) {
            return new Context<>(new Vector2i(x, z), workerId, stage, fallthrough);
        }

        public Context<R> withOffset(double x, double z) {
            return new Context<>(new Vector2i((int) Math.floor(position.x + x), (int) Math.floor(position.y + z)), workerId, stage, fallthrough);
        }

        public R queryValue() {
            PipelineCartaStage<R> previousStage = stage.getPrevious();
            return previousStage.queryValue(withStage(previousStage));
        }

        public int queryValueDistanceSquared(R value) {
            PipelineCartaStage<R> previousStage = stage.getPrevious();
            return previousStage.queryValueDistanceSquared(withStage(previousStage), value);
        }
    }
}
