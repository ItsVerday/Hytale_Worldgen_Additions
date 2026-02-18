package me.verday.worldgenadditions;

import com.hypixel.hytale.builtin.hytalegenerator.assets.curves.CurveAsset;
import com.hypixel.hytale.builtin.hytalegenerator.assets.density.DensityAsset;
import com.hypixel.hytale.builtin.hytalegenerator.assets.worldstructures.WorldStructureAsset;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import me.verday.worldgenadditions.hytalegenerator.assets.curve.StepsCurveAsset;
import me.verday.worldgenadditions.hytalegenerator.assets.density.BoundaryDensityAsset;
import me.verday.worldgenadditions.hytalegenerator.assets.density.FWidthDensityAsset;
import me.verday.worldgenadditions.hytalegenerator.assets.density.StaticNoise2DDensityAsset;
import me.verday.worldgenadditions.hytalegenerator.assets.density.StaticNoise3DDensityAsset;
import me.verday.worldgenadditions.hytalegenerator.assets.curve.ThresholdCurveAsset;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.PipelineWorldStructureAsset;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.PipelineCartaTransformAsset;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms.*;
import me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline.transforms.conditions.*;

import javax.annotation.Nonnull;

public class WorldgenAdditionsPlugin extends JavaPlugin {
    public WorldgenAdditionsPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        // World Structures
        getCodecRegistry(WorldStructureAsset.CODEC)
                .register("Pipeline", PipelineWorldStructureAsset.class, PipelineWorldStructureAsset.CODEC);

        // Biome Pipeline Transforms
        getCodecRegistry(PipelineCartaTransformAsset.CODEC)
                .register("Constant", ConstantPipelineCartaTransformAsset.class, ConstantPipelineCartaTransformAsset.CODEC)
                .register("FieldFunction", FieldFunctionPipelineCartaTransformAsset.class, FieldFunctionPipelineCartaTransformAsset.CODEC)
                .register("Conditional", ConditionalPipelineCartaTransformAsset.class, ConditionalPipelineCartaTransformAsset.CODEC)
                .register("Queue", QueuePipelineCartaTransformAsset.class, QueuePipelineCartaTransformAsset.CODEC)
                .register("GradientWarp", GradientWarpPipelineCartaTransformAsset.class, GradientWarpPipelineCartaTransformAsset.CODEC)
                .register("PositionsCellNoise", PositionsCellNoisePipelineCartaTransformAsset.class, PositionsCellNoisePipelineCartaTransformAsset.CODEC)
                .register("Rescale", RescalePipelineCartaTransformAsset.class, RescalePipelineCartaTransformAsset.CODEC)
                .register("Smoothing", SmoothingPipelineCartaTransformAsset.class, SmoothingPipelineCartaTransformAsset.CODEC)
                .register("Imported", ImportedPipelineCartaTransformAsset.class, ImportedPipelineCartaTransformAsset.CODEC);

        // Biome Pipeline Conditional Conditions
        getCodecRegistry(ConditionalPipelineCartaTransformAsset.ConditionAsset.CODEC)
                .register("And", AndConditionAsset.class, AndConditionAsset.CODEC)
                .register("Or", OrConditionAsset.class, OrConditionAsset.CODEC)
                .register("Not", NotConditionAsset.class, NotConditionAsset.CODEC)
                .register("Biome", BiomeConditionAsset.class, BiomeConditionAsset.CODEC)
                .register("Distance", DistanceConditionAsset.class, DistanceConditionAsset.CODEC)
                .register("DistanceDensity", DistanceDensityConditionAsset.class, DistanceDensityConditionAsset.CODEC);

        // Density
        getCodecRegistry(DensityAsset.CODEC)
                .register("FWidth", FWidthDensityAsset.class, FWidthDensityAsset.CODEC)
                .register("Boundary", BoundaryDensityAsset.class, BoundaryDensityAsset.CODEC)
                .register("StaticNoise2D", StaticNoise2DDensityAsset.class, StaticNoise2DDensityAsset.CODEC)
                .register("StaticNoise3D", StaticNoise3DDensityAsset.class, StaticNoise3DDensityAsset.CODEC);

        // Curves
        getCodecRegistry(CurveAsset.CODEC)
                .register("Threshold", ThresholdCurveAsset.class, ThresholdCurveAsset.CODEC)
                .register("Steps", StepsCurveAsset.class, StepsCurveAsset.CODEC);
    }
}