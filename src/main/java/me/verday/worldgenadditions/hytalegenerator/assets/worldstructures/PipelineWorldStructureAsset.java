package me.verday.worldgenadditions.hytalegenerator.assets.worldstructures;

import com.hypixel.hytale.assetstore.codec.ContainedAssetCodec;
import com.hypixel.hytale.builtin.hytalegenerator.assets.biomes.BiomeAsset;
import com.hypixel.hytale.builtin.hytalegenerator.assets.worldstructures.WorldStructureAsset;
import com.hypixel.hytale.builtin.hytalegenerator.assets.worldstructures.mapcontentfield.BaseHeightContentFieldAsset;
import com.hypixel.hytale.builtin.hytalegenerator.assets.worldstructures.mapcontentfield.ContentFieldAsset;
import com.hypixel.hytale.builtin.hytalegenerator.biomemap.BiomeMap;
import com.hypixel.hytale.builtin.hytalegenerator.biomemap.SimpleBiomeMap;
import com.hypixel.hytale.builtin.hytalegenerator.material.SolidMaterial;
import com.hypixel.hytale.builtin.hytalegenerator.referencebundle.BaseHeightReference;
import com.hypixel.hytale.builtin.hytalegenerator.referencebundle.ReferenceBundle;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.codec.validation.Validators;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaStageAsset;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import me.verday.worldgenadditions.hytalegenerator.cartas.PipelineCarta;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaStage;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.*;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.conditions.*;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.ArrayList;

public class PipelineWorldStructureAsset extends WorldStructureAsset {
    public static final BuilderCodec<PipelineWorldStructureAsset> CODEC = BuilderCodec.builder(PipelineWorldStructureAsset.class, PipelineWorldStructureAsset::new, WorldStructureAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("DefaultBiome", new ContainedAssetCodec<>(BiomeAsset.class, BiomeAsset.CODEC), true), (t, k) -> t.defaultBiomeId = k, t -> t.defaultBiomeId)
            .addValidatorLate(() -> BiomeAsset.VALIDATOR_CACHE.getValidator().late())
            .add()
            .append(new KeyedCodec<>("DefaultTransitionDistance", Codec.INTEGER, true), (t, k) -> t.biomeTransitionDistance = k, t -> t.biomeTransitionDistance)
            .addValidator(Validators.greaterThan(0))
            .add()
            .append(new KeyedCodec<>("MaxBiomeEdgeDistance", Codec.INTEGER, true), (t, k) -> t.maxBiomeEdgeDistance = k, t -> t.maxBiomeEdgeDistance)
            .addValidator(Validators.greaterThanOrEqual(0))
            .add()
            .append(new KeyedCodec<>("ContentFields", new ArrayCodec<>(ContentFieldAsset.CODEC, ContentFieldAsset[]::new), false), (t, k) -> t.contentFieldAssets = k, t -> t.contentFieldAssets)
            .add()
            .append(new KeyedCodec<>("Stages", new ArrayCodec<>(PipelineCartaStageAsset.CODEC, PipelineCartaStageAsset[]::new), true), (t, k) -> t.stages = k, t -> t.stages)
            .add()
            .build();

    private int biomeTransitionDistance = 32;
    private int maxBiomeEdgeDistance = 0;
    private String defaultBiomeId = "";
    private ContentFieldAsset[] contentFieldAssets = new ContentFieldAsset[0];
    private PipelineCartaStageAsset[] stages = new PipelineCartaStageAsset[0];
    
    @Override
    public BiomeMap<SolidMaterial> buildBiomeMap(@NonNullDecl Argument argument) {
        ReferenceBundle referenceBundle = new ReferenceBundle();

        for (int i = this.contentFieldAssets.length - 1; i >= 0; i--) {
            if (this.contentFieldAssets[i] instanceof BaseHeightContentFieldAsset bedAsset) {
                String name = bedAsset.getName();
                double y = bedAsset.getY();
                BaseHeightReference bedLayer = new BaseHeightReference((x, z) -> y);
                referenceBundle.put(name, bedLayer, bedLayer.getClass());
            }
        }

        ArrayList<PipelineCartaStage> finalStages = new ArrayList<>();
        finalStages.add(new PipelineCartaStage(new ConstantPipelineCartaTransform(defaultBiomeId), false));

        PipelineCartaTransformAsset.Argument arg = new PipelineCartaTransformAsset.Argument(argument.materialCache, argument.parentSeed, referenceBundle, argument.workerIndexer);
        for (PipelineCartaStageAsset stage: stages) {
            finalStages.add(stage.build(arg));
        }

        PipelineCarta carta = new PipelineCarta(finalStages.toArray(new PipelineCartaStage[0]), biomeId -> {
            BiomeAsset biomeAsset = BiomeAsset.getAssetStore().getAssetMap().getAsset(biomeId);
            if (biomeAsset == null) return null;
            return biomeAsset.build(argument.materialCache, argument.parentSeed, referenceBundle, argument.workerIndexer);
        });

        SimpleBiomeMap<SolidMaterial> biomeMap = new SimpleBiomeMap<>(carta);
        int defaultRadius = Math.max(1, this.biomeTransitionDistance / 2);
        biomeMap.setDefaultRadius(defaultRadius);
        return biomeMap;
    }

    @Override
    public int getBiomeTransitionDistance() {
        return biomeTransitionDistance;
    }

    @Override
    public int getMaxBiomeEdgeDistance() {
        return maxBiomeEdgeDistance;
    }
}
