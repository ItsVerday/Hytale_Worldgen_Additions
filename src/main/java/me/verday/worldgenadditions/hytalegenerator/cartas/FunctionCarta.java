package me.verday.worldgenadditions.hytalegenerator.cartas;

import com.hypixel.hytale.builtin.hytalegenerator.framework.interfaces.functions.BiCarta;
import com.hypixel.hytale.builtin.hytalegenerator.threadindexer.WorkerIndexer;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FunctionCarta<T, R> extends BiCarta<R> {
    private final BiCarta<T> input;
    private final Function<T, R> function;

    public FunctionCarta(BiCarta<T> input, Function<T, R> function) {
        this.input = input;
        this.function = function;
    }

    @Override
    public R apply(int x, int z, @NonNullDecl WorkerIndexer.Id id) {
        return function.apply(input.apply(x, z, id));
    }

    @Override
    public List<R> allPossibleValues() {
        ArrayList<R> values = new ArrayList<>();
        for (T value: input.allPossibleValues()) {
            values.add(function.apply(value));
        }

        return values;
    }
}
