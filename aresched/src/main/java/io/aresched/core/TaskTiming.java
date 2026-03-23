package io.aresched.core;
public class TaskTiming {
    private long submitNanos;
    private long startNanos;
    private long finishNanos;

    public synchronized void markSubmitted(long nanos) {
        if (submitNanos != 0L) {
            throw new IllegalStateException("Submit Time already set");
        
        }
        submitNanos = nanos;
    }
    public synchronized void markStarted(long nanos) {
        if (startNanos != 0L) {
            throw new IllegalStateException("Start Time already set");
        }
        startNanos = nanos;
    }
    public synchronized void markFinished(long nanos) {
        if (finishNanos != 0L) {
            throw new IllegalStateException("Finish Time already set");
        }
        finishNanos = nanos;
    }
    public synchronized long getSubmitNanos() {
        return submitNanos;
    }
    public synchronized long getStartNanos() {
        return startNanos;
    }
    public synchronized long getFinishNanos() {
        return finishNanos;
    }

    public synchronized long getQueueWaitNanos() {
        if (submitNanos == 0L || startNanos == 0L) {
            return -1L;
        }
        return startNanos - submitNanos;
    }
    public synchronized long getRunTimeNanos() {
        if (startNanos == 0L || finishNanos == 0L) {
            return -1L;
        }
        return finishNanos - startNanos;
    }
    public synchronized long getEndToEndNanos() {
        if (submitNanos == 0L || finishNanos == 0L) {
            return -1L;
        }
        return finishNanos - submitNanos;
    }

    @Override
    public synchronized String toString() {
       return "TaskTiming{" +
                "submitNanos=" + submitNanos +
                ", startNanos=" + startNanos +
                ", finishNanos=" + finishNanos +
                '}';
    }
}
