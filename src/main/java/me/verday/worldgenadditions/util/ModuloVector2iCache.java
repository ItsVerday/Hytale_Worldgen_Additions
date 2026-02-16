package me.verday.worldgenadditions.util;

import com.hypixel.hytale.math.vector.Vector2i;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ModuloVector2iCache<V> {
    private final int moduloBits;
    private final ArrayList<V> valueCache;
    private final ArrayList<Vector2i> realPositionCache;

    public ModuloVector2iCache(int moduloBits) {
        this.moduloBits = moduloBits;
        valueCache = new ArrayList<>(Collections.nCopies(1 << (moduloBits * 2), null));
        realPositionCache = new ArrayList<>(Collections.nCopies(1 << (moduloBits * 2), null));
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
        Vector2i realPosition = realPositionCache.get(index);
        if (!position.equals(realPosition)) return null;

        return valueCache.get(index);
    }

    public boolean containsKey(@Nonnull Vector2i position) {
        int index = indexForPosition(position);
        Vector2i realPosition = realPositionCache.get(index);
        return position.equals(realPosition);
    }

    public void put(@Nonnull Vector2i position, @Nonnull V value) {
        int index = indexForPosition(position);
        valueCache.set(index, value);
        realPositionCache.set(index, position);
    }
}
