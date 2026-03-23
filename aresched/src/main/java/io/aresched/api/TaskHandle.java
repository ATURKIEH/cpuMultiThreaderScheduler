package io.aresched.api;

import io.aresched.core.TaskRecord;
import io.aresched.core.TaskState;
import io.aresched.core.TaskTiming;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public final class TaskHandle<V> {
	private final TaskRecord<V> record;

    public TaskHandle(TaskRecord<V> record) {
        this.record = Objects.requireNonNull(record, "record cannot be null");
    }
    public UUID getTaskId() {
        return record.getId();
    }
    public boolean cancel() {
        return record.tryCancel();
    }
    public TaskState state() {
        return record.getState();
    }
    public TaskTiming timing() {
        return record.getTiming();
    }
    public V get() throws InterruptedException, ExecutionException {
        return record.getFuture().get();
    }
}
