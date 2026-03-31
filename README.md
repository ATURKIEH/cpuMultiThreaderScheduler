
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)

AresScheduler — Adaptive Multithreaded Task Scheduler
=====================================================

A high-performance multithreaded task scheduler built in Java, supporting multiple scheduling strategies with heuristic-based ML logic for runtime prediction and adaptive policy selection.

* * *

Features
--------

### Scheduling Policies

*   **FIFO** — simple first-in, first-out ordering
*   **Priority-based** — weighted task ordering by priority level
*   **Work-stealing** — per-worker deque model for dynamic load balancing

### Concurrent Execution Engine

*   Custom worker thread pool
*   Lock-based work deques with safe task lifecycle management

### Metrics Collection

*   Queue wait time, execution time, and end-to-end latency
*   Per-worker completion statistics
*   Work-stealing success rate

### ML-Inspired Runtime Prediction

Classifies tasks into `SHORT`, `MEDIUM`, or `LONG` buckets using:

*   Input size
*   Task hint (`CPU` vs `IO`)
*   IO bias adjustment factor

### Adaptive Policy Recommendation

Analyzes live runtime metrics to recommend the optimal scheduling policy by detecting queue congestion and worker imbalance, then suggesting a policy switch when beneficial.

* * *

System Architecture
-------------------

    ┌─────────────────────┐
    │   Task Submission   │
    └──────────┬──────────┘
               │
               ▼
    ┌─────────────────────┐
    │  Runtime Predictor  │  ← classifies task duration
    └──────────┬──────────┘
               │
               ▼
    ┌─────────────────────┐
    │  Scheduling Policy  │  ← FIFO / Priority / Work-Stealing
    └──────────┬──────────┘
               │
               ▼
    ┌─────────────────────┐
    │   Worker Threads    │
    └──────────┬──────────┘
               │
               ▼
    ┌─────────────────────┐
    │  Metrics Collector  │
    └──────────┬──────────┘
               │
               ▼
    ┌─────────────────────┐
    │  Policy Selector    │  ← ML-driven recommendation
    └─────────────────────┘

* * *

Work-Stealing Model
-------------------

    Worker 0 deque: [T1, T2, T3]
    Worker 1 deque: []
    Worker 2 deque: []
    
    → Worker 1 steals T1 from Worker 0 (top)
    → Worker 0 continues with T2, T3 (bottom)

*   **Owner thread** → `popBottom()`
*   **Thief thread** → `stealTop()`

* * *

Benchmark Results
-----------------

| Policy | Latency | Queue Wait | Notes |
|--------|---------|------------|-------|
| FIFO | High | High | Bottlenecks under heavy load |
| Priority | Medium | Medium | Improved ordering |
| Work-Stealing | Lowest | Lowest | Best overall load distribution |

* * *

Example Output
--------------

    [LOW PRESSURE]
    Recommended policy: FIFO
    
    [HIGH PRESSURE]
    Recommended policy: WORK_STEALING

* * *

How to Run
----------

bash

    mvn compile
    java -cp target/classes io.aresched.TestPhase7Adaptive

* * *

Tech Stack
----------

*   **Java** — core implementation
*   **Concurrency primitives** — threads, locks, atomics
*   **Custom scheduling algorithms** — FIFO, priority queue, work-stealing deque
*   **Heuristic ML logic** — rule-based runtime classification and policy recommendation

* * *

Key Insight
-----------

Rather than committing to a static scheduling strategy upfront, AresScheduler continuously profiles runtime behavior — queue depth, worker utilization, steal rates — and dynamically recommends the most efficient policy for the current workload. The result is a scheduler that self-tunes under pressure without any external configuration.

Performance Comparison
-----------

*  FIFO: ~100 ms
*  Priority: ~8 ms
*  Work-Stealing: ~9 ms
