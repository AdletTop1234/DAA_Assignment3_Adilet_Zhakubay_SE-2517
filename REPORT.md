# Assignment 3 Report: City Network & Job Scheduler

**Student:** Adilet Zhakubay  
**Group:** SE-2517

## 1. Introduction

The project implements and evaluates five graph algorithms and a disjoint-set union (DSU) data structure. The main tasks are minimum spanning forest construction, strongly connected component detection, topological sorting, and shortest paths in directed acyclic graphs.

The implementation uses Java, Maven, and JUnit 5. Graphs are stored using adjacency lists.

## 2. Algorithms and complexity

Let `n` be the number of vertices and `m` the number of edges.

| Algorithm | Time complexity | Extra space | Main purpose |
|---|---|---|---|
| DSU (union by rank and path compression) | Amortized O(О±(n)) per operation | O(n) | Maintain components |
| Kruskal | O(m log m + m О±(n)) | O(n + m) | Minimum spanning forest |
| Prim (binary heap) | O(m log m + n log n) | O(n + m) | Minimum spanning forest |
| Kosaraju | O(n + m) | O(n + m) | Strongly connected components |
| Kahn | O(n + m) | O(n) | Topological sorting and cycle detection |
| DAG shortest paths | O(n + m) | O(n) | Shortest distances in a DAG |

`О±(n)` is the inverse Ackermann function.

## 3. Implementation

### 3.1 Graph representation

`Graph.java` stores adjacency lists and the input edge list. For undirected graphs, both directions are added to the adjacency lists while the input edge list stores one copy of each input edge. `GraphReader.java` reads weighted edges from text files.

### 3.2 Disjoint-set union

`DisjointSetUnion.java` uses path compression and union by rank. It tracks the number of connected components, calls to `find`, and successful unions.

### 3.3 Minimum spanning forest

Kruskal sorts edges by weight and uses DSU to avoid cycles. Prim uses a priority queue and expands a forest from every unvisited starting vertex. Both should return the same total weight on the same undirected weighted graph.

### 3.4 Strongly connected components

Kosaraju performs two DFS passes. The first records finishing order; the second explores the reversed graph. Iterative stacks avoid recursive call-stack overflow on long paths.

### 3.5 Topological ordering

Kahn calculates indegrees and repeatedly removes zero-indegree vertices. If fewer than `n` vertices are processed, the graph contains a directed cycle.

### 3.6 Shortest paths in a DAG

Shortest paths are calculated by relaxing edges in topological order. The current implementation initializes zero-indegree vertices with distance zero, so its result represents shortest distances from any source vertex. Negative edge weights are supported in a DAG.

## 4. Benchmark methodology

The benchmark uses the random seed `42` and generates graphs at five sizes: `1,000`, `3,162`, `10,000`, `31,623`, and `100,000` vertices.

Two edge counts are tested at each size: `m = 4n` (sparse) and `m = 20n` (dense). Undirected connected graphs are used for Kruskal and Prim; directed graphs for Kosaraju; DAGs for Kahn and DAG shortest paths.

Each algorithm is executed five times. The CSV reports the median elapsed time in milliseconds together with operation counts. The planned output is 50 data rows: 5 algorithms Г— 5 sizes Г— 2 densities.

### Measured counters

| Counter | Meaning in the current code |
|---|---|
| `dsu_ops` | DSU `find` calls plus successful union operations |
| `edge_scans` | Edge iterations explicitly counted by each algorithm |
| `comparisons` | Comparisons performed by the measured sorting / priority queue comparator |
| `max_depth` | Maximum explicit stack size observed in Kosaraju |

The operation-count plot uses `dsu_ops + edge_scans + comparisons`. This is a project-defined instrumentation total, not a hardware-level instruction count.

## 5. Validation

JUnit 5 tests check DSU connectivity against a simple reference grouping, compare Kruskal and Prim forest weights, validate SCC membership against reachability, verify topological order and cycle detection, and compare DAG shortest distances with BellmanвЂ“Ford on small DAGs.

A 100,000-vertex directed path is used to check that Kosaraju's iterative implementation avoids recursion-related stack overflow.

## 6. Results

**Important:** Numerical performance results must be filled from the actual `results/results.csv` file. No benchmark measurements are included here because the program's CSV output has not been supplied.

### 6.1 Results table

| n | m | Kruskal (ms) | Prim (ms) | Kosaraju (ms) | Kahn (ms) | DAG shortest path (ms) |
|---:|---:|---:|---:|---:|---:|---:|
| 1,000 | 4,000 | TBD | TBD | TBD | TBD | TBD |
| 1,000 | 20,000 | TBD | TBD | TBD | TBD | TBD |
| 3,162 | 12,648 | TBD | TBD | TBD | TBD | TBD |
| 3,162 | 63,240 | TBD | TBD | TBD | TBD | TBD |
| 10,000 | 40,000 | TBD | TBD | TBD | TBD | TBD |
| 10,000 | 200,000 | TBD | TBD | TBD | TBD | TBD |
| 31,623 | 126,492 | TBD | TBD | TBD | TBD | TBD |
| 31,623 | 632,460 | TBD | TBD | TBD | TBD | TBD |
| 100,000 | 400,000 | TBD | TBD | TBD | TBD | TBD |
| 100,000 | 2,000,000 | TBD | TBD | TBD | TBD | TBD |


## 7. Discussion

Kruskal's edge sorting contributes the dominant `O(m log m)` term. Prim's use of a priority queue results in logarithmic heap operations as edges are inserted and removed. Kosaraju, Kahn, and DAG shortest paths have linear theoretical bounds in vertices and edges.

The final discussion should compare the observed timings and counters with these bounds and identify any deviations due to implementation overhead, JVM warm-up, or graph density.

## 7. Conclusion

The project provides implementations of DSU, two minimum spanning forest algorithms, SCC detection, topological sorting, and DAG shortest paths. The tests and benchmark framework are designed to check correctness and compare operation counts across graph sizes and densities.
