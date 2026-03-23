package io.aresched.policy;

import io.aresched.core.TaskRecord;

public interface SchedulingPolicy {
    void enqueue(TaskRecord<?> task);

    TaskRecord<?> take(int workerId) throws InterruptedException;

    boolean isEmpty();

    void signalAll();
}