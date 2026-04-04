package io.github.itsverday.worldgenadditions;

import io.github.itsverday.renode.builder.NodeBuilder;
import io.github.itsverday.renode.builder.NodeCategory;
import io.github.itsverday.renode.builder.NodeVariantClass;
import io.github.itsverday.renode.builder.Renode;
import io.github.itsverday.renode.builder.root.AbstractNodeRoot;
import io.github.itsverday.renode.vanilla.HytaleGeneratorNodes;

import java.util.ArrayList;
import java.util.List;

public class RenodeIntegration {
    private static final List<NodeBuilder> nodes = new ArrayList<>();
    private static final List<NodeVariantClass> variants = new ArrayList<>();
    private static final List<NodeCategory> categories = new ArrayList<>();
    private static final List<AbstractNodeRoot> roots = new ArrayList<>();

    //region World Structure
    public static final NodeCategory CATEGORY_WORLD_STRUCTURE = addCategory(Renode.category("World Structure", "Grey"));
    public static final NodeVariantClass VARIANT_FRAMEWORK = addVariant(Renode.variant("Framework", "Grey"));

    public static final NodeBuilder NODE_FRAMEWORK_DECIMAL_CONSTANTS_ENTRY = addNode(Renode.node("Framework.DecimalConstantsEntry", "DecimalConstant Entry"))
            .addContent(Renode.smallStringContent("Name", "Name").withDefaultValue("Base").withWidth(200))
            .addContent(Renode.floatContent("Value", "Value").withDefaultValue(0.0).withWidth(100))
            .addCategory(CATEGORY_WORLD_STRUCTURE);
    public static final NodeBuilder NODE_FRAMEWORK_DECIMAL_CONSTANTS = addNode(VARIANT_FRAMEWORK.variantNode("DecimalConstants", "Framework DecimalConstants"))
            .addNodeOutput("Entries", "Entries", true, NODE_FRAMEWORK_DECIMAL_CONSTANTS_ENTRY)
            .addCategory(CATEGORY_WORLD_STRUCTURE);
    public static final NodeBuilder NODE_FRAMEWORK_POSITIONS_ENTRY = addNode(Renode.node("Framework.PositionsEntry", "Positions Entry"))
            .addContent(Renode.smallStringContent("Name", "Name").withDefaultValue("Base").withWidth(200))
            .addVariantOutput("Positions", "Positions", false, HytaleGeneratorNodes.VARIANT_POSITIONS);
    public static final NodeBuilder NODE_FRAMEWORK_POSITIONS = addNode(VARIANT_FRAMEWORK.variantNode("Positions", "Framework Positions"))
            .addNodeOutput("Entries", "Entries", true, NODE_FRAMEWORK_POSITIONS_ENTRY)
            .addCategory(CATEGORY_WORLD_STRUCTURE);
    //endregion

    //region Biome Pipeline
    public static final NodeCategory CATEGORY_BIOME_PIPELINE = addCategory(Renode.category("Biome Pipeline", "30,150,122"));
    public static final NodeVariantClass VARIANT_BIOME_PIPELINE_TRANSFORMS = addVariant(Renode.variant("BiomePipelineTransforms", "32,153,60"));
    public static final NodeVariantClass VARIANT_BIOME_PIPELINE_CONDITIONS = addVariant(Renode.variant("BiomePipelineConditions", "191,158,36"));

    public static final NodeBuilder NODE_BIOME_PIPELINE = addNode(Renode.node("BiomePipeline", "Biome Pipeline"))
            .addContent(Renode.smallStringContent("DefaultBiome", "DefaultBiome").withDefaultValue("").withWidth(200))
            .addContent(Renode.integerContent("DefaultTransitionDistance", "DefaultTransitionDistance").withDefaultValue(32).withWidth(50))
            .addContent(Renode.integerContent("MaxBiomeEdgeDistance", "MaxBiomeEdgeDistance").withDefaultValue(32).withWidth(50))
            .addNodeOutput("Stages", "Stages", true, () -> RenodeIntegration.NODE_BIOME_PIPELINE_STAGE)
            .addVariantOutput("Framework", "Framework", true, VARIANT_FRAMEWORK) // Framework
            .addVariantOutput("SpawnPositions", "SpawnPositions", false, HytaleGeneratorNodes.VARIANT_POSITIONS)
            .addNodeOutput("DebugBiomes", "DebugBiomes", true, () -> RenodeIntegration.NODE_BIOME_PIPELINE_DEBUG_BIOME) // Debug Biomes
            .addCategory(CATEGORY_BIOME_PIPELINE)
            .addSchemaString("Type", "Pipeline");

