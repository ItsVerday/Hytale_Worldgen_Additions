package me.verday.worldgenadditions.hytalegenerator.assets.worldstructures.pipeline;

import com.hypixel.hytale.assetstore.AssetExtraInfo;
import com.hypixel.hytale.assetstore.codec.AssetBuilderCodec;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.assetstore.map.JsonAssetWithMap;
import com.hypixel.hytale.builtin.hytalegenerator.assets.Cleanable;
import com.hypixel.hytale.builtin.hytalegenerator.assets.material.MaterialAsset;
import com.hypixel.hytale.builtin.hytalegenerator.biome.Biome;
import com.hypixel.hytale.builtin.hytalegenerator.biome.SimpleBiome;
import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.density.nodes.BaseHeightDensity;
import com.hypixel.hytale.builtin.hytalegenerator.density.nodes.InverterDensity;
import com.hypixel.hytale.builtin.hytalegenerator.environmentproviders.EnvironmentProvider;
import com.hypixel.hytale.builtin.hytalegenerator.material.MaterialCache;
import com.hypixel.hytale.builtin.hytalegenerator.materialproviders.ConstantMaterialProvider;
import com.hypixel.hytale.builtin.hytalegenerator.materialproviders.MaterialProvider;
import com.hypixel.hytale.builtin.hytalegenerator.materialproviders.SolidityMaterialProvider;
import com.hypixel.hytale.builtin.hytalegenerator.tintproviders.TintProvider;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;

import javax.annotation.Nonnull;

public class PipelineCartaDebugBiomeAsset implements Cleanable, JsonAssetWithMap<String, DefaultAssetMap<String, PipelineCartaDebugBiomeAsset>> {
    public static final AssetBuilderCodec<String, PipelineCartaDebugBiomeAsset> CODEC = AssetBuilderCodec.builder(
            PipelineCartaDebugBiomeAsset.class,
            PipelineCartaDebugBiomeAsset::new,
            Codec.STRING,
            (asset, id) -> asset.id = id,
            config -> config.id,
            (asset, data) -> asset.data = data,
            config -> config.data
    )
            .append(new KeyedCodec<>("Name", Codec.STRING, true), (t, k) -> t.biomeName = k, t -> t.biomeName)
            .add()
            .append(new KeyedCodec<>("Material", MaterialAsset.CODEC, true), (t, k) -> t.materialAsset = k, t -> t.materialAsset)
            .add()
            .build();

    private String id;
    private AssetExtraInfo.Data data;
    private String biomeName = "DebugBiome";
    private MaterialAsset materialAsset = new MaterialAsset();

    @Override
    public String getId() {
        return id;
    }

    public String getBiomeName() {
        return biomeName;
    }

    @Override
    public void cleanUp() {
        materialAsset.cleanUp();
    }

    private static final Density flatDensity = new InverterDensity(new BaseHeightDensity(100, true));

    @Nonnull
    public Biome build(@Nonnull MaterialCache materialCache) {
        return new SimpleBiome(biomeName, flatDensity, new SolidityMaterialProvider<>(new ConstantMaterialProvider<>(materialAsset.build(materialCache)), MaterialProvider.noMaterialProvider()), EnvironmentProvider.noEnvironmentProvider(), TintProvider.noTintProvider());
    }
}
