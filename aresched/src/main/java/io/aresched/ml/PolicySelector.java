package io.aresched.ml;

import io.aresched.core.PolicyType;
import io.aresched.metrics.MetricsSnapshot;

public interface PolicySelector {
    PolicyType chooseNext(MetricsSnapshot snapshot, PolicyType currentPolicy);
}