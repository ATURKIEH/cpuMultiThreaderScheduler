package io.aresched.ml;

import io.aresched.core.TaskMetadata;
import io.aresched.core.TaskHint;
import java.util.Objects;

public final class HeuristicRuntimePredictor implements RuntimePredictor {

    private final long shortThreshold;
    private final long mediumThreshold;
    private final boolean ioBoundBiasEnabled;

    public HeuristicRuntimePredictor() {
        this.shortThreshold = 500;
        this.mediumThreshold = 5000;
        this.ioBoundBiasEnabled = true;
    }

    public HeuristicRuntimePredictor(long shortThreshold, long mediumThreshold, boolean ioBoundBiasEnabled) {
        if(shortThreshold < 0) {
            throw new IllegalArgumentException("Thresholds must be positive");
        }
        if(mediumThreshold < shortThreshold) {
            throw new IllegalArgumentException("Medium threshold must be greater than short threshold");
        }
        this.shortThreshold = shortThreshold;
        this.mediumThreshold = mediumThreshold;
        this.ioBoundBiasEnabled = ioBoundBiasEnabled;
    }


    @Override
    public RuntimeBucket predict(TaskMetadata metadata) {
        Objects.requireNonNull(metadata, "TaskMetadata cannot be null");
        long inputsize = metadata.getInputSize();
        TaskHint hint = metadata.getHint();

        RuntimeBucket baseBucket = predictFromSize(inputsize);
        return applyIoBoundBias(baseBucket, hint);
    }

    private RuntimeBucket predictFromSize(long inputSize) {
        if(inputSize < shortThreshold) {
            return RuntimeBucket.SHORT;
        } else if(inputSize < mediumThreshold) {
            return RuntimeBucket.MEDIUM;
        } else {
            return RuntimeBucket.LONG;
        }
    }

    private RuntimeBucket applyIoBoundBias(RuntimeBucket bucket, TaskHint hint) {
        if(!ioBoundBiasEnabled){
            return bucket;
        }
        if(hint != TaskHint.IO_BOUND) {
            return bucket;
        }
        if(bucket == RuntimeBucket.SHORT) {
            return RuntimeBucket.MEDIUM;
        } else if(bucket == RuntimeBucket.MEDIUM) {
            return RuntimeBucket.LONG;
        } else {
            return RuntimeBucket.LONG;
        }
    }


    
}
