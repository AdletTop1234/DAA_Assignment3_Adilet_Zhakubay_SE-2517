package algorithms;

import graph.Edge;
import graph.Graph;
import metrics.Metrics;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Kahn {

    public static class Result {
        public final List<Integer> order;
        public final boolean hasCycle;

        public Result(List<Integer> order, boolean hasCycle) {
            this.order = order;
            this.hasCycle = hasCycle;
        }
    }

    public static Result run(Graph graph, Metrics metrics) {
        int n = graph.getN();
        int[] indegree = new int[n];

        for (int u = 0; u < n; u++) {
            for (Edge edge : graph.getAdj(u)) {
                indegree[edge.to]++;
            }
        }

        ArrayDeque<Integer> queue = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                queue.add(i);
            }
        }

        List<Integer> order = new ArrayList<>();

        while (!queue.isEmpty()) {
            int u = queue.poll();
            order.add(u);

            for (Edge edge : graph.getAdj(u)) {
                metrics.edgeScans++;

                indegree[edge.to]--;

                if (indegree[edge.to] == 0) {
                    queue.add(edge.to);
                }
            }
        }

        if (order.size() != n) {
            return new Result(Collections.emptyList(), true);
        }

        return new Result(order, false);
    }
}