    public static final AbstractNodeRoot ROOT_PIPELINE_WORLD_STRUCTURE = addRoot(Renode.root(NODE_BIOME_PIPELINE, "Biome Pipeline"));

    public static final NodeBuilder NODE_BIOME_PIPELINE_STAGE = addNode(Renode.node("BiomePipeline.Stage", "Stage"))
            .addContent(HytaleGeneratorNodes.CONTENT_SKIP)
            .addVariantOutput("Root", "Transform", false, VARIANT_BIOME_PIPELINE_TRANSFORMS)
            .addCategory(CATEGORY_BIOME_PIPELINE);
    public static final NodeBuilder NODE_BIOME_PIPELINE_DEBUG_BIOME = addNode(Renode.node("BiomePipeline.DebugBiome", "Debug Biome"))
            .addContent(Renode.smallStringContent("Name", "Name").withDefaultValue("Biome").withWidth(200))
            .addNodeOutput("Material", "Material", false, HytaleGeneratorNodes.NODE_MATERIAL)
            .addCategory(CATEGORY_BIOME_PIPELINE);

    public static final NodeBuilder NODE_BIOME_PIPELINE_TRANSFORM_CACHE = addNode(VARIANT_BIOME_PIPELINE_TRANSFORMS.variantNode("Cache", "BiomePipeline.Transforms.Cache", "Cached Biome Transform"))
            .addContent(HytaleGeneratorNodes.CONTENT_EXPORT_AS, HytaleGeneratorNodes.CONTENT_SKIP)
            .addVariantOutput("Inputs", "Inputs", true, VARIANT_BIOME_PIPELINE_TRANSFORMS)
            .addCategory(CATEGORY_BIOME_PIPELINE);
    public static final NodeBuilder NODE_BIOME_PIPELINE_TRANSFORM_CONDITIONAL = addNode(VARIANT_BIOME_PIPELINE_TRANSFORMS.variantNode("Conditional", "BiomePipeline.Transforms.Conditional", "Conditional Biome Transform"))
            .addContent(HytaleGeneratorNodes.CONTENT_EXPORT_AS, HytaleGeneratorNodes.CONTENT_SKIP)
            .addVariantOutput("Condition", "Condition", false, VARIANT_BIOME_PIPELINE_CONDITIONS)
            .addVariantOutput("IfTrue", "True", false, VARIANT_BIOME_PIPELINE_TRANSFORMS)
            .addVariantOutput("IfFalse", "False", false, VARIANT_BIOME_PIPELINE_TRANSFORMS)
            .addCategory(CATEGORY_BIOME_PIPELINE);
    public static final NodeBuilder NODE_BIOME_PIPELINE_TRANSFORM_CONSTANT = addNode(VARIANT_BIOME_PIPELINE_TRANSFORMS.variantNode("Constant", "BiomePipeline.Transforms.Constant", "Constant Biome Transform"))
            .addContent(HytaleGeneratorNodes.CONTENT_EXPORT_AS, HytaleGeneratorNodes.CONTENT_SKIP)
            .addContent(Renode.smallStringContent("Value", "Biome").withDefaultValue("").withWidth(200))
            .addCategory(CATEGORY_BIOME_PIPELINE);
    public static final NodeBuilder NODE_BIOME_PIPELINE_TRANSFORM_FIELD_FUNCTION = addNode(VARIANT_BIOME_PIPELINE_TRANSFORMS.variantNode("FieldFunction", "BiomePipeline.Transforms.FieldFunction", "FieldFunction Biome Transform"))
            .addContent(HytaleGeneratorNodes.CONTENT_EXPORT_AS, HytaleGeneratorNodes.CONTENT_SKIP)
            .addVariantOutput("FieldFunction", "Density", false, HytaleGeneratorNodes.VARIANT_DENSITY)
            .addNodeOutput("Delimiters", "Delimiters", true, () -> RenodeIntegration.NODE_BIOME_PIPELINE_TRANSFORM_FIELD_FUNCTION_DELIMITER)
            .addCategory(CATEGORY_BIOME_PIPELINE);
    public static final NodeBuilder NODE_BIOME_PIPELINE_TRANSFORM_FIELD_FUNCTION_DELIMITER = addNode(Renode.node("BiomePipeline.FieldFunctionTransform.Delimiter", "FFBT Delimiter"))
            .addContent(Renode.floatContent("From", "From").withDefaultValue(0.0).withWidth(200))
            .addContent(Renode.floatContent("To", "To").withDefaultValue(0.0).withWidth(200))
            .addVariantOutput("Biome", "Biome", false, VARIANT_BIOME_PIPELINE_TRANSFORMS)
            .withColorOverride("28,145,166")
            .addCategory(CATEGORY_BIOME_PIPELINE);
    public static final NodeBuilder NODE_BIOME_PIPELINE_TRANSFORM_GRADIENT_WARP = addNode(VARIANT_BIOME_PIPELINE_TRANSFORMS.variantNode("GradientWarp", "BiomePipeline.Transforms.GradientWarp", "GradientWarp Biome Transform"))
            .addContent(HytaleGeneratorNodes.CONTENT_EXPORT_AS, HytaleGeneratorNodes.CONTENT_SKIP)
            .addContent(Renode.floatContent("SampleDistance", "SampleDistance").withDefaultValue(1.0).withWidth(50))
            .addContent(Renode.floatContent("WarpFactor", "WarpFactor").withDefaultValue(1.0).withWidth(50))
            .addVariantOutput("WarpField", "WarpField", false, HytaleGeneratorNodes.VARIANT_DENSITY)
            .addVariantOutput("Inputs", "Inputs", true, VARIANT_BIOME_PIPELINE_TRANSFORMS)
            .addCategory(CATEGORY_BIOME_PIPELINE);
    public static final NodeBuilder NODE_BIOME_PIPELINE_TRANSFORM_IMPORTED = addNode(VARIANT_BIOME_PIPELINE_TRANSFORMS.variantNode("Imported", "BiomePipeline.Transforms.Imported", "Imported Biome Transform"))
            .addContent(HytaleGeneratorNodes.CONTENT_SKIP)
            .addContent(Renode.smallStringContent("Name", "Name").withDefaultValue("").withWidth(200))
            .addCategory(CATEGORY_BIOME_PIPELINE);
    public static final NodeBuilder NODE_BIOME_PIPELINE_TRANSFORM_POSITIONS_CELL_NOISE = addNode(VARIANT_BIOME_PIPELINE_TRANSFORMS.variantNode("PositionsCellNoise", "BiomePipeline.Transforms.PositionsCellNoise", "PositionsCellNoise Biome Transform"))
            .addContent(HytaleGeneratorNodes.CONTENT_EXPORT_AS, HytaleGeneratorNodes.CONTENT_SKIP)
            .addContent(Renode.smallStringContent("Seed", "Seed").withDefaultValue("A").withWidth(200))
            .addContent(Renode.floatContent("MaxDistance", "MaxDistance").withDefaultValue(32.0).withWidth(50))
            .addContent(Renode.floatContent("DistanceWarpMin", "DistanceWarpMin").withDefaultValue(0.0).withWidth(50))
            .addContent(Renode.floatContent("DistanceWarpMax", "DistanceWarpMax").withDefaultValue(1.0).withWidth(50))
            .addVariantOutput("Positions", "Positions", false, HytaleGeneratorNodes.VARIANT_POSITIONS)
            .addNodeOutput("DistanceFunction", "DistanceFunction", false, HytaleGeneratorNodes.NODE_DENSITY_POSITIONS_CELL_NOISE_DISTANCE_FUNCTION)
            .addNodeOutput("CellValues", "CellValues", true, () -> RenodeIntegration.NODE_BIOME_PIPELINE_TRANSFORM_POSITIONS_CELL_NOISE_CELL_VALUE) // Cell values
            .addVariantOutput("DistanceWarp", "DistanceWarp", false, HytaleGeneratorNodes.VARIANT_DENSITY)
            .addCategory(CATEGORY_BIOME_PIPELINE);
    public static final NodeBuilder NODE_BIOME_PIPELINE_TRANSFORM_POSITIONS_CELL_NOISE_CELL_VALUE = addNode(Renode.node("BiomePipeline.PCNCellValues.CellValue", "PCN Cell Value"))
            .addContent(Renode.floatContent("Weight", "Weight").withDefaultValue(1.0).withWidth(50))
            .addContent(Renode.checkboxContent("OriginValue", "OriginValue").withDefaultValue(false))
            .addVariantOutput("Biome", "Biome", false, VARIANT_BIOME_PIPELINE_TRANSFORMS)
            .withColorOverride("28,145,166")
            .addCategory(CATEGORY_BIOME_PIPELINE);
    public static final NodeBuilder NODE_BIOME_PIPELINE_TRANSFORM_QUEUE = addNode(VARIANT_BIOME_PIPELINE_TRANSFORMS.variantNode("Queue", "BiomePipeline.Transforms.Queue", "Queue Biome Transform"))
            .addContent(HytaleGeneratorNodes.CONTENT_EXPORT_AS, HytaleGeneratorNodes.CONTENT_SKIP)
            .addVariantOutput("Inputs", "Inputs", true, VARIANT_BIOME_PIPELINE_TRANSFORMS)
            .addCategory(CATEGORY_BIOME_PIPELINE);
    public static final NodeBuilder NODE_BIOME_PIPELINE_TRANSFORM_RESCALE = addNode(VARIANT_BIOME_PIPELINE_TRANSFORMS.variantNode("Rescale", "BiomePipeline.Transforms.Rescale", "Rescale Biome Transform"))
            .addContent(HytaleGeneratorNodes.CONTENT_EXPORT_AS, HytaleGeneratorNodes.CONTENT_SKIP)
            .addContent(Renode.floatContent("Scale", "Scale").withDefaultValue(2.0).withWidth(50))
            .addVariantOutput("Inputs", "Input", true, VARIANT_BIOME_PIPELINE_TRANSFORMS)
            .addCategory(CATEGORY_BIOME_PIPELINE);
    public static final NodeBuilder NODE_BIOME_PIPELINE_TRANSFORM_SMOOTHING = addNode(VARIANT_BIOME_PIPELINE_TRANSFORMS.variantNode("Smoothing", "BiomePipeline.Transforms.Smoothing", "Smooth Biome Transform"))
            .addContent(HytaleGeneratorNodes.CONTENT_EXPORT_AS, HytaleGeneratorNodes.CONTENT_SKIP)
            .addContent(Renode.floatContent("Radius", "Radius").withDefaultValue(5.0).withWidth(50))
            .addContent(Renode.floatContent("Threshold", "Threshold").withDefaultValue(0.5).withWidth(50))
            .addVariantOutput("Inputs", "Input", true, VARIANT_BIOME_PIPELINE_TRANSFORMS)
            .addCategory(CATEGORY_BIOME_PIPELINE);

