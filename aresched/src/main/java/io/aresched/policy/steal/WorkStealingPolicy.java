package io.aresched.policy.steal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import io.aresched.core.TaskRecord;
import io.aresched.metrics.MetricsCollector;
import io.aresched.policy.SchedulingPolicy;
import io.aresched.policy.steal.WorkDeque;

public class WorkStealingPolicy implements SchedulingPolicy {
    private final List<WorkDeque> deques;
    private final int workerCount;
    private final AtomicInteger nextWorkerIndex;
    private final MetricsCollector metricsCollector;

    public WorkStealingPolicy(int workerCount, MetricsCollector metricsCollector){
        if (workerCount <= 0) {
            throw new IllegalArgumentException("Worker count must be > 0");
        }
        this.workerCount = workerCount;
        this.nextWorkerIndex = new AtomicInteger(0);
        this.deques = new ArrayList<>(workerCount);
        this.metricsCollector = Objects.requireNonNull(metricsCollector, "metrics cant be null");
        for (int i = 0; i < workerCount; i++){
            deques.add(new WorkDeque());
        }
        
    }

    public void enqueue(TaskRecord<?> task){
        Objects.requireNonNull(task, "Task cannot be null");
        int index = Math.floorMod(nextWorkerIndex.getAndIncrement(), workerCount);
        deques.get(index).pushBottom(task);

    }
    
    public TaskRecord<?> take(int workerId) throws InterruptedException {
        while (true) {

            // 1. Try local work
            TaskRecord<?> local = deques.get(workerId).popBottom();
            if (local != null) {
                return local;
            }
            
            // 2. Try stealing
            for (int i = 0; i < deques.size(); i++) {
                if (i == workerId) continue;
                metricsCollector.recordStealAttempt();
                TaskRecord<?> stolen = deques.get(i).stealTop();
                if (stolen != null) {
                    metricsCollector.recordStealSuccess();
                    return stolen;
                }
            }

            // 3. allow shutdown exit
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            // 4. Backoff
            Thread.sleep(10);
        }
    }
    
    @Override
    public boolean isEmpty(){
        for (int i = 0; i < deques.size(); i++) {
            if (!deques.get(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }
    @Override
    public void signalAll(){

        // nothing
    }
    

    
}
