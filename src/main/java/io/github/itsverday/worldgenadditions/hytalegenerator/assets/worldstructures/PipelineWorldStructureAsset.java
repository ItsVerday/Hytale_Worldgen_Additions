package io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures;

import com.hypixel.hytale.assetstore.codec.ContainedAssetCodec;
import com.hypixel.hytale.builtin.hytalegenerator.assets.biomes.BiomeAsset;
import com.hypixel.hytale.builtin.hytalegenerator.assets.framework.FrameworkAsset;
import com.hypixel.hytale.builtin.hytalegenerator.assets.positionproviders.ListPositionProviderAsset;
import com.hypixel.hytale.builtin.hytalegenerator.assets.positionproviders.PositionProviderAsset;
import com.hypixel.hytale.builtin.hytalegenerator.assets.worldstructures.WorldStructureAsset;
import com.hypixel.hytale.builtin.hytalegenerator.positionproviders.PositionProvider;
import com.hypixel.hytale.builtin.hytalegenerator.referencebundle.ReferenceBundle;
import com.hypixel.hytale.builtin.hytalegenerator.worldstructure.WorldStructure;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.codec.validation.Validators;
import io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaDebugBiomeAsset;
import io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaStageAsset;
import io.github.itsverday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.PipelineCarta;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms.ConstantPipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

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
            .append(new KeyedCodec<>("Framework", new ArrayCodec<>(FrameworkAsset.CODEC, FrameworkAsset[]::new), false), (t, k) -> t.frameworkAssets = k, t -> t.frameworkAssets)
            .add()
            .append(new KeyedCodec<>("SpawnPositions", PositionProviderAsset.CODEC, false), (t, k) -> t.spawnPositionsAsset = k, t -> t.spawnPositionsAsset)
            .add()
            .append(new KeyedCodec<>("Stages", new ArrayCodec<>(PipelineCartaStageAsset.CODEC, PipelineCartaStageAsset[]::new), true), (t, k) -> t.stages = k, t -> t.stages)
            .add()
            .append(new KeyedCodec<>("DebugBiomes", new ArrayCodec<>(PipelineCartaDebugBiomeAsset.CODEC, PipelineCartaDebugBiomeAsset[]::new), false), (t, k) -> t.debugBiomeAssets = k, t -> t.debugBiomeAssets)
            .add()
            .build();

    private int biomeTransitionDistance = 32;
    private int maxBiomeEdgeDistance = 0;
    private String defaultBiomeId = "";
    private FrameworkAsset[] frameworkAssets = new FrameworkAsset[0];
    private PositionProviderAsset spawnPositionsAsset = new ListPositionProviderAsset();
    private PipelineCartaDebugBiomeAsset[] debugBiomeAssets = new PipelineCartaDebugBiomeAsset[0];

    private PipelineCartaStageAsset[] stages = new PipelineCartaStageAsset[0];

    @NullableDecl
    @Override
    public WorldStructure build(@NonNullDecl Argument argument) {
        ReferenceBundle referenceBundle = new ReferenceBundle();

        for (FrameworkAsset frameworkAsset: frameworkAssets) {
            frameworkAsset.build(argument, referenceBundle);
        }

        PipelineCartaTransformAsset.Argument transformArgument = new PipelineCartaTransformAsset.Argument(argument.materialCache, argument.parentSeed, referenceBundle, argument.workerId, defaultBiomeId);
        for (PipelineCartaDebugBiomeAsset debugBiomeAsset: debugBiomeAssets) {
            transformArgument.biomesById.put(debugBiomeAsset.getBiomeName(), debugBiomeAsset.build(argument.materialCache));
        }

        PipelineCartaTransform transform = new ConstantPipelineCartaTransform(transformArgument.cacheBiomeId(defaultBiomeId));

        for (PipelineCartaStageAsset stage: stages) {
            transform = stage.build(transformArgument, transform);
        }

        PipelineCarta biomeCarta = new PipelineCarta(transform);
        int defaultRadius = Math.max(1, this.biomeTransitionDistance / 2);
        PositionProvider spawnPositions = spawnPositionsAsset.build(new PositionProviderAsset.Argument(argument.parentSeed, referenceBundle, argument.workerId));
        return new WorldStructure(biomeCarta, transformArgument.biomeIdRegistry, defaultRadius, maxBiomeEdgeDistance, spawnPositions);
    }

    @NonNullDecl
    @Override
    public PositionProviderAsset getSpawnPositionsAsset() {
        return spawnPositionsAsset;
    }

    @Override
    public void cleanUp() {
        for (FrameworkAsset frameworkAsset: frameworkAssets) {
            frameworkAsset.cleanUp();
        }

        for (PipelineCartaDebugBiomeAsset debugBiomeAsset: debugBiomeAssets) {
            debugBiomeAsset.cleanUp();
        }

        spawnPositionsAsset.cleanUp();

        for (PipelineCartaStageAsset stage: stages) {
            stage.cleanUp();
        }
    }
}
