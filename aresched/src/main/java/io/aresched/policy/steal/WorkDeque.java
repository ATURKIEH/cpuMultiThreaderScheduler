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
        
        Objects.requireNonNull(task, "Task cannot be null");
        lock.lock();
        try{
            deque.addLast(task);
        }
        finally{
            lock.unlock();
        }
        

    }

    public TaskRecord<?> popBottom(){
        lock.lock();
        try {
            if (deque.isEmpty()) {
                return null;
            }
            return deque.pollLast();
        } finally {
            lock.unlock();
        }
    }

    public TaskRecord<?> stealTop(){
        lock.lock();
        try {
            if (deque.isEmpty()) {
                return null;
            }
            return deque.pollFirst();
        } finally {
            lock.unlock();
        }
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
