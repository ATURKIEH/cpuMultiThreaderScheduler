package io.aresched.core;

import java.util.Objects;
import java.util.concurrent.Callable;

public final class SchedulableTask<V> {
    private final Callable<V> callable;
    private final TaskMetadata metadata;

    public SchedulableTask(Callable<V> callable, TaskMetadata metadata) {
        this.callable = Objects.requireNonNull(callable, "Callable cannot be null");
        this.metadata = Objects.requireNonNull(metadata, "Metadata cannot be null");
    }
    public Callable<V> getCallable() {
        return callable;
    }
    public TaskMetadata getMetadata() {
        return metadata;
    }
}
