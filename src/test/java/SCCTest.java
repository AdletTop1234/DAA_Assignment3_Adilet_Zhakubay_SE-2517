import algorithms.Kosaraju;
import graph.Edge;
import graph.Graph;
import metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class SCCTest {

    private boolean[] bfs(Graph graph, int start) {
        int n = graph.getN();
        boolean[] visited = new boolean[n];
        ArrayDeque<Integer> queue = new ArrayDeque<>();

        queue.add(start);
        visited[start] = true;

        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (Edge edge : graph.getAdj(u)) {
                if (!visited[edge.to]) {
                    visited[edge.to] = true;
                    queue.add(edge.to);
                }
            }
        }

        return visited;
    }

    @Test
    void compareWithBruteForce() {
        Random random = new Random(42);

        for (int test = 0; test < 40; test++) {
            int n = test == 0 ? 200 : 2 + random.nextInt(199);
            int m = random.nextInt(5 * n + 1);

            Graph graph = new Graph(n, true);
            Graph reversed = new Graph(n, true);

            for (int i = 0; i < m; i++) {
                int u = random.nextInt(n);
                int v = random.nextInt(n);
                int weight = 1 + random.nextInt(1000);

                graph.addEdge(u, v, weight);
                reversed.addEdge(v, u, weight);
            }

            Metrics metrics = new Metrics();

            List<List<Integer>> components =
                    Kosaraju.run(graph, metrics);

            int[] componentId = new int[n];
            Arrays.fill(componentId, -1);

            for (int i = 0; i < components.size(); i++) {
                for (int v : components.get(i)) {
                    assertEquals(-1, componentId[v]);
                    componentId[v] = i;
                }
            }

            boolean[][] forward = new boolean[n][];
            boolean[][] backward = new boolean[n][];

            for (int i = 0; i < n; i++) {
                forward[i] = bfs(graph, i);
                backward[i] = bfs(reversed, i);
            }

            for (int i = 0; i < n; i++) {
                assertNotEquals(-1, componentId[i]);

                for (int j = 0; j < n; j++) {
                    boolean expected =
                            forward[i][j] && backward[i][j];

                    boolean actual =
                            componentId[i] == componentId[j];

                    assertEquals(expected, actual);
                }
            }

            assertEquals(2L * m, metrics.edgeScans);
        }
    }
}