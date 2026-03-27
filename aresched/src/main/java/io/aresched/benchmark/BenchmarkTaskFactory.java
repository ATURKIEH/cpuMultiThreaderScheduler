package io.aresched.benchmark;

import io.aresched.core.SchedulableTask;
import io.aresched.core.TaskHint;
import io.aresched.core.TaskMetadata;

public final class BenchmarkTaskFactory {

    private BenchmarkTaskFactory() {
    }

    public static SchedulableTask<Integer> createCpuTask(int taskId,
                                                         int iterations,
                                                         int priority) {
        return new SchedulableTask<>(
                () -> {
                    long acc = 0;
                    for (int i = 0; i < iterations; i++) {
                        acc += (i % 7);
                    }
                    return taskId + (int) (acc % 1000);
                },
                new TaskMetadata(
                        priority,
                        "cpu-task-" + taskId,
                        iterations,
                        TaskHint.CPU_BOUND
                )
        );
    }

    public static SchedulableTask<Integer> createSleepTask(int taskId,
                                                           long millis,
                                                           int priority) {
        return new SchedulableTask<>(
                () -> {
                    Thread.sleep(millis);
                    return taskId;
                },
                new TaskMetadata(
                        priority,
                        "sleep-task-" + taskId,
                        millis,
                        TaskHint.IO_BOUND
                )
        );
    }
}