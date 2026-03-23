package io.aresched.policy.steal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import io.aresched.core.TaskRecord;
import io.aresched.policy.steal.WorkDeque;

public class WorkStealingPolicy {
    private final List<WorkDeque> deques;
    private final int workerCount;
    private final AtomicInteger nextWorkerIndex;

    public WorkStealingPolicy(int workerCount){
        this.workerCount = workerCount;
        if (workerCount > 0) {
            this.nextWorkerIndex = new AtomicInteger(0);
            this.deques = new ArrayList<>(workerCount);
            for (int i = 0; i < workerCount; i++){
                new ArrayList<>(i);
            }
        }
    }

    public void enqueue(TaskRecord<?> task){
        Objects.requireNonNull(task, "Task cannot be null");
        int index =Math.floorMod(nextWorkerIndex.getAndIncrement(), workerCount);
        deques.get(index).pushBottom(task);

    }

    
}
