package me.verday.worldgenadditions.util;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.function.Function;

public class FastReadIntegerCache<V> {
    private final ArrayList<V> values;

    public FastReadIntegerCache() {
        this.values = new ArrayList<>();
    }

    public int add(@Nonnull V value) {
        for (int index = 0; index < values.size(); index++) {
            if (values.get(index).equals(value)) return index;
        }

        int index = values.size();
        values.add(value);
        return index;
    }

    public V get(int index) {
        return values.get(index);
    }

    public <N> FastReadIntegerCache<N> map(Function<V, N> function) {
        FastReadIntegerCache<N> newCache = new FastReadIntegerCache<>();
        newCache.values.addAll(values.stream().map(function).toList());
        return newCache;
    }
}
