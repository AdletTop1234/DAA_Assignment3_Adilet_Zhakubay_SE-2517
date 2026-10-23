import algorithms.Kosaraju;
import graph.Graph;
import metrics.Metrics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StackSafetyTest {

    @Test
    void pathOf100000Vertices() {
        int n = 100000;

        Graph graph = new Graph(n, true);

        for (int i = 0; i < n - 1; i++) {
            graph.addEdge(i, i + 1, 1);
        }

        Metrics metrics = new Metrics();

        assertEquals(n, Kosaraju.run(graph, metrics).size());
        assertEquals(2L * (n - 1), metrics.edgeScans);
        assertTrue(metrics.maxDepth > 0);
    }
}