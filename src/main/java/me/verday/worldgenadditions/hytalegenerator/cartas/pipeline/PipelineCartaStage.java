package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline;

import me.verday.worldgenadditions.hytalegenerator.cartas.PipelineCarta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class PipelineCartaStage<R> {
    private PipelineCarta<R> carta = null;
    private PipelineCartaStage<R> previousStage = null;
    private int stageIndex;

    private final PipelineCartaTransform<R> root;
    private final boolean skip;

    public PipelineCartaStage(PipelineCartaTransform<R> root, boolean skip) {
        this.root = root;
        this.skip = skip;
    }

    public void setCarta(PipelineCarta<R> carta) {
        this.carta = carta;
    }

    public void setStageIndex(int stageIndex) {
        this.stageIndex = stageIndex;
    }

    public PipelineCartaStage<R> getPrevious() {
        if (previousStage == null) previousStage = carta.getPreviousStage(stageIndex);
        return previousStage;
    }

    @Nullable
    public R queryValue(@Nonnull PipelineCartaTransform.Context<R> context) {
        R value = root.process(context);
        if (value == null && context.fallthrough) value = context.queryValue();
        return value;
    }

    public List<R> allPossibleValues() {
        return root.allPossibleValues();
    }

    public boolean isSkipped() {
        return skip;
    }
}
