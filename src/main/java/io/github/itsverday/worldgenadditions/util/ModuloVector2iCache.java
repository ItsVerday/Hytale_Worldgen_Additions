package io.github.itsverday.worldgenadditions.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ModuloVector2iCache<V> {
    private final int moduloBits;
    private final Object[] valueCache;
    private final int[] realXCache;
    private final int[] realYCache;

    public ModuloVector2iCache(int moduloBits) {
        this.moduloBits = moduloBits;
        int cacheSize = 1 << (moduloBits * 2);
        valueCache = new Object[cacheSize];
        realXCache = new int[cacheSize];
        realYCache = new int[cacheSize];
        realXCache[0] = -1;
        realYCache[0] = -1;
    }

    private static int modulo(int num, int bits) {
        return num & ((1 << bits) - 1);
    }

    private int indexForPosition(int x, int y) {
        return modulo(x, moduloBits) | (modulo(y, moduloBits) << moduloBits);
    }

    @Nullable
    public V get(int x, int y) {
        int index = indexForPosition(x, y);
        if (x != realXCache[index]) return null;
        if (y != realYCache[index]) return null;
        return (V) valueCache[index];
    }

    public boolean containsKey(int x, int y) {
        int index = indexForPosition(x, y);
        if (x != realXCache[index]) return false;
        return y == realYCache[index];
    }

    public void put(int x, int y, @Nonnull V value) {
        int index = indexForPosition(x, y);
        valueCache[index] = value;
        realXCache[index] = x;
        realYCache[index] = y;
    }
}
