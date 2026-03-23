package io.aresched.policy.priority;
import java.util.Comparator;
import io.aresched.core.TaskRecord;

import java.util.Objects;
import java.util.concurrent.PriorityBlockingQueue;
import io.aresched.policy.SchedulingPolicy;

public class PriorityPolicy implements SchedulingPolicy {
    private final PriorityBlockingQueue<TaskRecord<?>> queue;

    public PriorityPolicy() {
        Comparator<TaskRecord<?>> comparator =
        (a, b) -> Integer.compare(
            b.getMetadata().getPriority(),
            a.getMetadata().getPriority()
        );
        this.queue = new PriorityBlockingQueue<>(11, comparator);
    }
    @Override
    public void enqueue(TaskRecord<?> task) {
        Objects.requireNonNull(task, "Task cannot be null");
        queue.offer(task);
    }
    @Override
    public TaskRecord<?> take() throws InterruptedException {
        return queue.take();
    }
    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    @Override
    public void signalAll(){
        // No-op for this implementation since take() will unblock on interrupt
    }
}
