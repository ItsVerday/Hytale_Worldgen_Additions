package io.github.itsverday.worldgenadditions.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ModuloXZDoubleCache<V> {
    private final int moduloBits;
    private final Object[] valueCache;
    private final double[] realXCache;
    private final double[] realYCache;

    public ModuloXZDoubleCache(int moduloBits) {
        this.moduloBits = moduloBits;
        int cacheSize = 1 << (moduloBits * 2);
        valueCache = new Object[cacheSize];
        realXCache = new double[cacheSize];
        realYCache = new double[cacheSize];
        realXCache[0] = -1.0;
        realYCache[0] = -1.0;
    }

    private static int modulo(int num, int bits) {
        return num & ((1 << bits) - 1);
    }

    private int indexForPosition(double x, double y) {
        return modulo((int) x, moduloBits) | (modulo((int) y, moduloBits) << moduloBits);
    }

    @Nullable
    public V get(double x, double y) {
        int index = indexForPosition(x, y);
        if (x != realXCache[index]) return null;
        if (y != realYCache[index]) return null;
        return (V) valueCache[index];
    }

    public boolean containsKey(double x, double y) {
        int index = indexForPosition(x, y);
        if (x != realXCache[index]) return false;
        return y == realYCache[index];
    }

    public void put(double x, double y, @Nonnull V value) {
        int index = indexForPosition(x, y);
        valueCache[index] = value;
        realXCache[index] = x;
        realYCache[index] = y;
    }
}
