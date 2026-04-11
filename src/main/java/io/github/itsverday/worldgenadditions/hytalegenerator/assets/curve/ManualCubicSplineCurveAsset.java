package io.github.itsverday.worldgenadditions.hytalegenerator.assets.curve;

import com.hypixel.hytale.assetstore.AssetExtraInfo;
import com.hypixel.hytale.assetstore.codec.AssetBuilderCodec;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.assetstore.map.JsonAssetWithMap;
import com.hypixel.hytale.builtin.hytalegenerator.assets.curves.CurveAsset;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import io.github.itsverday.worldgenadditions.util.math.CubicSplineFunction;
import io.github.itsverday.worldgenadditions.util.math.CubicSplinePoint;
import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;

public class ManualCubicSplineCurveAsset extends CurveAsset {
    public static final BuilderCodec<ManualCubicSplineCurveAsset> CODEC = BuilderCodec.builder(ManualCubicSplineCurveAsset.class, ManualCubicSplineCurveAsset::new, CurveAsset.ABSTRACT_CODEC)
            .append(new KeyedCodec<>("Points", new ArrayCodec<>(SplinePointAsset.CODEC, SplinePointAsset[]::new), true), (t, k) -> t.points = k, t -> t.points)
            .add()
            .build();

    private SplinePointAsset[] points = new SplinePointAsset[0];

    @Override
    public Double2DoubleFunction build() {
        CubicSplineFunction function = new CubicSplineFunction();

        for (SplinePointAsset point: points) {
            function.addPoint(point.build());
        }

        return function;
    }

    @Override
    public void cleanUp() {}

    public static class SplinePointAsset implements JsonAssetWithMap<String, DefaultAssetMap<String, SplinePointAsset>> {
        public static final AssetBuilderCodec<String, SplinePointAsset> CODEC = AssetBuilderCodec.builder(
                SplinePointAsset.class,
                SplinePointAsset::new,
                Codec.STRING,
                (asset, id) -> asset.id = id,
                config -> config.id,
                (config, data) -> config.data = data,
                config -> config.data
        )
                .append(new KeyedCodec<>("In", Codec.DOUBLE, true), (asset, x) -> asset.x = x, asset -> asset.x)
                .add()
                .append(new KeyedCodec<>("Out", Codec.DOUBLE, true), (asset, y) -> asset.y = y, asset -> asset.y)
                .add()
                .append(new KeyedCodec<>("Slope", Codec.DOUBLE, true), (asset, derivative) -> asset.derivative = derivative, asset -> asset.derivative)
                .add()
                .build();

        private String id;
        private AssetExtraInfo.Data data;
        private double x = 0.0;
        private double y = 0.0;
        private double derivative = 0.0;

        public CubicSplinePoint build() {
            return new CubicSplinePoint(x, y, derivative);
        }

        @Override
        public String getId() {
            return id;
        }

        public double getX() {
            return x;
        }
    }
}