    public static final NodeBuilder NODE_BIOME_PIPELINE_CONDITION_AND  = addNode(VARIANT_BIOME_PIPELINE_CONDITIONS.variantNode("And", "BiomePipeline.Conditions.And", "And Condition"))
            .addVariantOutput("Conditions", "Conditions", true, VARIANT_BIOME_PIPELINE_CONDITIONS)
            .addCategory(CATEGORY_BIOME_PIPELINE);
    public static final NodeBuilder NODE_BIOME_PIPELINE_CONDITION_BIOME  = addNode(VARIANT_BIOME_PIPELINE_CONDITIONS.variantNode("Biome", "BiomePipeline.Conditions.Biome", "Biome Condition"))
            .addContent(Renode.smallStringContent("Biome", "Biome").withDefaultValue("").withWidth(200))
            .addCategory(CATEGORY_BIOME_PIPELINE);
    public static final NodeBuilder NODE_BIOME_PIPELINE_CONDITION_DISTANCE  = addNode(VARIANT_BIOME_PIPELINE_CONDITIONS.variantNode("Distance", "BiomePipeline.Conditions.Distance", "Distance Condition"))
            .addContent(Renode.floatContent("Distance", "Distance").withDefaultValue(8.0).withWidth(50))
            .addContent(Renode.checkboxContent("Fast", "FastMode").withDefaultValue(false))
            .addVariantOutput("Condition", "Condition", false, VARIANT_BIOME_PIPELINE_CONDITIONS)
            .addCategory(CATEGORY_BIOME_PIPELINE);
    public static final NodeBuilder NODE_BIOME_PIPELINE_CONDITION_DISTANCE_DENSITY  = addNode(VARIANT_BIOME_PIPELINE_CONDITIONS.variantNode("DistanceDensity", "BiomePipeline.Conditions.DistanceDensity", "DistanceDensity Condition"))
            .addContent(Renode.floatContent("DistanceMin", "DistanceMin").withDefaultValue(0.0).withWidth(50))
            .addContent(Renode.floatContent("DistanceMax", "DistanceMax").withDefaultValue(10.0).withWidth(50))
            .addContent(Renode.checkboxContent("Fast", "FastMode").withDefaultValue(false))
            .addVariantOutput("Density", "Density", false, HytaleGeneratorNodes.VARIANT_DENSITY)
            .addVariantOutput("Condition", "Condition", false, VARIANT_BIOME_PIPELINE_CONDITIONS)
            .addCategory(CATEGORY_BIOME_PIPELINE);
    public static final NodeBuilder NODE_BIOME_PIPELINE_CONDITION_NOT  = addNode(VARIANT_BIOME_PIPELINE_CONDITIONS.variantNode("Not", "BiomePipeline.Conditions.Not", "Not Condition"))
            .addVariantOutput("Condition", "Condition", false, VARIANT_BIOME_PIPELINE_CONDITIONS)
            .addCategory(CATEGORY_BIOME_PIPELINE);
    public static final NodeBuilder NODE_BIOME_PIPELINE_CONDITION_OR  = addNode(VARIANT_BIOME_PIPELINE_CONDITIONS.variantNode("Or", "BiomePipeline.Conditions.Or", "Or Condition"))
            .addVariantOutput("Conditions", "Conditions", true, VARIANT_BIOME_PIPELINE_CONDITIONS)
            .addCategory(CATEGORY_BIOME_PIPELINE);
    //endregion

