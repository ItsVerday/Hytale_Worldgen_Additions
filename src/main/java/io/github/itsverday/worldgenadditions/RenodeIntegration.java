package io.github.itsverday.worldgenadditions;

import io.github.itsverday.renode.builder.NodeBuilder;
import io.github.itsverday.renode.builder.NodeCategory;
import io.github.itsverday.renode.builder.NodeVariantClass;
import io.github.itsverday.renode.builder.Renode;
import io.github.itsverday.renode.builder.root.AbstractNodeRoot;
import io.github.itsverday.renode.vanilla.update3.VanillaNodes;

import java.util.ArrayList;
import java.util.List;

public class RenodeIntegration {
    private static final List<NodeBuilder> nodes = new ArrayList<>();
    private static final List<NodeVariantClass> variants = new ArrayList<>();
    private static final List<NodeCategory> categories = new ArrayList<>();
    private static final List<AbstractNodeRoot> roots = new ArrayList<>();

    public static final NodeBuilder NODE_DENSITY_STATIC_NOISE_2D = addNode(VanillaNodes.VARIANT_DENSITY.variantNode("StaticNoise2D", "StaticNoise2D Density"))
            .addContent(Renode.smallStringContent("ExportAs", "ExportAs").withDefaultValue("").withWidth(250))
            .addContent(Renode.checkboxContent("Skip", "Skip").withDefaultValue(false))
            .addContent(Renode.smallStringContent("Seed", "Seed").withDefaultValue("A").withWidth(250))
            .withCategory(VanillaNodes.CATEGORY_DENSITY);

    public static final NodeBuilder NODE_DENSITY_STATIC_NOISE_3D = addNode(VanillaNodes.VARIANT_DENSITY.variantNode("StaticNoise3D", "StaticNoise3D Density"))
            .addContent(Renode.smallStringContent("ExportAs", "ExportAs").withDefaultValue("").withWidth(250))
            .addContent(Renode.checkboxContent("Skip", "Skip").withDefaultValue(false))
            .addContent(Renode.smallStringContent("Seed", "Seed").withDefaultValue("A").withWidth(250))
            .withCategory(VanillaNodes.CATEGORY_DENSITY);

    public static final NodeBuilder NODE_DENSITY_F_WIDTH = addNode(VanillaNodes.VARIANT_DENSITY.variantNode("FWidth", "FWidth Density"))
            .addContent(Renode.smallStringContent("ExportAs", "ExportAs").withDefaultValue("").withWidth(250))
            .addContent(Renode.checkboxContent("Skip", "Skip").withDefaultValue(false))
            .addContent(Renode.floatContent("SampleDistance", "SampleDistance").withDefaultValue(1.0).withWidth(50))
            .addVariantOutput("Inputs", "Inputs", true, VanillaNodes.VARIANT_DENSITY)
            .withCategory(VanillaNodes.CATEGORY_DENSITY);

    public static final NodeBuilder NODE_DENSITY_BOUNDARY = addNode(VanillaNodes.VARIANT_DENSITY.variantNode("Boundary", "Boundary Density"))
            .addContent(Renode.smallStringContent("ExportAs", "ExportAs").withDefaultValue("").withWidth(250))
            .addContent(Renode.checkboxContent("Skip", "Skip").withDefaultValue(false))
            .addContent(Renode.floatContent("Cutoff", "Cutoff").withDefaultValue(0.0).withWidth(50))
            .addContent(Renode.floatContent("Width", "Width").withDefaultValue(1.0).withWidth(50))
            .addContent(Renode.floatContent("Bias", "Bias").withDefaultValue(0.5).withWidth(50))
            .withCategory(VanillaNodes.CATEGORY_DENSITY);

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
