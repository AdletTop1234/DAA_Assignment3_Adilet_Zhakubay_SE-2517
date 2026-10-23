package benchmark;

import graph.Graph;

import java.util.Random;

public class GraphGenerator {

    private static int[] shuffle(int n, Random rnd) {
        int[] array = new int[n];

        for (int i = 0; i < n; i++) {
            array[i] = i;
        }

        for (int i = n - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }

        return array;
    }

    private static void addRandomEdges(
            Graph graph, int n, int m, Random rnd,
            int[] rank, boolean dag) {

        while (graph.getEdges().size() < m) {
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);

            while (b == a) {
                b = rnd.nextInt(n);
            }

            if (dag && rank[a] > rank[b]) {
                int temp = a;
                a = b;
                b = temp;
            }

            int weight = 1 + rnd.nextInt(1000);
            graph.addEdge(a, b, weight);
        }
    }

    public static Graph undirected(int n, int m) {
        Random rnd = new Random(42);
        Graph graph = new Graph(n, false);
        int[] perm = shuffle(n, rnd);

        for (int i = 1; i < n; i++) {
            int u = perm[rnd.nextInt(i)];
            int v = perm[i];
            int weight = 1 + rnd.nextInt(1000);

            graph.addEdge(u, v, weight);
        }

        addRandomEdges(graph, n, m, rnd, null, false);
        return graph;
    }

    public static Graph directed(int n, int m) {
        Random rnd = new Random(42);
        Graph graph = new Graph(n, true);

        addRandomEdges(graph, n, m, rnd, null, false);
        return graph;
    }

    public static Graph dag(int n, int m) {
        Random rnd = new Random(42);
        Graph graph = new Graph(n, true);
        int[] rank = shuffle(n, rnd);

        addRandomEdges(graph, n, m, rnd, rank, true);
        return graph;
    }
}