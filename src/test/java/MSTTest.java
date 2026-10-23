import algorithms.Kruskal;
import algorithms.Prim;
import graph.Graph;
import metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MSTTest {

    @Test
    void randomConnectedGraphs() {
        Random random = new Random(42);

        for (int test = 0; test < 100; test++) {
            int n = 2 + random.nextInt(49);
            Graph graph = new Graph(n, false);

            for (int i = 1; i < n; i++) {
                int parent = random.nextInt(i);
                int weight = 1 + random.nextInt(1000);
                graph.addEdge(parent, i, weight);
            }

            for (int i = 0; i < n * 4; i++) {
                int u = random.nextInt(n);
                int v = random.nextInt(n);
                int weight = 1 + random.nextInt(1000);

                graph.addEdge(u, v, weight);
            }

            Kruskal.Result kruskal =
                    Kruskal.run(graph, new Metrics());

            Prim.Result prim =
                    Prim.run(graph, new Metrics());

            assertEquals(kruskal.totalWeight, prim.totalWeight);
            assertEquals(n - 1, kruskal.edges.size());
            assertEquals(n - 1, prim.edges.size());
            assertEquals(1, kruskal.components);
            assertEquals(1, prim.components);
        }
    }

    @Test
    void disconnectedGraph() {
        Graph graph = new Graph(6, false);

        graph.addEdge(0, 1, 4);
        graph.addEdge(1, 2, 2);
        graph.addEdge(3, 4, 5);

        Kruskal.Result kruskal =
                Kruskal.run(graph, new Metrics());

        Prim.Result prim =
                Prim.run(graph, new Metrics());

        assertEquals(11, kruskal.totalWeight);
        assertEquals(kruskal.totalWeight, prim.totalWeight);
        assertEquals(3, kruskal.components);
        assertEquals(3, prim.components);
        assertEquals(3, kruskal.edges.size());
        assertEquals(3, prim.edges.size());
    }
}