    //region Density
    public static final NodeBuilder NODE_DENSITY_BOUNDARY = addNode(HytaleGeneratorNodes.VARIANT_DENSITY.variantNode("Boundary", "BoundaryDensityNode", "Boundary Density"))
            .addContent(HytaleGeneratorNodes.CONTENT_EXPORT_AS)
            .addContent(HytaleGeneratorNodes.CONTENT_SKIP)
            .addContent(Renode.floatContent("Cutoff", "Cutoff").withDefaultValue(0.0).withWidth(50))
            .addContent(Renode.floatContent("Width", "Width").withDefaultValue(1.0).withWidth(50))
            .addContent(Renode.floatContent("Bias", "Bias").withDefaultValue(0.5).withWidth(50))
            .addVariantOutput("Inputs", "Inputs", true, HytaleGeneratorNodes.VARIANT_DENSITY)
            .addCategory(HytaleGeneratorNodes.CATEGORY_DENSITY);
    public static final NodeBuilder NODE_DENSITY_EROSION = addNode(HytaleGeneratorNodes.VARIANT_DENSITY.variantNode("Erosion", "ErosionDensityNode", "Erosion Density"))
            .addContent(HytaleGeneratorNodes.CONTENT_EXPORT_AS)
            .addContent(HytaleGeneratorNodes.CONTENT_SKIP)
            .addContent(Renode.smallStringContent("Seed", "Seed").withDefaultValue("A").withWidth(250))
            .addContent(Renode.floatContent("SampleDistance", "SampleDistance").withDefaultValue(1.0).withWidth(50))
            .addContent(Renode.integerSliderContent("Octaves", "Octaves", 1, 10, 1).withDefaultValue(1).withWidth(150))
            .addContent(Renode.floatContent("Lacunarity", "Lacunarity").withDefaultValue(2.0).withWidth(50))
            .addContent(Renode.floatContent("Persistence", "Persistence").withDefaultValue(0.5).withWidth(50))
            .addContent(Renode.floatContent("Scale", "Scale").withDefaultValue(50.0).withWidth(50))
            .addContent(Renode.floatContent("Strength", "Strength").withDefaultValue(0.2).withWidth(50))
            .addContent(Renode.floatContent("GullyWeight", "GullyWeight").withDefaultValue(0.5).withWidth(50))
            .addContent(Renode.floatContent("Detail", "Detail").withDefaultValue(1.5).withWidth(50))
            .addContent(Renode.floatContent("RidgeRounding", "RidgeRounding").withDefaultValue(0.1).withWidth(50))
            .addContent(Renode.floatContent("CreaseRounding", "CreaseRounding").withDefaultValue(0.0).withWidth(50))
            .addContent(Renode.floatContent("RoundingMultiplier", "RoundingMultiplier").withDefaultValue(0.1).withWidth(50))
            .addContent(Renode.floatContent("InitialOnset", "InitialOnset").withDefaultValue(1.25).withWidth(50))
            .addContent(Renode.floatContent("GullyOnset", "GullyOnset").withDefaultValue(1.25).withWidth(50))
            .addContent(Renode.floatContent("CellScale", "CellScale").withDefaultValue(0.7).withWidth(50))
            .addContent(Renode.floatContent("Normalization", "Normalization").withDefaultValue(0.5).withWidth(50))
            .addVariantOutput("Inputs", "Inputs", true, HytaleGeneratorNodes.VARIANT_DENSITY)
            .addCategory(HytaleGeneratorNodes.CATEGORY_DENSITY);
    public static final NodeBuilder NODE_DENSITY_F_WIDTH = addNode(HytaleGeneratorNodes.VARIANT_DENSITY.variantNode("FWidth", "FWidthDensityNode", "FWidth Density"))
            .addContent(HytaleGeneratorNodes.CONTENT_EXPORT_AS)
            .addContent(HytaleGeneratorNodes.CONTENT_SKIP)
            .addContent(Renode.floatContent("SampleDistance", "SampleDistance").withDefaultValue(1.0).withWidth(50))
            .addVariantOutput("Inputs", "Inputs", true, HytaleGeneratorNodes.VARIANT_DENSITY)
            .addCategory(HytaleGeneratorNodes.CATEGORY_DENSITY);
    public static final NodeBuilder NODE_DENSITY_STATIC_NOISE_2D = addNode(HytaleGeneratorNodes.VARIANT_DENSITY.variantNode("StaticNoise2D", "StaticNoise2DDensityNode","StaticNoise2D Density"))
            .addContent(HytaleGeneratorNodes.CONTENT_EXPORT_AS)
            .addContent(HytaleGeneratorNodes.CONTENT_SKIP)
            .addContent(Renode.smallStringContent("Seed", "Seed").withDefaultValue("A").withWidth(250))
            .addCategory(HytaleGeneratorNodes.CATEGORY_DENSITY);
    public static final NodeBuilder NODE_DENSITY_STATIC_NOISE_3D = addNode(HytaleGeneratorNodes.VARIANT_DENSITY.variantNode("StaticNoise3D", "StaticNoise3DDensityNode","StaticNoise3D Density"))
            .addContent(HytaleGeneratorNodes.CONTENT_EXPORT_AS)
            .addContent(HytaleGeneratorNodes.CONTENT_SKIP)
            .addContent(Renode.smallStringContent("Seed", "Seed").withDefaultValue("A").withWidth(250))
            .addCategory(HytaleGeneratorNodes.CATEGORY_DENSITY);
    //endregion

