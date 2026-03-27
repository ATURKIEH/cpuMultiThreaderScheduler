package io.aresched.core;

import java.util.concurrent.atomic.AtomicBoolean;
import io.aresched.policy.SchedulingPolicy;
import io.aresched.policy.fifo.FifoPolicy;
import io.aresched.policy.priority.PriorityPolicy;
import io.aresched.policy.steal.WorkStealingPolicy;
import io.aresched.api.Scheduler;
import io.aresched.api.SchedulerConfig;
import io.aresched.api.TaskHandle;
import io.aresched.metrics.MetricsCollector;
import io.aresched.metrics.MetricsSnapshot;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import io.aresched.core.TaskRecord;
import java.util.concurrent.atomic.AtomicInteger;



public class AresScheduler implements Scheduler {
    private final AtomicBoolean acceptingTasks;
    private final SchedulerConfig config;
    private final SchedulingPolicy policy;
    private final MetricsCollector metricsCollector;
    private final AtomicInteger inFlight;
    private final List<Worker> workers;

    public AresScheduler(SchedulerConfig config, MetricsCollector metricsCollector) {
        this.config = Objects.requireNonNull(config, "config cannot be null");
        this.metricsCollector = Objects.requireNonNull(metricsCollector, "metricsCollector cannot be null");

        this.acceptingTasks = new AtomicBoolean(true);
        this.inFlight = new AtomicInteger(0);
        this.workers = new ArrayList<>();

        this.policy = createPolicy(config.getInitialPolicy());

        for (int i = 0; i < config.getWorkerCount(); i++) {
            Worker worker = new Worker(i, this, policy, this.metricsCollector);
            workers.add(worker);
            worker.start();
        }
    }

    @Override
    public <V> TaskHandle<V> submit(SchedulableTask<V> task) {
        Objects.requireNonNull(task, "task cannot be null");
        TaskRecord<V> record = new TaskRecord<>(task.getCallable(), task.getMetadata());
        TaskHandle<V> handle = new TaskHandle<>(record);
        if (!acceptingTasks.get()) {
            record.tryMarkRejected();
            metricsCollector.onRejected(record);
            return handle;
        }
        record.tryMarkQueued(System.nanoTime());
        policy.enqueue(record);
        metricsCollector.onSubmit(record);
        return handle;
    }

    @Override
    public void shutdown() {
        acceptingTasks.set(false);
        policy.signalAll();
        for (Worker worker : workers) {
            worker.interrupt();
        }
    }

    @Override
    public boolean isAcceptingTasks() {
        return acceptingTasks.get();
    }

    @Override
    public MetricsSnapshot metricsSnapshot() {
        return metricsCollector.snapshot();
    }


    public void incrementInFlight() {
        inFlight.incrementAndGet();
    }

    public void decrementInFlight() {
        inFlight.decrementAndGet();
    }

    public boolean shouldKeepRunning(){
        return acceptingTasks.get() || !policy.isEmpty() || inFlight.get() > 0;

    }

    private SchedulingPolicy createPolicy(PolicyType policyType){
        switch(policyType){
            case FIFO:
                return new FifoPolicy();
            case PRIORITY:
                return new PriorityPolicy();
            case WORK_STEALING:
                return new WorkStealingPolicy(config.getWorkerCount(), metricsCollector);
            default:
                throw new IllegalStateException("Not a policy type:" + policyType);

        }
    }

   

}
