package me.verday.worldgenadditions.util;

import com.hypixel.hytale.builtin.hytalegenerator.threadindexer.WorkerIndexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Supplier;

public class WorkerIndexerData<T> {
    private ArrayList<T> data = new ArrayList<>();
    private Supplier<T> initialize;

    public WorkerIndexerData(Supplier<T> initialize) {
        this.initialize = initialize;
    }

    private void ensureDataSize(WorkerIndexer.Id id) {
        if (id.id >= data.size()) data.addAll(Collections.nCopies(id.id + 1 - data.size(), null));
    }

    public T get(WorkerIndexer.Id id) {
        ensureDataSize(id);
        T value = data.get(id.id);
        if (value != null) return value;

        data.set(id.id, value = initialize.get());
        return value;
    }

    public void set(WorkerIndexer.Id id, T value) {
        ensureDataSize(id);
        data.set(id.id, value);
    }
}
