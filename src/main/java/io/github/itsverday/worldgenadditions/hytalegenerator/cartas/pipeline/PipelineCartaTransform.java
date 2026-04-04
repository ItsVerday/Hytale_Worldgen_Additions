package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline;

import com.hypixel.hytale.builtin.hytalegenerator.workerindexer.WorkerIndexer;
import com.hypixel.hytale.math.vector.Vector2d;
import com.hypixel.hytale.math.vector.Vector2i;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class PipelineCartaTransform {
    public abstract int process(@Nonnull ContextStack context);
    public abstract List<Integer> allPossibleValues();

    public static class ContextStack {
        private final ArrayList<Context> stack;
        private int stackIndex;
        private final WorkerIndexer.Id workerId;

        public ContextStack(@Nonnull Vector2d position, @Nonnull WorkerIndexer.Id workerId) {
            stack = new ArrayList<>(List.of(new Context(position)));
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

        public void push(@Nonnull Vector2d position) {
            if (stackIndex + 1 == stack.size()) {
                stack.add(new Context(position));
                stackIndex++;
                return;
            }

            Context context = stack.get(++stackIndex);
            context.set(position);
        }

        public void pushWithOffset(double x, double z) {
            Vector2d position = getPosition();
            pushWithPosition(new Vector2d(position.x + x, position.y + z));
        }

        public void pushWithPosition(@Nonnull Vector2d position) {
            push(position);
        }

        public void pop() {
            stackIndex--;
        }
    }

    private static class Context {
        @Nonnull
        private Vector2d position;

        public Context(@Nonnull Vector2d position) {
            this.position = position;
        }

        @Nonnull
        public Vector2d getPosition() {
            return position;
        }

        public void set(@Nonnull Vector2d position) {
            this.position = position;
        }
    }
}
