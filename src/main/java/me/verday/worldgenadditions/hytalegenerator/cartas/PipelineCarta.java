package me.verday.worldgenadditions.hytalegenerator.cartas;

import com.hypixel.hytale.builtin.hytalegenerator.biome.BiomeType;
import com.hypixel.hytale.builtin.hytalegenerator.framework.interfaces.functions.BiCarta;
import com.hypixel.hytale.builtin.hytalegenerator.threadindexer.WorkerIndexer;
import com.hypixel.hytale.math.vector.Vector2i;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaStage;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class PipelineCarta extends BiCarta<BiomeType> {
    private final PipelineCartaStage[] stages;
    private final Function<String, BiomeType> biomeBuilder;
    private final Map<String, BiomeType> biomeTypeCache = new ConcurrentHashMap<>();

    private int maxPipelineBiomeDistance = 0;

    private List<BiomeType> allPossibleValues = null;

    public PipelineCarta(PipelineCartaStage[] stages, Function<String, BiomeType> biomeBuilder) {
        this.stages = stages;
        this.biomeBuilder = biomeBuilder;

        int index = 0;
        for (PipelineCartaStage stage: stages) {
            stage.setCarta(this);
            stage.setStageIndex(index++);

            int distance = stage.getMaxPipelineBiomeDistance();
            if (distance > maxPipelineBiomeDistance) {
                maxPipelineBiomeDistance = distance;
            }
        }
    }

    @Override
    public BiomeType apply(int x, int z, @NonNullDecl WorkerIndexer.Id id) {
        PipelineCartaStage lastStage = stages[stages.length - 1];
        PipelineCartaTransform.Context ctx = new PipelineCartaTransform.Context(new Vector2i(x, z), id, lastStage, true);
        return biomeTypeById(lastStage.queryBiome(ctx));
    }

    public PipelineCartaStage getPreviousStage(int stageIndex) {
        do {
            stageIndex--;
        } while (stages[stageIndex].isSkipped());

        return stages[stageIndex];
    }

    @Nullable
    public BiomeType biomeTypeById(String biomeId) {
        if (!biomeTypeCache.containsKey(biomeId)) {
            BiomeType biomeType = biomeBuilder.apply(biomeId);
            if (biomeType != null) biomeTypeCache.put(biomeId, biomeType);
        }

        return biomeTypeCache.get(biomeId);
    }

    @Override
    public List<BiomeType> allPossibleValues() {
        if (allPossibleValues == null) {
            List<String> allPossibleBiomeIds = new ArrayList<>();
            for (PipelineCartaStage stage: stages) {
                for (String possibility: stage.allPossibleValues()) {
                    if (!allPossibleBiomeIds.contains(possibility)) {
                        allPossibleBiomeIds.add(possibility);
                    }
                }
            }

            allPossibleValues = new ArrayList<>();
            for (String biomeId: allPossibleBiomeIds) {
                BiomeType biomeType = biomeTypeById(biomeId);
                if (biomeType != null) allPossibleValues.add(biomeType);
            }
        }

        return allPossibleValues;
    }

    public int getMaxPipelineBiomeDistance() {
        return maxPipelineBiomeDistance;
    }
}
