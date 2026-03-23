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

    //region Density
    public static final NodeBuilder NODE_DENSITY_BOUNDARY = addNode(HytaleGeneratorNodes.VARIANT_DENSITY.variantNode("Boundary", "BoundaryDensityNode", "Boundary Density"))
            .addContent(HytaleGeneratorNodes.CONTENT_EXPORT_AS)
            .addContent(HytaleGeneratorNodes.CONTENT_SKIP)
            .addContent(Renode.floatContent("Cutoff", "Cutoff").withDefaultValue(0.0).withWidth(50))
            .addContent(Renode.floatContent("Width", "Width").withDefaultValue(1.0).withWidth(50))
            .addContent(Renode.floatContent("Bias", "Bias").withDefaultValue(0.5).withWidth(50))
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
    public static final NodeBuilder NODE_CURVE_STEPS = addNode(HytaleGeneratorNodes.VARIANT_CURVES.variantNode("Steps", "StepsCurve", "Steps Curve"))
            .addContent(HytaleGeneratorNodes.CONTENT_EXPORT_AS)
            .addContent(Renode.floatContent("FromMin", "FromMin").withDefaultValue(0.0).withWidth(100))
            .addContent(Renode.floatContent("FromMax", "FromMax").withDefaultValue(1.0).withWidth(100))
            .addContent(Renode.floatContent("ToMin", "ToMin").withDefaultValue(0.0).withWidth(100))
            .addContent(Renode.floatContent("ToMax", "ToMax").withDefaultValue(1.0).withWidth(100))
            .addContent(Renode.floatContent("CurveExponent", "CurveExponent").withDefaultValue(1.0).withWidth(100))
            .addContent(Renode.integerContent("StepCount", "StepCount").withDefaultValue(1).withWidth(100))
            .addContent(Renode.floatContent("WallWidth", "WallWidth").withDefaultValue(0.0).withWidth(100))
            .addContent(Renode.floatContent("StepSlope", "StepSlope").withDefaultValue(0.0).withWidth(100));
    public static final NodeBuilder NODE_CURVE_THRESHOLD = addNode(HytaleGeneratorNodes.VARIANT_CURVES.variantNode("Threshold", "ThresholdCurve", "Threshold Curve"))
            .addContent(HytaleGeneratorNodes.CONTENT_EXPORT_AS)
            .addContent(Renode.floatContent("Low", "Low").withDefaultValue(0.0).withWidth(100))
            .addContent(Renode.floatContent("High", "High").withDefaultValue(1.0).withWidth(100))
            .addContent(Renode.floatContent("Cutoff", "Cutoff").withDefaultValue(0.0).withWidth(100));
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
