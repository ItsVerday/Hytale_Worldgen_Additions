package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline;

import com.hypixel.hytale.builtin.hytalegenerator.threadindexer.WorkerIndexer;
import com.hypixel.hytale.math.vector.Vector2i;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class PipelineCartaTransform {
    @Nullable
    public abstract String process(@Nonnull Context ctx);
    public abstract List<String> allPossibleValues();

    public void setInputs(PipelineCartaTransform[] inputs) {
    }

    public int getMaxPipelineBiomeDistance() {
        return 0;
    }

    public static class Context {
        @Nonnull
        public Vector2i position;
        @Nonnull
        public WorkerIndexer.Id workerId;
        @Nonnull
        public PipelineCartaStage stage;
        public boolean fallthrough;

        public Context(@Nonnull Vector2i position, @Nonnull WorkerIndexer.Id workerId, @Nonnull PipelineCartaStage stage, boolean fallthrough) {
            this.position = position;
            this.workerId = workerId;
            this.stage = stage;
            this.fallthrough = fallthrough;
        }

        public Context(@Nonnull Context ctx) {
            this.position = ctx.position;
            this.workerId = ctx.workerId;
            this.stage = ctx.stage;
            this.fallthrough = ctx.fallthrough;
        }

        public Context withStage(PipelineCartaStage newStage) {
            return new Context(position, workerId, newStage, fallthrough);
        }

        public Context withPosition(int x, int z) {
            return new Context(new Vector2i(x, z), workerId, stage, fallthrough);
        }

        public Context withOffset(double x, double z) {
            return new Context(new Vector2i((int) Math.floor(position.x + x), (int) Math.floor(position.y + z)), workerId, stage, fallthrough);
        }

        public String queryBiome() {
            PipelineCartaStage previousStage = stage.getPrevious();
            return previousStage.queryBiome(withStage(previousStage));
        }

        public int queryBiomeDistanceSquared(String biomeId) {
            PipelineCartaStage previousStage = stage.getPrevious();
            return previousStage.queryBiomeDistanceSquared(withStage(previousStage), biomeId);
        }
    }
}
