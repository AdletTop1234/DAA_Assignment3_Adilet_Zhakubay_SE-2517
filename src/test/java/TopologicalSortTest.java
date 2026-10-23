import algorithms.Kahn;
import graph.Edge;
import graph.Graph;
import metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class TopologicalSortTest {

    @Test
    void validTopologicalOrder() {
        Random random = new Random(42);
        int n = 100;

        Graph graph = new Graph(n, true);

        for (int u = 0; u < n; u++) {
            for (int v = u + 1; v < n; v++) {
                if (random.nextDouble() < 0.1) {
                    graph.addEdge(u, v, 1);
                }
            }
        }

        Metrics metrics = new Metrics();
        Kahn.Result result = Kahn.run(graph, metrics);

        assertFalse(result.hasCycle);
        assertEquals(n, result.order.size());

        List<Integer> order = result.order;
        int[] position = new int[n];
        boolean[] seen = new boolean[n];

        for (int i = 0; i < n; i++) {
            int v = order.get(i);

            assertFalse(seen[v]);
            seen[v] = true;
            position[v] = i;
        }

        for (Edge edge : graph.getEdges()) {
            assertTrue(position[edge.from] < position[edge.to]);
        }

        assertEquals(graph.getEdges().size(), metrics.edgeScans);
    }

    @Test
    void cycleDetection() {
        Graph graph = new Graph(4, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        Kahn.Result result = Kahn.run(graph, new Metrics());

        assertTrue(result.hasCycle);
        assertTrue(result.order.isEmpty());
    }
}