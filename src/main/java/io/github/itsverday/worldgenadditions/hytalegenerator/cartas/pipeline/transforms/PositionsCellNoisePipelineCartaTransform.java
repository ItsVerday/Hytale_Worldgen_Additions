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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PositionsCellNoisePipelineCartaTransform extends PipelineCartaTransform {
    @Nonnull
    private final PipelineCartaTransform previous;

    private final long seed;
    private final PositionProvider positions;
    private final DistanceFunction distanceFunction;
    private final List<CellValue> cellValues;
    @Nullable
    private final Density distanceWarpField;
    private final double distanceWarpMin;
    private final double distanceWarpMax;
    private double maximumWeight;

    private final double maxDistance;

    private final Vector3d rMin;
    private final Vector3d rMax;
    private final Vector2d rClosestPoint;
    private final Vector3d rLocalPoint;
    private final double[] rDistance;
    private final boolean[] rHasClosestPoint;

    private final Density.Context rDensityContext;

    private final Vector2d rChildPosition;
    private final Context rChildContext;

    public PositionsCellNoisePipelineCartaTransform(@NonNullDecl PipelineCartaTransform previous, long seed, PositionProvider positions, DistanceFunction distanceFunction, List<CellValue> cellValues, @Nullable Density distanceWarpField, double distanceWarpMin, double distanceWarpMax, double maxDistance) {
        this.previous = previous;
        this.seed = seed;
        this.positions = positions;
        this.distanceFunction = distanceFunction;
        this.cellValues = cellValues;
        this.distanceWarpField = distanceWarpField;
        this.distanceWarpMin = distanceWarpMin;
        this.distanceWarpMax = distanceWarpMax;
        this.maxDistance = maxDistance;

        maximumWeight = 0;
        for (CellValue cellValue: cellValues) {
            maximumWeight += cellValue.weight;
        }

        rMin = new Vector3d();
        rMax = new Vector3d();
        rClosestPoint = new Vector2d();
        rLocalPoint = new Vector3d();
        rDistance = new double[1];
        rHasClosestPoint = new boolean[1];

        rDensityContext = new Density.Context();
        rDensityContext.densityAnchor = new Vector3d();

        rChildPosition = new Vector2d();
        rChildContext = new Context();
    }

    @Override
    public int process(@NonNullDecl Context context) {
        // Implementation modified from PositionsDensity
        rMin.assign(context.position.x - maxDistance - distanceWarpMax, -1.0, context.position.y - maxDistance - distanceWarpMax);
        rMax.assign(context.position.x + maxDistance + distanceWarpMax, 1.0, context.position.y + maxDistance + distanceWarpMax);
        rDistance[0] = Double.MAX_VALUE;
        rHasClosestPoint[0] = false;
        rClosestPoint.assign(0.0, 0.0);
        rLocalPoint.assign(0.0, 0.0, 0.0);
        Pipe.One<Vector3d> positionsPipe = (providedPoint, control) -> {
            rLocalPoint.x = providedPoint.x - context.position.x;
            rLocalPoint.y = 0;
            rLocalPoint.z = providedPoint.z - context.position.y;
            double newDistance = distanceFunction.getDistance(rLocalPoint);

            if (distanceWarpField != null) {
                newDistance = Math.sqrt(newDistance);
                rDensityContext.position.assign(providedPoint.x + context.position.x, 0, providedPoint.z + context.position.y);
                rDensityContext.densityAnchor.assign(rLocalPoint);
                newDistance += Normalizer.normalize(-1, 1, distanceWarpMin, distanceWarpMax, distanceWarpField.process(rDensityContext));
                newDistance = newDistance * newDistance;
            }

            if (newDistance < maxDistance * maxDistance && newDistance < rDistance[0]) {
                rDistance[0] = newDistance;
                rClosestPoint.assign(providedPoint.x, providedPoint.z);
                rHasClosestPoint[0] = true;
            }
        };

        PositionProvider.Context positionsContext = new PositionProvider.Context();
        positionsContext.bounds.min.assign(rMin);
        positionsContext.bounds.max.assign(rMax);
        positionsContext.pipe = positionsPipe;
        positions.generate(positionsContext);

        if (rHasClosestPoint[0]) {
            double hashValue = HashUtil.random(seed, Double.doubleToLongBits(rClosestPoint.x), Double.doubleToLongBits(rClosestPoint.y)) * maximumWeight;

            CellValue cellValueHere = null;
            for (CellValue cellValue: cellValues) {
                hashValue -= cellValue.weight;
                if (hashValue < 0) {
                    cellValueHere = cellValue;
                    break;
                }
            }

            if (cellValueHere != null) {
                if (cellValueHere.originValue) {
                    rChildPosition.assign(rClosestPoint);
                } else {
                    rChildPosition.assign(context.position);
                }

                rChildContext.assign(context);
                rChildContext.position = rChildPosition;
                return cellValueHere.value.process(rChildContext);
            }
        }

        return previous.process(context);
    }

    @Override
    public List<Integer> allPossibleValues() {
        ArrayList<Integer> values = new ArrayList<>();

        for (CellValue cellValue: cellValues) {
            for (Integer possibility: cellValue.value.allPossibleValues()) {
                if (!values.contains(possibility)) {
                    values.add(possibility);
                }
            }
        }

        for (Integer possibility: previous.allPossibleValues()) {
            if (!values.contains(possibility)) {
                values.add(possibility);
            }
        }

        return values;
    }

    public static class CellValue {
        double weight;
        PipelineCartaTransform value;
        boolean originValue;

        public CellValue(double weight, PipelineCartaTransform value, boolean originValue) {
            this.weight = weight;
            this.value = value;
            this.originValue = originValue;
        }
    }
}
