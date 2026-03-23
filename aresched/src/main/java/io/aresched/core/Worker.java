package io.aresched.core;

import io.aresched.metrics.MetricsCollector;
import io.aresched.policy.SchedulingPolicy;
import java.util.Objects;

public final class Worker extends Thread {
    private final int workerId;
    private final AresScheduler scheduler;
    private final SchedulingPolicy policy;
    private final MetricsCollector metricsCollector;

    public Worker(int workerId, AresScheduler scheduler, SchedulingPolicy policy, MetricsCollector metricsCollector){
        this.workerId = workerId;
        this.scheduler = Objects.requireNonNull(scheduler, "Scheduler cannot be null");
        this.policy = Objects.requireNonNull(policy, "Policy cannot be null");
        this.metricsCollector = Objects.requireNonNull(metricsCollector, "Metrics collector cannot be null");
        this.setName("aresched-worker-" + workerId);
    }
    @Override
    public void run() {
        try{
            while (scheduler.shouldKeepRunning()){
                TaskRecord<?> task = policy.take();
                boolean running = task.tryMarkRunning(System.nanoTime());
                if (!running) {
                    continue; // Task was rejected or already completed
                }
                metricsCollector.onStart(task);
                scheduler.incrementInFlight();
                executeUnchecked(task);
            }
        }
        catch (InterruptedException e){

            if (!scheduler.isAcceptingTasks()){
                return;
            }

            Thread.currentThread().interrupt();
            return;
        }
    }

    public <V> void executeTask(TaskRecord<V> task){
        try{
            V result = task.getPayload().call();
            task.complete(result, System.nanoTime());
            metricsCollector.onComplete(task);
        }
        catch (Exception e){
            task.fail(e, System.nanoTime());
            metricsCollector.onFail(task);
        }
        finally {
            scheduler.decrementInFlight();
        }
    }

    @SuppressWarnings("unchecked")
    public void executeUnchecked(TaskRecord<?> task){
        executeTask((TaskRecord<Object>) task);
    }


    
}
