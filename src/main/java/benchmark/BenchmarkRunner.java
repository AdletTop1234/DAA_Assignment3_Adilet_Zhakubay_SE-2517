package benchmark;

import algorithms.DagShortestPath;
import algorithms.Kahn;
import algorithms.Kosaraju;
import algorithms.Kruskal;
import algorithms.Prim;
import graph.Edge;
import graph.Graph;
import metrics.Metrics;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class BenchmarkRunner {

    private static final int[] SIZES = {
            1000, 3162, 10000, 31623, 100000
    };

    private static final long[] MST_EXPECTED = {
            147970, 30363,
            468911, 97589,
            1486139, 309550,
            4715515, 968517,
            15012094, 3056681
    };

    private static final long[] SCC_EXPECTED = {
            27, 1,
            137, 1,
            373, 1,
            1188, 1,
            3923, 1
    };

    private static final long[] DAG_EXPECTED = {
            396274, 252411,
            1271911, 723157,
            3941828, 2381342,
            12551891, 7376612,
            40096539, 23512087
    };

    private interface Operation {
        long run(Metrics metrics);
    }

    private record Measurement(
            long result,
            Metrics metrics,
            double timeMs
    ) {
    }

    private static Measurement measure(Operation operation) {
        long[] times = new long[5];
        Metrics savedMetrics = null;
        long savedResult = 0;

        for (int i = 0; i < 5; i++) {
            Metrics metrics = new Metrics();

            long start = System.nanoTime();
            long result = operation.run(metrics);
            times[i] = System.nanoTime() - start;

            if (i == 0) {
                savedResult = result;
                savedMetrics = metrics;
            } else {
                if (result != savedResult ||
                        metrics.dsuOps != savedMetrics.dsuOps ||
                        metrics.edgeScans != savedMetrics.edgeScans ||
                        metrics.comparisons != savedMetrics.comparisons ||
                        metrics.maxDepth != savedMetrics.maxDepth) {
                    throw new IllegalStateException(
                            "Inconsistent benchmark results"
                    );
                }
            }
        }

        Arrays.sort(times);

        return new Measurement(
                savedResult,
                savedMetrics,
                times[2] / 1_000_000.0
        );
    }

    private static void check(
            long actual, long expected, String name) {

        if (actual != expected) {
            throw new IllegalStateException(
                    name + ": expected " + expected +
                            ", got " + actual
            );
        }
    }

    private static Measurement[] runMst(int n, int m) {
        Graph graph = GraphGenerator.undirected(n, m);

        Measurement kruskal = measure(metrics -> {
            Kruskal.Result result = Kruskal.run(graph, metrics);

            if (result.components != 1 ||
                    result.edges.size() != n - 1) {
                throw new IllegalStateException(
                        "Invalid Kruskal tree"
                );
            }

            return result.totalWeight;
        });

        Measurement prim = measure(metrics -> {
            Prim.Result result = Prim.run(graph, metrics);

            if (result.components != 1 ||
                    result.edges.size() != n - 1) {
                throw new IllegalStateException(
                        "Invalid Prim tree"
                );
            }

            return result.totalWeight;
        });

        check(prim.result(), kruskal.result(), "MST agreement");

        return new Measurement[]{kruskal, prim};
    }

    private static Measurement runScc(int n, int m) {
        Graph graph = GraphGenerator.directed(n, m);

        Measurement result = measure(metrics ->
                Kosaraju.run(graph, metrics).size()
        );

        check(result.metrics().edgeScans, 2L * m,
                "Kosaraju edge scans");

        return result;
    }

    private static void validateOrder(
            Graph graph, List<Integer> order) {

        int n = graph.getN();

        if (order.size() != n) {
            throw new IllegalStateException(
                    "Invalid topological order size"
            );
        }

        int[] position = new int[n];

        for (int i = 0; i < n; i++) {
            position[order.get(i)] = i;
        }

        for (Edge edge : graph.getEdges()) {
            if (position[edge.from] >= position[edge.to]) {
                throw new IllegalStateException(
                        "Invalid topological order"
                );
            }
        }
    }

    private static Measurement[] runDag(int n, int m) {
        Graph graph = GraphGenerator.dag(n, m);

        Kahn.Result topological = Kahn.run(
                graph, new Metrics()
        );

        if (topological.hasCycle) {
            throw new IllegalStateException(
                    "Generated DAG contains a cycle"
            );
        }

        validateOrder(graph, topological.order);

        Measurement kahn = measure(metrics -> {
            Kahn.Result result = Kahn.run(graph, metrics);

            if (result.hasCycle) {
                throw new IllegalStateException(
                        "Cycle detected"
                );
            }

            return result.order.size();
        });

        Measurement shortestPath = measure(metrics ->
                DagShortestPath.run(graph, metrics).sumDistances
        );

        check(kahn.result(), n, "Kahn result");
        check(kahn.metrics().edgeScans, m, "Kahn edge scans");
        check(shortestPath.metrics().edgeScans, m,
                "DAG edge scans");

        return new Measurement[]{kahn, shortestPath};
    }

    private static void writeResult(
            CsvWriter writer,
            String algorithm,
            String graph,
            int n,
            int m,
            Measurement measurement) throws IOException {

        writer.write(
                algorithm,
                graph,
                n,
                m,
                measurement.timeMs(),
                measurement.metrics(),
                measurement.result()
        );
    }

    public static void main(String[] args) throws IOException {
        try (CsvWriter writer =
                     new CsvWriter("results/results.csv")) {

            int index = 0;

            System.out.println(
                    "n / m | MST weight | SCC count | DAG sum"
            );

            for (int n : SIZES) {
                for (int density : new int[]{4, 20}) {
                    int m = n * density;

                    String suffix = density == 4
                            ? "sparse" : "dense";

                    Measurement[] mst = runMst(n, m);
                    Measurement scc = runScc(n, m);
                    Measurement[] dag = runDag(n, m);

                    check(mst[0].result(),
                            MST_EXPECTED[index], "MST");
                    check(scc.result(),
                            SCC_EXPECTED[index], "SCC");
                    check(dag[1].result(),
                            DAG_EXPECTED[index], "DAG");

                    System.out.printf(
                            "%d / %d | %d | %d | %d%n",
                            n, m,
                            mst[0].result(),
                            scc.result(),
                            dag[1].result()
                    );

                    writeResult(writer, "Kruskal",
                            "undirected_" + suffix,
                            n, m, mst[0]);

                    writeResult(writer, "Prim",
                            "undirected_" + suffix,
                            n, m, mst[1]);

                    writeResult(writer, "Kosaraju",
                            "directed_" + suffix,
                            n, m, scc);

                    writeResult(writer, "Kahn",
                            "dag_" + suffix,
                            n, m, dag[0]);

                    writeResult(writer, "DagShortestPath",
                            "dag_" + suffix,
                            n, m, dag[1]);

                    index++;
                }
            }
        }
    }
}