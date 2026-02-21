package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.density.nodes.positions.distancefunctions.DistanceFunction;
import com.hypixel.hytale.builtin.hytalegenerator.framework.math.Normalizer;
import com.hypixel.hytale.builtin.hytalegenerator.positionproviders.PositionProvider;
import com.hypixel.hytale.math.util.HashUtil;
import com.hypixel.hytale.math.vector.Vector2d;
import com.hypixel.hytale.math.vector.Vector3d;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PositionsCellNoisePipelineCartaTransform<R> extends PipelineCartaTransform<R> {
    private final long seed;
    private final PositionProvider positions;
    private final DistanceFunction distanceFunction;
    private final List<CellValue<R>> cellValues;
    @Nullable
    private final Density distanceWarpField;
    private final double distanceWarpMin;
    private final double distanceWarpMax;
    private double maximumWeight;

    private final double maxDistance;

    public PositionsCellNoisePipelineCartaTransform(long seed, PositionProvider positions, DistanceFunction distanceFunction, List<CellValue<R>> cellValues, @Nullable Density distanceWarpField, double distanceWarpMin, double distanceWarpMax, double maxDistance) {
        this.seed = seed;
        this.positions = positions;
        this.distanceFunction = distanceFunction;
        this.cellValues = cellValues;
        this.distanceWarpField = distanceWarpField;
        this.distanceWarpMin = distanceWarpMin;
        this.distanceWarpMax = distanceWarpMax;
        this.maxDistance = maxDistance;

        maximumWeight = 0;
        for (CellValue<R> cellValue: cellValues) {
            maximumWeight += cellValue.weight;
        }
    }

    @NullableDecl
    @Override
    public R process(@NonNullDecl Context<R> context) {
        // Implementation modified from PositionsDensity
        Vector3d min = new Vector3d(context.position.x - maxDistance - distanceWarpMax, 0, context.position.y - maxDistance - distanceWarpMax);
        Vector3d max = new Vector3d(context.position.x + maxDistance + distanceWarpMax, 384, context.position.y + maxDistance + distanceWarpMax);
        double[] distance = new double[] {Double.MAX_VALUE};
        boolean[] hasClosestPoint = new boolean[1];
        Density.Context densityContext = new Density.Context();
        densityContext.position = new Vector3d(context.position.x, 0, context.position.y);
        Vector2d closestPoint = new Vector2d();
        Vector3d localPoint = new Vector3d();

        Consumer<Vector3d> positionsConsumer = providedPoint -> {
            localPoint.x = providedPoint.x - context.position.x;
            localPoint.y = 0;
            localPoint.z = providedPoint.z - context.position.y;
            double newDistance = distanceFunction.getDistance(localPoint);

            if (distanceWarpField != null) {
                newDistance = Math.sqrt(newDistance);
                Density.Context densityChildContext = new Density.Context(densityContext);
                densityChildContext.position.add(providedPoint);
                densityChildContext.densityAnchor = new Vector3d(localPoint);
                newDistance += Normalizer.normalize(-1, 1, distanceWarpMin, distanceWarpMax, distanceWarpField.process(densityChildContext));
                newDistance = newDistance * newDistance;
            }

            if (newDistance < maxDistance * maxDistance && newDistance < distance[0]) {
                distance[0] = newDistance;
                closestPoint.assign(new Vector2d(providedPoint.x, providedPoint.z));
                hasClosestPoint[0] = true;
            }
        };

        PositionProvider.Context positionsContext = new PositionProvider.Context();
        positionsContext.minInclusive = min;
        positionsContext.maxExclusive = max;
        positionsContext.consumer = positionsConsumer;
        positions.positionsIn(positionsContext);

        if (hasClosestPoint[0]) {
            double hashValue = HashUtil.random(seed, Double.doubleToLongBits(closestPoint.x), Double.doubleToLongBits(closestPoint.y)) * maximumWeight;

            CellValue<R> cellValueHere = null;
            for (CellValue<R> cellValue: cellValues) {
                hashValue -= cellValue.weight;
                if (hashValue < 0) {
                    cellValueHere = cellValue;
                    break;
                }
            }

            if (cellValueHere != null) {
                Context<R> childContext = new Context<>(context);
                if (cellValueHere.originValue) childContext.position = closestPoint;
                return cellValueHere.value.process(childContext);
            }
        }

        return null;
    }

    @Override
    public List<R> allPossibleValues() {
        ArrayList<R> values = new ArrayList<>();

        for (CellValue<R> cellValue: cellValues) {
            for (R possibility: cellValue.value.allPossibleValues()) {
                if (!values.contains(possibility)) {
                    values.add(possibility);
                }
            }
        }

        return values;
    }

    public static class CellValue<R> {
        double weight;
        PipelineCartaTransform<R> value;
        boolean originValue;

        public CellValue(double weight, PipelineCartaTransform<R> value, boolean originValue) {
            this.weight = weight;
            this.value = value;
            this.originValue = originValue;
        }
    }
}
