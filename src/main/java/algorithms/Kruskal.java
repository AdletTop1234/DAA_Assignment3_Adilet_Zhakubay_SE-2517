package algorithms;

import graph.Edge;
import graph.Graph;
import metrics.Metrics;

import java.util.ArrayList;
import java.util.List;

public class Kruskal {

    public static class Result {
        public final long totalWeight;
        public final List<Edge> edges;
        public final int components;

        public Result(long totalWeight, List<Edge> edges, int components) {
            this.totalWeight = totalWeight;
            this.edges = edges;
            this.components = components;
        }
    }

    public static Result run(Graph graph, Metrics metrics) {
        List<Edge> edges = new ArrayList<>(graph.getEdges());

        edges.sort((a, b) -> {
            metrics.comparisons++;
            return Integer.compare(a.weight, b.weight);
        });

        DisjointSetUnion dsu =
                new DisjointSetUnion(graph.getN(), metrics);

        List<Edge> tree = new ArrayList<>();
        long totalWeight = 0;

        for (Edge edge : edges) {
            metrics.edgeScans++;

            if (dsu.find(edge.from) != dsu.find(edge.to)) {
                dsu.union(edge.from, edge.to);
                tree.add(edge);
                totalWeight += edge.weight;
            }
        }

        return new Result(totalWeight, tree, dsu.components());
    }
}