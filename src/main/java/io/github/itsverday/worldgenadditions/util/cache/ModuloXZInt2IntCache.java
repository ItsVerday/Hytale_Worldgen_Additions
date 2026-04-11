package io.github.itsverday.worldgenadditions.util.cache;

public class ModuloXZInt2IntCache {
    private final int moduloBits;
    private final int[] valueCache;
    private final int[] realXCache;
    private final int[] realYCache;

    public ModuloXZInt2IntCache(int moduloBits) {
        this.moduloBits = moduloBits;
        int cacheSize = 1 << (moduloBits * 2);
        valueCache = new int[cacheSize];
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

    public int get(int x, int y) {
        int index = indexForPosition(x, y);
        if (x != realXCache[index]) return 0;
        if (y != realYCache[index]) return 0;
        return valueCache[index];
    }

    public boolean containsKey(int x, int y) {
        int index = indexForPosition(x, y);
        if (x != realXCache[index]) return false;
        return y == realYCache[index];
    }

    public void put(int x, int y, int value) {
        int index = indexForPosition(x, y);
        valueCache[index] = value;
        realXCache[index] = x;
        realYCache[index] = y;
    }
}
