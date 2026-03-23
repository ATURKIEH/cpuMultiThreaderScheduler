package io.aresched.policy.fifo;
import io.aresched.core.TaskRecord;
import io.aresched.policy.SchedulingPolicy;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class FifoPolicy implements SchedulingPolicy {
    private final BlockingQueue<TaskRecord<?>> queue = new LinkedBlockingQueue<>();

    @Override
    public void enqueue(TaskRecord<?> task) {
        Objects.requireNonNull(task, "task cannot be null");
        queue.offer(task);
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public void signalAll() {
        // no-op for now in Phase 2
    }

    @Override
    public TaskRecord<?> take(int workerId) throws InterruptedException {
        return queue.take();
    }


}
