package io.aresched.metrics;

import io.aresched.core.TaskRecord;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public final class MetricsCollector {
    private final AtomicLong submitted = new AtomicLong(0);
    private final AtomicLong rejected = new AtomicLong(0);
    private final AtomicLong started = new AtomicLong(0);
    private final AtomicLong completed = new AtomicLong(0);
    private final AtomicLong failed = new AtomicLong(0);
    private final AtomicLong totalQueueWaitNanos = new AtomicLong(0);
    private final AtomicLong totalRunNanos = new AtomicLong(0);
    private final AtomicLong totalEndToEndNanos = new AtomicLong(0);
    private final ConcurrentHashMap<Integer, AtomicLong> workerCompletedCounts = new ConcurrentHashMap<>();
    private final AtomicLong stealAttempts = new AtomicLong(0);
    private final AtomicLong stealSuccesses = new AtomicLong(0);


    public void onSubmit(TaskRecord<?> task) {
        submitted.incrementAndGet();
    }

    public void onRejected(TaskRecord<?> task) {
        rejected.incrementAndGet();
    }

    public void onStart(TaskRecord<?> task) {
        started.incrementAndGet();
        Long wait_time = task.getTiming().getQueueWaitNanos();
        if(wait_time >=0){
            totalQueueWaitNanos.addAndGet(wait_time);
        }
    }

    public void onComplete(TaskRecord<?> task, int workerId) {
        completed.incrementAndGet();
        Long runtime = task.getTiming().getRunTimeNanos();
        Long endtoend = task.getTiming().getEndToEndNanos();
        if (runtime >=0 && endtoend >= 0 ){
            totalRunNanos.addAndGet(runtime);
            totalEndToEndNanos.addAndGet(endtoend);
            workerCompletedCounts.computeIfAbsent(workerId, id -> new AtomicLong(0)).incrementAndGet();
        }
    }

    public void onFail(TaskRecord<?> task, int workerId) {
        failed.incrementAndGet();
        Long runtime = task.getTiming().getRunTimeNanos();
        Long endtoend = task.getTiming().getEndToEndNanos();
        if (runtime >=0 && endtoend >= 0 ){
            totalRunNanos.addAndGet(runtime);
            totalEndToEndNanos.addAndGet(endtoend);
            workerCompletedCounts.computeIfAbsent(workerId, id -> new AtomicLong(0)).incrementAndGet();
        }
    }

    public void recordStealAttempt(){
        stealAttempts.incrementAndGet();
    }

    public void recordStealSuccess(){
        stealSuccesses.incrementAndGet();
    }

    

    public MetricsSnapshot snapshot() {
        Map<Integer, Long> workerCountsSnapshot = workerCompletedCounts.entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().get()
                    ));
        return new MetricsSnapshot(
                    submitted.get(),
                    rejected.get(),
                    started.get(),
                    completed.get(),
                    failed.get(),
                    totalQueueWaitNanos.get(),
                    totalRunNanos.get(),
                    totalEndToEndNanos.get(),
                    workerCountsSnapshot,
                    stealAttempts.get(),
                    stealSuccesses.get()
                );
    }
}