    //region Curves
    public static final NodeBuilder NODE_CURVE_SMOOTHSTEP = addNode(HytaleGeneratorNodes.VARIANT_CURVES.variantNode("Smoothstep", "SmoothstepCurve", "Smoothstep Curve"))
            .addContent(HytaleGeneratorNodes.CONTENT_EXPORT_AS)
            .addContent(Renode.floatContent("EdgeMin", "EdgeMin").withDefaultValue(0.0).withWidth(100))
            .addContent(Renode.floatContent("EdgeMax", "EdgeMax").withDefaultValue(1.0).withWidth(100))
            .addContent(Renode.floatContent("Low", "Low").withDefaultValue(0.0).withWidth(100))
            .addContent(Renode.floatContent("High", "High").withDefaultValue(1.0).withWidth(100))
            .addCategory(HytaleGeneratorNodes.CATEGORY_CURVES);
    public static final NodeBuilder NODE_CURVE_STEPS = addNode(HytaleGeneratorNodes.VARIANT_CURVES.variantNode("Steps", "StepsCurve", "Steps Curve"))
            .addContent(HytaleGeneratorNodes.CONTENT_EXPORT_AS)
            .addContent(Renode.floatContent("FromMin", "FromMin").withDefaultValue(0.0).withWidth(100))
            .addContent(Renode.floatContent("FromMax", "FromMax").withDefaultValue(1.0).withWidth(100))
            .addContent(Renode.floatContent("ToMin", "ToMin").withDefaultValue(0.0).withWidth(100))
            .addContent(Renode.floatContent("ToMax", "ToMax").withDefaultValue(1.0).withWidth(100))
            .addContent(Renode.floatContent("CurveExponent", "CurveExponent").withDefaultValue(1.0).withWidth(100))
            .addContent(Renode.integerContent("StepCount", "StepCount").withDefaultValue(1).withWidth(100))
            .addContent(Renode.floatContent("WallWidth", "WallWidth").withDefaultValue(0.0).withWidth(100))
            .addContent(Renode.floatContent("StepSlope", "StepSlope").withDefaultValue(0.0).withWidth(100))
            .addCategory(HytaleGeneratorNodes.CATEGORY_CURVES);
    public static final NodeBuilder NODE_CURVE_THRESHOLD = addNode(HytaleGeneratorNodes.VARIANT_CURVES.variantNode("Threshold", "ThresholdCurve", "Threshold Curve"))
            .addContent(HytaleGeneratorNodes.CONTENT_EXPORT_AS)
            .addContent(Renode.floatContent("Low", "Low").withDefaultValue(0.0).withWidth(100))
            .addContent(Renode.floatContent("High", "High").withDefaultValue(1.0).withWidth(100))
            .addContent(Renode.floatContent("Cutoff", "Cutoff").withDefaultValue(0.0).withWidth(100))
            .addCategory(HytaleGeneratorNodes.CATEGORY_CURVES);
    //endregion

    private static NodeBuilder addNode(NodeBuilder node) {
        nodes.add(node);
        return node;
    }

    private static NodeVariantClass addVariant(NodeVariantClass variant) {
        variants.add(variant);
        return variant;
    }

    private static NodeCategory addCategory(NodeCategory category) {
        categories.add(category);
        return category;
    }

    private static AbstractNodeRoot addRoot(AbstractNodeRoot root) {
        roots.add(root);
        return root;
    }

    public static void registerAllNodes() {
        for (NodeBuilder node: nodes) {
            Renode.registerNode(node);
        }

        for (NodeVariantClass variant: variants) {
            Renode.registerVariant(variant);
        }

        for (NodeCategory category: categories) {
            Renode.registerCategory(category);
        }

        for (AbstractNodeRoot root: roots) {
            Renode.registerRoot(root);
        }
    }
}
