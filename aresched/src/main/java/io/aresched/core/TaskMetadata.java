package io.aresched.core;

import java.util.Objects;

public class TaskMetadata {
    private final int priority;
    private final String taskType;
    private final long inputSize;
    private final TaskHint hint;

    public TaskMetadata(int priority, String taskType, long inputSize, TaskHint hint) {
        this.priority = priority;
        this.taskType = Objects.requireNonNullElse(taskType, "default");
        this.inputSize = inputSize;
        this.hint = Objects.requireNonNullElse(hint, TaskHint.UNKNOWN);
    }

    public int getPriority() {
        return priority;
    }
    public String getTaskType() {
        return taskType;
    }
    public long getInputSize() {
        return inputSize;
    }
    public TaskHint getHint() {
        return hint;
    }

    @Override
    public String toString() {
        return "TaskMetadata{" +
                "priority=" + priority +
                ", taskType='" + taskType + '\'' +
                ", inputSize=" + inputSize +
                ", hint=" + hint +
                '}';
    }

}
