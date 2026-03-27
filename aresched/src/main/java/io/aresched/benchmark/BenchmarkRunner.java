package io.aresched.benchmark;

import io.aresched.api.SchedulerConfig;
import io.aresched.api.TaskHandle;
import io.aresched.core.AresScheduler;
import io.aresched.core.PolicyType;
import io.aresched.core.SchedulableTask;
import io.aresched.metrics.MetricsCollector;
import io.aresched.metrics.MetricsSnapshot;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public final class BenchmarkRunner {

    public void runCpuBenchmark(PolicyType policyType,
                                int workerCount,
                                int taskCount,
                                int iterations) throws Exception {

        MetricsCollector metricsCollector = new MetricsCollector();

        SchedulerConfig config = new SchedulerConfig(
                workerCount,
                policyType,
                false,
                Duration.ofSeconds(1),
                Duration.ofSeconds(5)
        );

        AresScheduler scheduler = new AresScheduler(config, metricsCollector);
        List<TaskHandle<Integer>> handles = new ArrayList<>();

        long wallStart = System.nanoTime();

        for (int i = 0; i < taskCount; i++) {
            SchedulableTask<Integer> task =
                    BenchmarkTaskFactory.createCpuTask(i, iterations, 1);

            TaskHandle<Integer> handle = scheduler.submit(task);
            handles.add(handle);
        }

        for (TaskHandle<Integer> handle : handles) {
            handle.get();
        }

        long wallEnd = System.nanoTime();
        long wallDuration = wallEnd - wallStart;

        MetricsSnapshot snapshot = scheduler.metricsSnapshot();

        System.out.println("========================================");
        System.out.println("CPU BENCHMARK");
        System.out.println("Policy: " + policyType);
        System.out.println("Worker count: " + workerCount);
        System.out.println("Task count: " + taskCount);
        System.out.println("Iterations per task: " + iterations);
        System.out.println("Wall-clock total ns: " + wallDuration);
        System.out.println("Wall-clock total ms: " + (wallDuration / 1_000_000.0));
        System.out.println("Metrics snapshot: " + snapshot);
        System.out.println("========================================");

        scheduler.shutdown();
    }

    public void runSleepBenchmark(PolicyType policyType,
                                  int workerCount,
                                  int taskCount,
                                  long sleepMillis) throws Exception {

        MetricsCollector metricsCollector = new MetricsCollector();

        SchedulerConfig config = new SchedulerConfig(
                workerCount,
                policyType,
                false,
                Duration.ofSeconds(1),
                Duration.ofSeconds(5)
        );

        AresScheduler scheduler = new AresScheduler(config, metricsCollector);
        List<TaskHandle<Integer>> handles = new ArrayList<>();

        long wallStart = System.nanoTime();

        for (int i = 0; i < taskCount; i++) {
            SchedulableTask<Integer> task =
                    BenchmarkTaskFactory.createSleepTask(i, sleepMillis, 1);

            TaskHandle<Integer> handle = scheduler.submit(task);
            handles.add(handle);
        }

        for (TaskHandle<Integer> handle : handles) {
            handle.get();
        }

        long wallEnd = System.nanoTime();
        long wallDuration = wallEnd - wallStart;

        MetricsSnapshot snapshot = scheduler.metricsSnapshot();

        System.out.println("========================================");
        System.out.println("SLEEP BENCHMARK");
        System.out.println("Policy: " + policyType);
        System.out.println("Worker count: " + workerCount);
        System.out.println("Task count: " + taskCount);
        System.out.println("Sleep per task (ms): " + sleepMillis);
        System.out.println("Wall-clock total ns: " + wallDuration);
        System.out.println("Wall-clock total ms: " + (wallDuration / 1_000_000.0));
        System.out.println("Metrics snapshot: " + snapshot);
        System.out.println("========================================");

        scheduler.shutdown();
    }
}