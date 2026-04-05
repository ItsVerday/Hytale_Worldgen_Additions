package io.github.itsverday.worldgenadditions.hytalegenerator.cartas;

import com.hypixel.hytale.builtin.hytalegenerator.workerindexer.WorkerIndexer;
import com.hypixel.hytale.builtin.hytalegenerator.worldstructure.BiCarta;
import com.hypixel.hytale.math.vector.Vector2d;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.List;

public class PipelineCarta extends BiCarta<Integer> {
    private final PipelineCartaTransform transform;

    public PipelineCarta(PipelineCartaTransform transform) {
        this.transform = transform;
    }

    @Override
    public Integer apply(int x, int z, @NonNullDecl WorkerIndexer.Id id) {
        PipelineCartaTransform.Context context = new PipelineCartaTransform.Context(new Vector2d(x, z), id);
        return transform.process(context);
    }

    @Override
    public List<Integer> allPossibleValues() {
        return transform.allPossibleValues();
    }
}
