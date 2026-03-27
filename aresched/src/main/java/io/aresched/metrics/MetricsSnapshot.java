package io.aresched.metrics;
import java.util.Map;
import java.util.HashMap;
public final class MetricsSnapshot {
    private final long submitted;
    private final long rejected;
    private final long started;
    private final long completed;
    private final long failed;

    private final long totalQueueWaitNanos;
    private final long totalRunNanos;
    private final long totalEndToEndNanos;

    private final Map<Integer, Long> workerCompletedCounts;

    private final Long stealAttempts;
    private final Long stealSuccesses;

    public MetricsSnapshot(long submitted, long rejected, long started, long completed, long failed, long totalQueueWaitNanos, long totalRunNanos, long totalEndToEndNanos, Map<Integer, Long> workerCompletedCounts, Long stealAttempts, Long stealSuccesses) {
        this.submitted = submitted;
        this.rejected = rejected;
        this.started = started;
        this.completed = completed;
        this.failed = failed;
        this.totalQueueWaitNanos = totalQueueWaitNanos;
        this.totalRunNanos = totalRunNanos;
        this.totalEndToEndNanos = totalEndToEndNanos;
        this.workerCompletedCounts = workerCompletedCounts;
        this.stealAttempts = stealAttempts;
        this.stealSuccesses = stealSuccesses;
    }

    public long getSubmitted() {
        return submitted;
    }

    public long getRejected() {
        return rejected;
    }

    public long getComplete() {
        return completed;
    }

    public long getFailed() {
        return failed;
    }

    public long getStarted(){
        return started;
    }

    public long getQueueWait(){
        return totalQueueWaitNanos;
    }

    public long getRunNanos(){
        return totalRunNanos;
    }

    public long getEndToEnd(){
        return totalEndToEndNanos;
    }

    public Map<Integer, Long> getWorkerCompletedCounts(){
        return workerCompletedCounts;
    }

    public long getStealAttempts(){
        return stealAttempts;
    }

    public long getStealSuccesses(){
        return stealSuccesses;
    }

    public double getAverageQueueWaitNanos(){
        if (completed == 0){
            return 0.0;
        }
        return (double) totalQueueWaitNanos / completed;

    }

    public double getAverageRunNanos(){
        if (completed == 0){
            return 0.0;
        }
        return (double) totalRunNanos / completed;
    }

    public double getAverageEndToEndNanos(){
        if (completed == 0){
            return 0.0;
        }
        return (double) totalEndToEndNanos / completed;
    }

    public double getStealSuccessRate(){
        if (stealAttempts == 0){
            return 0.0;
        }
        return (double) stealSuccesses / stealAttempts;
    }


    @Override
    public String toString() {
        return "MetricsSnapshot{" +
                "submitted=" + submitted +
                ", rejected=" + rejected +
                ", started=" + started +
                ", completed=" + completed +
                ", failed=" + failed +
                ", total Queue Wait=" + totalQueueWaitNanos+
                ", total Run Nanos=" + totalRunNanos +
                ", total End To End=" + totalEndToEndNanos +
                ", steal attempts =" + stealAttempts +
                ", steal Successes=" + stealSuccesses +
                ", Avg Queue wait=" + getAverageQueueWaitNanos() +
                ", Avg Run Nanos=" + getAverageRunNanos() +
                ", Avg End To End=" + getAverageEndToEndNanos() +
                ", Steal Success rate=" + getStealSuccessRate()+
                '}';
    }
}