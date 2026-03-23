package io.aresched.metrics;

import io.aresched.core.TaskRecord;

import java.util.concurrent.atomic.AtomicLong;

public final class MetricsCollector {
    private final AtomicLong submitted = new AtomicLong(0);
    private final AtomicLong rejected = new AtomicLong(0);
    private final AtomicLong started = new AtomicLong(0);
    private final AtomicLong completed = new AtomicLong(0);
    private final AtomicLong failed = new AtomicLong(0);


    public void onSubmit(TaskRecord<?> task) {
        submitted.incrementAndGet();
    }

    public void onRejected(TaskRecord<?> task) {
        rejected.incrementAndGet();
    }

    public void onStart(TaskRecord<?> task) {
        started.incrementAndGet();
    }

    public void onComplete(TaskRecord<?> task) {
        completed.incrementAndGet();
    }

    public void onFail(TaskRecord<?> task) {
        failed.incrementAndGet();
    }

    

    public MetricsSnapshot snapshot() {
        return new MetricsSnapshot(submitted.get(), rejected.get(), started.get(), completed.get(), failed.get());
    }
}