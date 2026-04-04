package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.math.vector.Vector2d;
import com.hypixel.hytale.math.vector.Vector3d;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class FieldFunctionPipelineCartaTransform extends PipelineCartaTransform {
    @Nonnull
    private final Density density;
    @Nonnull
    private final List<FieldDelimiter> delimiters;

    public FieldFunctionPipelineCartaTransform(@Nonnull Density density, @Nonnull List<FieldDelimiter> delimiters) {
        this.density = density;
        this.delimiters = delimiters;

        for (FieldDelimiter field: delimiters) {
            if (field == null) {
                throw new IllegalArgumentException("delimiters contain null value");
            }
        }
    }

    @Override
    public int process(@NonNullDecl ContextStack stack) {
        Density.Context childContext = new Density.Context();
        Vector2d position = stack.getPosition();
        childContext.position = new Vector3d(position.x, 0, position.y);
        double densityValue = density.process(childContext);

        for (FieldDelimiter delimiter: delimiters) {
            if (delimiter.isInside(densityValue)) {
                return delimiter.value.process(stack);
            }
        }

        return -1;
    }

    @Override
    public List<Integer> allPossibleValues() {
        ArrayList<Integer> values = new ArrayList<>();

        for (FieldDelimiter delimiter: delimiters) {
            for (Integer possibility: delimiter.value.allPossibleValues()) {
                if (!values.contains(possibility)) {
                    values.add(possibility);
                }
            }
        }

        return values;
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
