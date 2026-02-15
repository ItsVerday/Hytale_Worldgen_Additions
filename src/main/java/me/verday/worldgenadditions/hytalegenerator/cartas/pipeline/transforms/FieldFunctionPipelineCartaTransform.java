package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.math.vector.Vector3d;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class FieldFunctionPipelineCartaTransform<R> extends PipelineCartaTransform<R> {
    @Nonnull
    private final Density density;
    @Nonnull
    private final List<FieldDelimiter<R>> delimiters;

    public FieldFunctionPipelineCartaTransform(@Nonnull Density density, @Nonnull List<FieldDelimiter<R>> delimiters) {
        this.density = density;
        this.delimiters = delimiters;

        for (FieldDelimiter<R> field: delimiters) {
            if (field == null) {
                throw new IllegalArgumentException("delimiters contain null value");
            }
        }
    }

    @NullableDecl
    @Override
    public R process(@NonNullDecl Context<R> ctx) {
        Density.Context childContext = new Density.Context();
        childContext.position = new Vector3d(ctx.position.x, 0, ctx.position.y);
        childContext.workerId = ctx.workerId;

        double densityValue = density.process(childContext);
        for (FieldDelimiter<R> delimiter: delimiters) {
            if (delimiter.isInside(densityValue)) {
                return delimiter.value.process(ctx);
            }
        }

        return null;
    }

    @Override
    public List<R> allPossibleValues() {
        ArrayList<R> values = new ArrayList<>();

        for (FieldDelimiter<R> delimiter: delimiters) {
            for (R possibility: delimiter.value.allPossibleValues()) {
                if (!values.contains(possibility)) {
                    values.add(possibility);
                }
            }
        }

        return values;
    }

    @Override
    public int getMaxPipelineValueDistance() {
        int distance = 0;
        for (FieldDelimiter<R> delimiter: delimiters) {
            int newDistance = delimiter.value.getMaxPipelineValueDistance();
            if (newDistance > distance) distance = newDistance;
        }

        return distance;
    }

    public static class FieldDelimiter<R> {
        double top;
        double bottom;
        PipelineCartaTransform<R> value;

        public FieldDelimiter(@Nonnull PipelineCartaTransform<R> value, double bottom, double top) {
            this.bottom = bottom;
            this.top = top;
            this.value = value;
        }

        boolean isInside(double fieldValue) {
            return fieldValue < this.top && fieldValue >= this.bottom;
        }
    }
}
