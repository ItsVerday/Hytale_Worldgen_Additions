package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline;

import com.hypixel.hytale.builtin.hytalegenerator.threadindexer.WorkerIndexer;
import com.hypixel.hytale.math.vector.Vector2d;
import com.hypixel.hytale.math.vector.Vector2i;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class PipelineCartaTransform<R> {
    @Nullable
    public abstract R process(@Nonnull ContextStack<R> context);
    public abstract List<R> allPossibleValues();

    public static class ContextStack<R> {
        private final ArrayList<Context<R>> stack;
        private int stackIndex;
        private final WorkerIndexer.Id workerId;

        public ContextStack(@Nonnull Vector2d position, @Nonnull WorkerIndexer.Id workerId, @Nonnull PipelineCartaStage<R> stage, boolean fallthrough) {
            stack = new ArrayList<>(List.of(new Context<>(position, stage, fallthrough)));
            stackIndex = 0;
            this.workerId = workerId;
        }

        public WorkerIndexer.Id getWorkerId() {
            return workerId;
        }

        public Vector2d getPosition() {
            return stack.get(stackIndex).getPosition();
        }

        public Vector2i getIntPosition() {
            Vector2d position = getPosition();
            return new Vector2i((int) position.x, (int) position.y);
        }

        public PipelineCartaStage<R> getStage() {
            return stack.get(stackIndex).getStage();
        }

        public boolean isFallthrough() {
            return stack.get(stackIndex).isFallthrough();
        }

        public void push(@Nonnull Vector2d position, @Nonnull PipelineCartaStage<R> stage, boolean fallthrough) {
            if (stackIndex >= stack.size()) {
                Context<R> context = new Context<>(position, stage, fallthrough);
                stack.add(context);
                stackIndex++;
            }

            Context<R> context = stack.get(stackIndex++);
            context.set(position, stage, fallthrough);
        }

        public void pushWithStage(@Nonnull PipelineCartaStage<R> stage) {
            push(getPosition(), stage, isFallthrough());
        }

        public void pushWithOffset(double x, double z) {
            Vector2d position = getPosition();
            pushWithPosition(new Vector2d(position.x + x, position.y + z));
        }

        public void pushWithPosition(@Nonnull Vector2d position) {
            push(position, getStage(), isFallthrough());
        }

        public void pushWithFallthrough(boolean fallthrough) {
            push(getPosition(), getStage(), fallthrough);
        }

        public void pop() {
            stackIndex--;
        }
    }

    private static class Context<R> {
        @Nonnull
        private Vector2d position;
        @Nonnull
        private PipelineCartaStage<R> stage;
        private boolean fallthrough;

        public Context(@Nonnull Vector2d position, @Nonnull PipelineCartaStage<R> stage, boolean fallthrough) {
            this.position = position;
            this.stage = stage;
            this.fallthrough = fallthrough;
        }

        @Nonnull
        public Vector2d getPosition() {
            return position;
        }

        @Nonnull
        public PipelineCartaStage<R> getStage() {
            return stage;
        }

        public boolean isFallthrough() {
            return fallthrough;
        }

        public void set(@Nonnull Vector2d position, @Nonnull PipelineCartaStage<R> stage, boolean fallthrough) {
            this.position = position;
            this.stage = stage;
            this.fallthrough = fallthrough;
        }
    }
}
