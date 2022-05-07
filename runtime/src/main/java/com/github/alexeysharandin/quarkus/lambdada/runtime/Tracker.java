package com.github.alexeysharandin.quarkus.lambdada.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author <a href="mailto:sanders@yandex.ru">Alexey Sharandin</a>
 */
public class Tracker {
    private final long startTime;
    private long stopTime;
    private long delta;
    private final EntryMetadata metadata;
    private UUID uuid;
    private Tracker parent;

    private final List<Tracker> leafs;

    public Tracker(EntryMetadata metadata) {
        this.startTime = System.currentTimeMillis();
        this.metadata = metadata;
        this.leafs = new ArrayList<>();
    }

    public String className() {
        return metadata.className();
    }

    public String methodName() {
        return metadata.methodName();
    }

    public long startTime() {
        return startTime;
    }

    public void stop() {
        stopTime = System.currentTimeMillis();
        delta = stopTime - startTime;
    }

    public long stopTime() {
        return stopTime;
    }

    public long result() {
        return delta;
    }

    @Override
    public String toString() {
        return "" + result();
    }

    public UUID newUUID() {
        this.uuid = UUID.randomUUID();
        return uuid;
    }

    public void parent(Tracker parent) {
        this.parent = parent;
        this.parent.addLeaf(this);
    }

    void addLeaf(Tracker tracker) {
        leafs.add(tracker);
    }

    public List<Tracker> leafs() {
        return leafs;
    }

    public Tracker parent() {
        return parent;
    }

    public UUID uuid() {
        return uuid;
    }

    public void uuid(UUID uuid) {
        this.uuid = uuid;
    }

}

