
package graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int n;
    private final boolean directed;
    private final List<List<Edge>> adj;
    private final List<Edge> edges;

    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        adj = new ArrayList<>();
        edges = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v, int weight) {
        Edge edge = new Edge(u, v, weight);
        edges.add(edge);
        adj.get(u).add(edge);

        if (!directed) {
            adj.get(v).add(new Edge(v, u, weight));
        }
    }

    public int getN() {
        return n;
    }

    public boolean isDirected() {
        return directed;
    }

    public List<Edge> getAdj(int v) {
        return adj.get(v);
    }

    public List<Edge> getEdges() {
        return edges;
    }
}
