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

public class PositionsCellNoisePipelineCartaTransform extends PipelineCartaTransform {
    private final long seed;
    private final PositionProvider positions;
    private final DistanceFunction distanceFunction;
    private final CellValue[] cellValues;
    private double maximumWeight;

    private double maxDistance;
    private double maxDistanceSquared;

    public PositionsCellNoisePipelineCartaTransform(long seed, PositionProvider positions, DistanceFunction distanceFunction, CellValue[] cellValues, double maxDistance) {
        this.seed = seed;
        this.positions = positions;
        this.distanceFunction = distanceFunction;
        this.cellValues = cellValues;
        this.maxDistance = maxDistance;
        this.maxDistanceSquared = maxDistance * maxDistance;

        maximumWeight = 0;
        for (CellValue cellValue: cellValues) {
            maximumWeight += cellValue.weight;
        }
    }

    @NullableDecl
    @Override
    public String process(@NonNullDecl Context ctx) {
        // Implementation modified from PositionsDensity
        Vector3d min = new Vector3d(ctx.position.x - maxDistance, 0, ctx.position.y - maxDistanceSquared);
        Vector3d max = new Vector3d(ctx.position.x + maxDistance, 384, ctx.position.y + maxDistanceSquared);
        double[] distance = new double[]{Double.MAX_VALUE, Double.MAX_VALUE};
        boolean[] hasClosestPoint = new boolean[2];
        Vector2d closestPoint = new Vector2d();
        Vector2d previousClosestPoint = new Vector2d();
        Vector3d localPoint = new Vector3d();

        Consumer<Vector3d> positionsConsumer = providedPoint -> {
            localPoint.x = providedPoint.x - ctx.position.x;
            localPoint.y = 0;
            localPoint.z = providedPoint.z - ctx.position.y;
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
        positionsContext.workerId = ctx.workerId;
        positions.positionsIn(positionsContext);
        distance[0] = Math.sqrt(distance[0]);
        distance[1] = Math.sqrt(distance[1]);

        double value = HashUtil.random(seed, Double.doubleToLongBits(hasClosestPoint[0] ? closestPoint.x : 0), Double.doubleToLongBits(hasClosestPoint[0] ? closestPoint.y : 0)) * maximumWeight;
        for (CellValue cellValue: cellValues) {
            value -= cellValue.weight;
            if (value < 0) return cellValue.value.process(ctx);
        }

        return null;
    }

    @Override
    public List<String> allPossibleValues() {
        ArrayList<String> values = new ArrayList<>();

        for (CellValue cellValue: cellValues) {
            for (String possibility: cellValue.value.allPossibleValues()) {
                if (!values.contains(possibility)) {
                    values.add(possibility);
                }
            }
        }

        return values;
    }

    @Override
    public int getMaxPipelineBiomeDistance() {
        int distance = 0;
        for (CellValue cellValue: cellValues) {
            int newDistance = cellValue.value.getMaxPipelineBiomeDistance();
            if (newDistance > distance) distance = newDistance;
        }

        return distance;
    }

    public static class CellValue {
        double weight;
        PipelineCartaTransform value;

        public CellValue(double weight, PipelineCartaTransform value) {
            this.weight = weight;
            this.value = value;
        }
    }
}
