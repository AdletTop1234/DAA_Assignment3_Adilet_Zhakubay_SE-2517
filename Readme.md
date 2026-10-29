# Assignment 3: City Network & Job Scheduler

## Overview

This Java project implements graph algorithms for minimum spanning forests, strongly connected components, topological sorting, and shortest paths in directed acyclic graphs.

## Requirements

- Java 21
- Maven
- JUnit 5

## Project structure

```text
src/
  main/java/
    graph/
      Edge.java
      Graph.java
      GraphReader.java
    algorithms/
      DisjointSetUnion.java
      Kruskal.java
      Prim.java
      Kosaraju.java
      Kahn.java
      DagShortestPath.java
    metrics/
      Metrics.java
    benchmark/
      GraphGenerator.java
      BenchmarkRunner.java
      CsvWriter.java
  test/java/
    DSUTest.java
    MSTTest.java
    SCCTest.java
    TopologicalSortTest.java
    DagShortestPathTest.java
    StackSafetyTest.java
data/
  connected.txt
  disconnected.txt
  cyclic.txt
results/
  results.csv
  plots/
REPORT.md
README.md
pom.xml
```

## Build and test

Run from the project root:

```bash
mvn clean test
```

## Run benchmarks

Run `benchmark.BenchmarkRunner` in IntelliJ IDEA or use:

```bash
mvn compile
java -cp target/classes benchmark.BenchmarkRunner
```

The benchmark generates graphs with `Random(42)`, uses vertex counts of 1,000, 3,162, 10,000, 31,623, and 100,000, and uses `m = 4n` and `m = 20n`. Each algorithm is measured five times, with the median elapsed time reported.

The resulting CSV is written to `results/results.csv`. It contains the columns:

```text
algorithm,graph,n,m,time_ms,dsu_ops,edge_scans,comparisons,max_depth,result
```

## Create plots

After generating `results/results.csv`, use the supplied `plot_results.py`:

```bash
python -m pip install matplotlib
python plot_results.py
```

This produces:

- `results/plots/operations.png`
- `results/plots/scans_per_edge.png`
- `results/plots/kruskal_edge_share.png`

## Input file format

The first line contains the number of vertices, the number of edges, and the graph type (`directed` or `undirected`).

Each subsequent line contains `from to weight`.

Example:

```text
4 3 undirected
0 1 5
1 2 2
2 3 8
```

Vertices are indexed from zero.

## Git branches

The planned branches are:

- `feature/dsu`
- `feature/mst`
- `feature/scc`
- `feature/paths`
- `feature/metrics`

The release tag `v1.0` should be created after the completed implementation has been merged and checked.

## Status

Benchmark results, plot images, and numerical conclusions must be verified against an actual program run before final submission.