package io.github.itsverday.worldgenadditions.hytalegenerator.cartas;

import com.hypixel.hytale.builtin.hytalegenerator.workerindexer.WorkerIndexer;
import com.hypixel.hytale.builtin.hytalegenerator.worldstructure.BiCarta;
import com.hypixel.hytale.math.vector.Vector2d;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaStage;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.ArrayList;
import java.util.List;

public class PipelineCarta extends BiCarta<Integer> {
    private final List<PipelineCartaStage> stages;
    private final PipelineCartaStage lastStage;
    private List<Integer> allPossibleValues = null;

    public PipelineCarta(List<PipelineCartaStage> stages) {
        this.stages = stages;

        PipelineCartaStage previousStage = null;
        for (PipelineCartaStage stage: stages) {
            if (!stage.isSkipped()) {
                stage.setPreviousStage(previousStage);
                previousStage = stage;
            }
        }

        lastStage = previousStage;
    }

    @Override
    public Integer apply(int x, int z, @NonNullDecl WorkerIndexer.Id id) {
        PipelineCartaTransform.ContextStack stack = new PipelineCartaTransform.ContextStack(new Vector2d(x, z), id, lastStage, true);
        return lastStage.process(stack);
    }

    @Override
    public List<Integer> allPossibleValues() {
        if (allPossibleValues == null) {
            allPossibleValues = new ArrayList<>();
            for (PipelineCartaStage stage: stages) {
                for (Integer possibility: stage.allPossibleValues()) {
                    if (!allPossibleValues.contains(possibility)) {
                        allPossibleValues.add(possibility);
                    }
                }
            }
        }

        return allPossibleValues;
    }
}
