package io.aresched.ml;

import io.aresched.core.TaskMetadata;

public interface RuntimePredictor {
    RuntimeBucket predict(TaskMetadata metadata);
}
