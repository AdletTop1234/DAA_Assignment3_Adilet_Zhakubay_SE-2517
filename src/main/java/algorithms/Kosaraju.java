package algorithms;

import graph.Edge;
import graph.Graph;
import metrics.Metrics;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Kosaraju {

    private static class Frame {
        int vertex;
        int index;

        Frame(int vertex) {
            this.vertex = vertex;
            this.index = 0;
        }
    }

    public static List<List<Integer>> run(Graph graph, Metrics metrics) {
        int n = graph.getN();

        boolean[] visited = new boolean[n];
        List<Integer> order = new ArrayList<>();

        for (int start = 0; start < n; start++) {
            if (visited[start]) {
                continue;
            }

            ArrayDeque<Frame> stack = new ArrayDeque<>();
            stack.push(new Frame(start));
            visited[start] = true;

            while (!stack.isEmpty()) {
                metrics.maxDepth = Math.max(metrics.maxDepth, stack.size());

                Frame current = stack.peek();
                List<Edge> neighbors = graph.getAdj(current.vertex);

                if (current.index < neighbors.size()) {
                    Edge edge = neighbors.get(current.index++);
                    metrics.edgeScans++;

                    if (!visited[edge.to]) {
                        visited[edge.to] = true;
                        stack.push(new Frame(edge.to));
                    }
                } else {
                    order.add(current.vertex);
                    stack.pop();
                }
            }
        }

        List<List<Integer>> reversed = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            reversed.add(new ArrayList<>());
        }

        for (int u = 0; u < n; u++) {
            for (Edge edge : graph.getAdj(u)) {
                reversed.get(edge.to).add(u);
            }
        }

        visited = new boolean[n];
        List<List<Integer>> components = new ArrayList<>();

        for (int i = order.size() - 1; i >= 0; i--) {
            int start = order.get(i);

            if (visited[start]) {
                continue;
            }

            List<Integer> component = new ArrayList<>();
            ArrayDeque<Integer> stack = new ArrayDeque<>();

            stack.push(start);
            visited[start] = true;

            while (!stack.isEmpty()) {
                metrics.maxDepth = Math.max(metrics.maxDepth, stack.size());

                int u = stack.pop();
                component.add(u);

                for (int v : reversed.get(u)) {
                    metrics.edgeScans++;

                    if (!visited[v]) {
                        visited[v] = true;
                        stack.push(v);
                    }
                }
            }

            components.add(component);
        }

        return components;
    }
}