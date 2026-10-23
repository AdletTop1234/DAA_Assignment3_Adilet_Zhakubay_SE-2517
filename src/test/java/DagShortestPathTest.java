import algorithms.DagShortestPath;
import graph.Edge;
import graph.Graph;
import metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DagShortestPathTest {

    private long[] bellmanFord(Graph graph) {
        int n = graph.getN();

        long[] distance = new long[n];
        int[] indegree = new int[n];

        Arrays.fill(distance, Long.MAX_VALUE);

        for (Edge edge : graph.getEdges()) {
            indegree[edge.to]++;
        }

        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                distance[i] = 0;
            }
        }

        for (int i = 0; i < n - 1; i++) {
            for (Edge edge : graph.getEdges()) {
                if (distance[edge.from] == Long.MAX_VALUE) {
                    continue;
                }

                long newDistance =
                        distance[edge.from] + edge.weight;

                if (newDistance < distance[edge.to]) {
                    distance[edge.to] = newDistance;
                }
            }
        }

        return distance;
    }

    @Test
    void compareWithBellmanFord() {
        Random random = new Random(42);

        for (int test = 0; test < 100; test++) {
            int n = 2 + random.nextInt(49);
            Graph graph = new Graph(n, true);

            for (int u = 0; u < n; u++) {
                for (int v = u + 1; v < n; v++) {
                    if (random.nextDouble() < 0.15) {
                        int weight = random.nextInt(41) - 20;
                        graph.addEdge(u, v, weight);
                    }
                }
            }

            graph.addEdge(0, n - 1, -5);

            Metrics metrics = new Metrics();

            DagShortestPath.Result result =
                    DagShortestPath.run(graph, metrics);

            long[] expected = bellmanFord(graph);

            assertArrayEquals(expected, result.distance);
            assertEquals(
                    Arrays.stream(expected).sum(),
                    result.sumDistances
            );

            assertEquals(
                    graph.getEdges().size(),
                    metrics.edgeScans
            );

            int[] indegree = new int[n];

            for (Edge edge : graph.getEdges()) {
                indegree[edge.to]++;
            }

            for (int v = 0; v < n; v++) {
                List<Integer> path = result.getPath(v);

                assertFalse(path.isEmpty());
                assertEquals(v, path.get(path.size() - 1));
                assertEquals(0, indegree[path.get(0)]);

                for (int i = 1; i < path.size(); i++) {
                    int u = path.get(i - 1);
                    int w = path.get(i);

                    boolean valid = false;

                    for (Edge edge : graph.getAdj(u)) {
                        if (edge.to == w &&
                                result.distance[u] + edge.weight
                                        == result.distance[w]) {
                            valid = true;
                            break;
                        }
                    }

                    assertTrue(valid);
                }
            }
        }
    }
}