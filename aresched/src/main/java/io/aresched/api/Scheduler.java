package io.aresched.api;
import io.aresched.core.SchedulableTask;
import io.aresched.metrics.MetricsSnapshot;


public interface Scheduler{
    <V>TaskHandle<V> submit(SchedulableTask<V> task);

    void shutdown();

    boolean isAcceptingTasks();

    MetricsSnapshot metricsSnapshot();
}