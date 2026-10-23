package algorithms;

import graph.Edge;
import graph.Graph;
import metrics.Metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Prim {

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
        int n = graph.getN();
        boolean[] visited = new boolean[n];

        List<Edge> tree = new ArrayList<>();

        PriorityQueue<Edge> queue = new PriorityQueue<>((a, b) -> {
            metrics.comparisons++;
            return Integer.compare(a.weight, b.weight);
        });

        long totalWeight = 0;
        int components = 0;

        for (int start = 0; start < n; start++) {
            if (visited[start]) {
                continue;
            }

            components++;
            queue.add(new Edge(-1, start, 0));

            while (!queue.isEmpty()) {
                Edge current = queue.poll();

                if (visited[current.to]) {
                    continue;
                }

                visited[current.to] = true;

                if (current.from != -1) {
                    tree.add(current);
                    totalWeight += current.weight;
                }

                for (Edge edge : graph.getAdj(current.to)) {
                    metrics.edgeScans++;

                    if (!visited[edge.to]) {
                        queue.add(edge);
                    }
                }
            }
        }

        return new Result(totalWeight, tree, components);
    }
}