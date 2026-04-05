package io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.transforms;

import com.hypixel.hytale.builtin.hytalegenerator.density.Density;
import com.hypixel.hytale.math.vector.Vector3d;
import io.github.itsverday.worldgenadditions.hytalegenerator.cartas.pipeline.PipelineCartaTransform;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class FieldFunctionPipelineCartaTransform extends PipelineCartaTransform {
    @Nonnull
    private final PipelineCartaTransform previous;
    @Nonnull
    private final Density density;
    @Nonnull
    private final List<FieldDelimiter> delimiters;

    private final Vector3d rChildPosition;
    private final Density.Context rChildContext;

    public FieldFunctionPipelineCartaTransform(@NonNullDecl PipelineCartaTransform previous, @Nonnull Density density, @Nonnull List<FieldDelimiter> delimiters) {
        this.previous = previous;
        this.density = density;
        this.delimiters = delimiters;

        for (FieldDelimiter field: delimiters) {
            if (field == null) {
                throw new IllegalArgumentException("delimiters contain null value");
            }
        }

        rChildPosition = new Vector3d();
        rChildContext = new Density.Context();
    }

    @Override
    public int process(@NonNullDecl Context context) {
        rChildPosition.assign(context.position.x, 0, context.position.y);
        rChildContext.position = rChildPosition;
        double densityValue = density.process(rChildContext);

        for (FieldDelimiter delimiter: delimiters) {
            if (delimiter.isInside(densityValue)) {
                return delimiter.value.process(context);
            }
        }

        return previous.process(context);
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
