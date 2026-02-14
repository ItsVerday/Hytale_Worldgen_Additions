package me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.math.vector.Vector3d;
import me.verday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class FieldFunctionPipelineCartaTransform extends PipelineCartaTransform {
    @Nonnull
    private final Density density;
    @Nonnull
    private final FieldDelimiter[] delimiters;

    public FieldFunctionPipelineCartaTransform(@Nonnull Density density, @Nonnull List<FieldDelimiter> delimiters) {
        this.density = density;
        this.delimiters = new FieldDelimiter[delimiters.size()];

        for (FieldDelimiter field: delimiters) {
            if (field == null) {
                throw new IllegalArgumentException("delimiters contain null value");
            }
        }

        for (int i = 0; i < delimiters.size(); i++) {
            this.delimiters[i] = delimiters.get(i);
        }
    }

    @NullableDecl
    @Override
    public String process(@NonNullDecl Context ctx) {
        Density.Context childContext = new Density.Context();
        childContext.position = new Vector3d(ctx.position.x, 0, ctx.position.y);
        childContext.workerId = ctx.workerId;

        double densityValue = this.density.process(childContext);
        for (FieldDelimiter delimiter: delimiters) {
            if (delimiter.isInside(densityValue)) {
                return delimiter.value.process(ctx);
            }
        }

        return null;
    }

    @Override
    public List<String> allPossibleValues() {
        ArrayList<String> values = new ArrayList<>();

        for (FieldDelimiter delimiter: delimiters) {
            for (String possibility: delimiter.value.allPossibleValues()) {
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
        for (FieldDelimiter delimiter: delimiters) {
            int newDistance = delimiter.value.getMaxPipelineBiomeDistance();
            if (newDistance > distance) distance = newDistance;
        }

        return distance;
    }

    public static class FieldDelimiter {
        double top;
        double bottom;
        PipelineCartaTransform value;

        public FieldDelimiter(@Nonnull PipelineCartaTransform value, double bottom, double top) {
            this.bottom = bottom;
            this.top = top;
            this.value = value;
        }

        boolean isInside(double fieldValue) {
            return fieldValue < this.top && fieldValue >= this.bottom;
        }
    }
}
