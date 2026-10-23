package algorithms;

import graph.Edge;
import graph.Graph;
import metrics.Metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DagShortestPath {

    public static class Result {
        public final long[] distance;
        public final int[] parent;
        public final long sumDistances;

        public Result(long[] distance, int[] parent) {
            this.distance = distance;
            this.parent = parent;

            long sum = 0;

            for (long d : distance) {
                if (d != Long.MAX_VALUE) {
                    sum += d;
                }
            }

            this.sumDistances = sum;
        }

        public List<Integer> getPath(int vertex) {
            List<Integer> path = new ArrayList<>();

            if (distance[vertex] == Long.MAX_VALUE) {
                return path;
            }

            int current = vertex;

            while (current != -1) {
                path.add(current);
                current = parent[current];
            }

            Collections.reverse(path);
            return path;
        }
    }

    public static Result run(Graph graph, Metrics metrics) {
        Kahn.Result topological = Kahn.run(graph, new Metrics());

        if (topological.hasCycle) {
            throw new IllegalArgumentException("Graph has a cycle");
        }

        int n = graph.getN();
        int[] indegree = new int[n];

        for (int u = 0; u < n; u++) {
            for (Edge edge : graph.getAdj(u)) {
                indegree[edge.to]++;
            }
        }

        long[] distance = new long[n];
        int[] parent = new int[n];

        Arrays.fill(distance, Long.MAX_VALUE);
        Arrays.fill(parent, -1);

        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                distance[i] = 0;
            }
        }

        for (int u : topological.order) {
            for (Edge edge : graph.getAdj(u)) {
                metrics.edgeScans++;

                if (distance[u] == Long.MAX_VALUE) {
                    continue;
                }

                long newDistance = distance[u] + edge.weight;

                if (newDistance < distance[edge.to]) {
                    distance[edge.to] = newDistance;
                    parent[edge.to] = u;
                }
            }
        }

        return new Result(distance, parent);
    }
}