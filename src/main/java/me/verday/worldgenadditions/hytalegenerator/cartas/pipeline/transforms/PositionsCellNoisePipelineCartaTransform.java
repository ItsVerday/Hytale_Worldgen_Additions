package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import com.hypixel.hytale.builtin.hytalegenerator.density.nodes.positions.distancefunctions.DistanceFunction;
import com.hypixel.hytale.builtin.hytalegenerator.positionproviders.PositionProvider;
import com.hypixel.hytale.math.util.HashUtil;
import com.hypixel.hytale.math.vector.Vector2d;
import com.hypixel.hytale.math.vector.Vector3d;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PositionsCellNoisePipelineCartaTransform<R> extends PipelineCartaTransform<R> {
    private final long seed;
    private final PositionProvider positions;
    private final DistanceFunction distanceFunction;
    private final List<CellValue<R>> cellValues;
    private double maximumWeight;

    private final double maxDistance;
    private final double maxDistanceSquared;

    public PositionsCellNoisePipelineCartaTransform(long seed, PositionProvider positions, DistanceFunction distanceFunction, List<CellValue<R>> cellValues, double maxDistance) {
        this.seed = seed;
        this.positions = positions;
        this.distanceFunction = distanceFunction;
        this.cellValues = cellValues;
        this.maxDistance = maxDistance;
        this.maxDistanceSquared = maxDistance * maxDistance;

        maximumWeight = 0;
        for (CellValue<R> cellValue: cellValues) {
            maximumWeight += cellValue.weight;
        }
    }

    @NullableDecl
    @Override
    public R process(@NonNullDecl Context<R> context) {
        // Implementation modified from PositionsDensity
        Vector3d min = new Vector3d(context.position.x - maxDistance, 0, context.position.y - maxDistanceSquared);
        Vector3d max = new Vector3d(context.position.x + maxDistance, 384, context.position.y + maxDistanceSquared);
        double[] distance = new double[] {Double.MAX_VALUE, Double.MAX_VALUE};
        boolean[] hasClosestPoint = new boolean[2];
        Vector2d closestPoint = new Vector2d();
        Vector2d previousClosestPoint = new Vector2d();
        Vector3d localPoint = new Vector3d();

        Consumer<Vector3d> positionsConsumer = providedPoint -> {
            localPoint.x = providedPoint.x - context.position.x;
            localPoint.y = 0;
            localPoint.z = providedPoint.z - context.position.y;
            double newDistance = this.distanceFunction.getDistance(localPoint);
            if (!(maxDistanceSquared < newDistance)) {
                distance[1] = Math.max(Math.min(distance[1], newDistance), distance[0]);
                if (newDistance < distance[0]) {
                    distance[0] = newDistance;
                    previousClosestPoint.assign(closestPoint);
                    closestPoint.assign(new Vector2d(providedPoint.x, providedPoint.z));
                    hasClosestPoint[1] = hasClosestPoint[0];
                    hasClosestPoint[0] = true;
                }
            }
        };

        PositionProvider.Context positionsContext = new PositionProvider.Context();
        positionsContext.minInclusive = min;
        positionsContext.maxExclusive = max;
        positionsContext.consumer = positionsConsumer;
        positions.positionsIn(positionsContext);
        distance[0] = Math.sqrt(distance[0]);
        distance[1] = Math.sqrt(distance[1]);

        if (hasClosestPoint[0]) {
            double value = HashUtil.random(seed, Double.doubleToLongBits(closestPoint.x), Double.doubleToLongBits(closestPoint.y)) * maximumWeight;
            for (CellValue<R> cellValue : cellValues) {
                value -= cellValue.weight;
                if (value < 0) return cellValue.value.process(context);
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

        public CellValue(double weight, PipelineCartaTransform<R> value) {
            this.weight = weight;
            this.value = value;
        }
    }
}
