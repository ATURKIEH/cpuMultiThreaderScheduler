package io.aresched.api;

import java.time.Duration;
import java.util.Objects;

import io.aresched.core.PolicyType;

public class SchedulerConfig {
    private final int workerCount;
    private final PolicyType initialPolicy;
    private final boolean mlEnabled;
    private final Duration controlLoopPeriod;
    private final Duration policyCooldown;
    public SchedulerConfig(int workerCount, PolicyType initialPolicy, boolean mlEnabled,
                           Duration controlLoopPeriod, Duration policyCooldown) {
        if (workerCount <= 0) {
            throw new IllegalArgumentException("workerCount must be > 0");
        }
        this.workerCount = workerCount;
        this.initialPolicy = Objects.requireNonNull(initialPolicy, "initialPolicy cannot be null");
        this.mlEnabled = mlEnabled;
        this.controlLoopPeriod = Objects.requireNonNull(controlLoopPeriod, "controlLoopPeriod cannot be null");
        this.policyCooldown = Objects.requireNonNull(policyCooldown, "policyCooldown cannot be null");
    }

    public int getWorkerCount() {
        return workerCount;
    }
    public PolicyType getInitialPolicy() {
        return initialPolicy;
    }
    public boolean isMlEnabled() {
        return mlEnabled;
    }
    public Duration getControlLoopPeriod() {
        return controlLoopPeriod;
    }
    public Duration getPolicyCooldown() {
        return policyCooldown;
    }

}