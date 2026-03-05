package me.verday.worldgenadditions.util;

import com.hypixel.hytale.math.vector.Vector2i;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ModuloVector2iCache<V> {
    private final int moduloBits;
    private final Object[] valueCache;
    private final Vector2i[] realPositionCache;

    public ModuloVector2iCache(int moduloBits) {
        this.moduloBits = moduloBits;
        int cacheSize = 1 << (moduloBits * 2);
        valueCache = new Object[cacheSize];
        realPositionCache = new Vector2i[cacheSize];
    }

    private static int modulo(int num, int bits) {
        return num & ((1 << bits) - 1);
    }

    private int indexForPosition(@Nonnull Vector2i position) {
        return modulo(Objects.requireNonNull(position).x, moduloBits) | (modulo(position.y, moduloBits) << moduloBits);
    }

    @Nullable
    public V get(@Nonnull Vector2i position) {
        int index = indexForPosition(position);
        Vector2i realPosition = realPositionCache[index];
        if (!position.equals(realPosition)) return null;

        return (V) valueCache[index];
    }

    public boolean containsKey(@Nonnull Vector2i position) {
        int index = indexForPosition(position);
        Vector2i realPosition = realPositionCache[index];
        return position.equals(realPosition);
    }

    public void put(@Nonnull Vector2i position, @Nonnull V value) {
        int index = indexForPosition(position);
        valueCache[index] = value;
        realPositionCache[index] = position;
    }
}
