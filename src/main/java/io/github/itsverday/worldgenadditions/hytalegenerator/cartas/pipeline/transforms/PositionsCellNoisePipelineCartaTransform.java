package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import com.hypixel.hytale.builtin.hytalegenerator.bounds.Bounds3d;
import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.builtin.hytalegenerator.density.nodes.positions.distancefunctions.DistanceFunction;
import com.hypixel.hytale.builtin.hytalegenerator.math.Normalizer;
import com.hypixel.hytale.builtin.hytalegenerator.pipe.Pipe;
import com.hypixel.hytale.builtin.hytalegenerator.positionproviders.PositionProvider;
import com.hypixel.hytale.math.util.HashUtil;
import com.hypixel.hytale.math.vector.Vector2d;
import com.hypixel.hytale.math.vector.Vector3d;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

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
    public R process(@NonNullDecl ContextStack<R> stack) {
        // Implementation modified from PositionsDensity
        Vector2d position = stack.getPosition();
        Vector3d min = new Vector3d(position.x - maxDistance - distanceWarpMax, 0, position.y - maxDistance - distanceWarpMax);
        Vector3d max = new Vector3d(position.x + maxDistance + distanceWarpMax, 384, position.y + maxDistance + distanceWarpMax);
        double[] distance = new double[] {Double.MAX_VALUE};
        boolean[] hasClosestPoint = new boolean[1];
        Vector2d closestPoint = new Vector2d();
        Vector3d localPoint = new Vector3d();

        Pipe.One<Vector3d> positionsPipe = (providedPoint, control) -> {
            localPoint.x = providedPoint.x - position.x;
            localPoint.y = 0;
            localPoint.z = providedPoint.z - position.y;
            double newDistance = distanceFunction.getDistance(localPoint);

            if (distanceWarpField != null) {
                newDistance = Math.sqrt(newDistance);
                Density.Context densityContext = new Density.Context();
                densityContext.position.assign(providedPoint.x + position.x, 0, providedPoint.z + position.y);
                densityContext.densityAnchor = new Vector3d(localPoint);
                densityContext.densityAnchor = new Vector3d(localPoint);
                newDistance += Normalizer.normalize(-1, 1, distanceWarpMin, distanceWarpMax, distanceWarpField.process(densityContext));
                newDistance = newDistance * newDistance;
            }

            if (newDistance < maxDistance * maxDistance && newDistance < distance[0]) {
                distance[0] = newDistance;
                closestPoint.assign(new Vector2d(providedPoint.x, providedPoint.z));
                hasClosestPoint[0] = true;
            }
        };

        PositionProvider.Context positionsContext = new PositionProvider.Context();
        positionsContext.bounds = new Bounds3d(min, max);
        positionsContext.pipe = positionsPipe;
        positions.generate(positionsContext);

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
                if (cellValueHere.originValue) {
                    stack.pushWithPosition(closestPoint);
                    R value = cellValueHere.value.process(stack);
                    stack.pop();
                    return value;
                } else {
                    return cellValueHere.value.process(stack);
                }
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
