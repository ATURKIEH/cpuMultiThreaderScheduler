package io.aresched.metrics;

public final class MetricsSnapshot {
    private final long submitted;
    private final long rejected;
    private final long started;
    private final long completed;
    private final long failed;

    public MetricsSnapshot(long submitted, long rejected, long started, long completed, long failed) {
        this.submitted = submitted;
        this.rejected = rejected;
        this.started = started;
        this.completed = completed;
        this.failed = failed;
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



    @Override
    public String toString() {
        return "MetricsSnapshot{" +
                "submitted=" + submitted +
                ", rejected=" + rejected +
                ", started=" + started +
                ", completed=" + completed +
                ", failed=" + failed +
                '}';
    }
}