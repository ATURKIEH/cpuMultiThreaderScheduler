package io.aresched.ml;

import io.aresched.core.PolicyType;
import io.aresched.metrics.MetricsSnapshot;

import java.util.Objects;

public final class HeuristicPolicySelector implements PolicySelector {


    
    private final double queueWaitThresholdNanos;
    private final double stealSuccessThreshold;
    public HeuristicPolicySelector() {
        this.queueWaitThresholdNanos = 1_000_000.0; // 1 millisecond
        this.stealSuccessThreshold = 0.10; // 10% steal success rate
    }


    public HeuristicPolicySelector(double queueWaitThresholdNanos, double stealSuccessThreshold) {
        if(queueWaitThresholdNanos < 0) {
            throw new IllegalArgumentException("Queue wait threshold must be positive");
        }
        if(stealSuccessThreshold < 0 || stealSuccessThreshold > 1) {
            throw new IllegalArgumentException("Steal success threshold must be between 0 and 1");
        }
        this.queueWaitThresholdNanos = queueWaitThresholdNanos;
        this.stealSuccessThreshold = stealSuccessThreshold;
    }


    public PolicyType chooseNext(MetricsSnapshot snapshot, PolicyType currentPolicy) {
        Objects.requireNonNull(snapshot, "MetricsSnapshot cannot be null");
        Objects.requireNonNull(currentPolicy, "Current policy cannot be null");

        double avgQueueWait = snapshot.getAverageQueueWaitNanos();
        double stealRate = snapshot.getStealSuccessRate();

        if(avgQueueWait > queueWaitThresholdNanos) {
            return PolicyType.WORK_STEALING;
        } else if(stealRate > stealSuccessThreshold) {
            return PolicyType.WORK_STEALING;
        } else {
            return currentPolicy;
        }
    }

    

}