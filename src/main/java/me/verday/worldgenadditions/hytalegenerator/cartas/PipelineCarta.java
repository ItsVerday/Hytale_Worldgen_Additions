package me.verday.worldgenadditions.hytalegenerator.cartas;

import com.hypixel.hytale.builtin.hytalegenerator.framework.interfaces.functions.BiCarta;
import com.hypixel.hytale.builtin.hytalegenerator.threadindexer.WorkerIndexer;
import com.hypixel.hytale.math.vector.Vector2d;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaStage;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.ArrayList;
import java.util.List;

public class PipelineCarta<R> extends BiCarta<R> {
    private final List<PipelineCartaStage<R>> stages;
    private List<R> allPossibleValues = null;

    public PipelineCarta(List<PipelineCartaStage<R>> stages) {
        this.stages = stages;

        int index = 0;
        for (PipelineCartaStage<R> stage: stages) {
            stage.setCarta(this);
            stage.setStageIndex(index++);
        }
    }

    @Override
    public R apply(int x, int z, @NonNullDecl WorkerIndexer.Id id) {
        PipelineCartaStage<R> lastStage = getPreviousStage(stages.size());
        PipelineCartaTransform.Context<R> ctx = new PipelineCartaTransform.Context<>(new Vector2d(x, z), id, lastStage, true);
        return lastStage.queryValue(ctx);
    }

    public PipelineCartaStage<R> getPreviousStage(int stageIndex) {
        do {
            stageIndex--;
        } while (stages.get(stageIndex).isSkipped());

        return stages.get(stageIndex);
    }

    @Override
    public List<R> allPossibleValues() {
        if (allPossibleValues == null) {
            allPossibleValues = new ArrayList<>();
            for (PipelineCartaStage<R> stage: stages) {
                for (R possibility: stage.allPossibleValues()) {
                    if (!allPossibleValues.contains(possibility)) {
                        allPossibleValues.add(possibility);
                    }
                }
            }
        }

        return allPossibleValues;
    }
}
