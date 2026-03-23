package io.aresched.core;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public final class TaskRecord<V> {
    private final UUID id;
    private final Callable<V> payload;
    private final TaskMetadata metadata;
    private final AtomicReference<TaskState> state;
    private final TaskTiming timing;
    private final CompletableFuture<V> future;

    public TaskRecord(Callable<V> payload, TaskMetadata metadata) {
        this.id = UUID.randomUUID();
        this.payload = Objects.requireNonNull(payload, "payload cannot be null");
        this.metadata = Objects.requireNonNull(metadata, "metadata cannot be null");
        this.state = new AtomicReference<>(TaskState.NEW);
        this.timing = new TaskTiming();
        this.future = new CompletableFuture<>();
    }

    public UUID getId() {
        return id;
    }

    public Callable<V> getPayload() {
        return payload;
    }

    public TaskMetadata getMetadata() {
        return metadata;
    }

    public TaskState getState() {
        return state.get();
    }

    public TaskTiming getTiming() {
        return timing;
    }

    public CompletableFuture<V> getFuture() {
        return future;
    }

    public boolean tryMarkRejected() {
        return state.compareAndSet(TaskState.NEW, TaskState.REJECTED);
    }


    public boolean tryMarkQueued(long submitNanos) {
        boolean changed = state.compareAndSet(TaskState.NEW, TaskState.QUEUED);
        if (changed) {
            timing.markSubmitted(submitNanos);
        }
        return changed;
    }
    public boolean tryMarkRunning(long startNanos) {
        boolean changed = state.compareAndSet(TaskState.QUEUED, TaskState.RUNNING);
        if (changed) {
            timing.markStarted(startNanos);
        }
        return changed;
    }
    public boolean tryCancel() {
        boolean changed = state.compareAndSet(TaskState.QUEUED, TaskState.CANCELLED);
        if (changed) {
            future.cancel(false);
        }
        return changed;
    }

    public void complete(V value, long finishNanos) {
        if (!state.compareAndSet(TaskState.RUNNING, TaskState.COMPLETED)) {
            throw new IllegalStateException("Task is not RUNNING; cannot complete. Current state: " + state.get());
        }
        timing.markFinished(finishNanos);
        future.complete(value);
    }
    public void fail(Throwable throwable, long finishNanos) {
        Objects.requireNonNull(throwable, "throwable cannot be null");

        if (!state.compareAndSet(TaskState.RUNNING, TaskState.FAILED)) {
            throw new IllegalStateException("Task is not RUNNING; cannot fail. Current state: " + state.get());
        }
        timing.markFinished(finishNanos);
        future.completeExceptionally(throwable);
    }
}
