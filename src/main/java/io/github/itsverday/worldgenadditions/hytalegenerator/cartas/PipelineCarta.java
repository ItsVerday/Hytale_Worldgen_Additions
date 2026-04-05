package io.github.itsverday.worldgenadditions.hytalegenerator.cartas;

import com.hypixel.hytale.builtin.hytalegenerator.workerindexer.WorkerIndexer;
import com.hypixel.hytale.builtin.hytalegenerator.worldstructure.BiCarta;
import com.hypixel.hytale.math.vector.Vector2d;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.List;

public class PipelineCarta extends BiCarta<Integer> {
    private final PipelineCartaTransform transform;
    private List<Integer> allPossibleValuesCached = null;

    private final Vector2d rPosition;
    private final PipelineCartaTransform.Context rContext;

    public PipelineCarta(PipelineCartaTransform transform) {
        this.transform = transform;
        rPosition = new Vector2d();
        rContext = new PipelineCartaTransform.Context();
    }

    @Override
    public Integer apply(int x, int z, @NonNullDecl WorkerIndexer.Id id) {
        rPosition.assign(x, z);
        rContext.assign(rPosition, id);
        return transform.process(rContext);
    }

    @Override
    public List<Integer> allPossibleValues() {
        if (allPossibleValuesCached == null) allPossibleValuesCached = transform.allPossibleValues();
        return allPossibleValuesCached;
    }
}
