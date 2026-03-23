package io.aresched.policy.steal;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

import io.aresched.core.TaskRecord;

public class WorkDeque {
    private final Deque<TaskRecord<?>> deque;
    private final ReentrantLock lock;

    public WorkDeque(){
    
        this.deque = new ArrayDeque<>();
        this.lock = new ReentrantLock();
        
    }

    public void pushBottom(TaskRecord<?> task){
        try{
            Objects.requireNonNull(task, "Task cannot be null");
            lock.lock();
            deque.addLast(task);
        }
        finally{
            lock.unlock();
        }
        

    }

    public TaskRecord<?> popBottom(){
        try{
            lock.lock();
            if(isEmpty()){
                return null;
            }
            deque.pollLast();

        }
        finally{
            lock.unlock();
        }
        return null;
    }

    public TaskRecord<?> stealTop(){
        try{
            lock.lock();
            if (isEmpty()){
                return null;
            }
            deque.pollFirst();

        }
        finally{
            lock.unlock();
        }
        return null;
    }

    public boolean isEmpty(){
        try{
            lock.lock();
            return deque.isEmpty();
        }
        finally{
            lock.unlock();
        }

    }

    public int size(){
        try{
            lock.lock();
            return deque.size();
        }
        finally{
            lock.unlock();
        }
    }
    
}
