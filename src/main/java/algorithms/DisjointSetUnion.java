package algorithms;

import metrics.Metrics;

public class DisjointSetUnion {
    private int[] parent;
    private int[] rank;
    private int count;
    private long findCalls;
    private long successfulUnions;
    private final Metrics metrics;

    public DisjointSetUnion(int n, Metrics metrics) {
        this.metrics = metrics;
        makeSet(n);
    }

    public void makeSet(int n) {
        parent = new int[n];
        rank = new int[n];
        count = n;
        findCalls = 0;
        successfulUnions = 0;

        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

    public int find(int x) {
        findCalls++;
        metrics.dsuOps++;

        int root = x;

        while (parent[root] != root) {
            root = parent[root];
        }

        while (parent[x] != x) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }

        return root;
    }

    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return false;
        }

        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }

        count--;
        successfulUnions++;
        metrics.dsuOps++;

        return true;
    }

    public int components() {
        return count;
    }

    public long getFindCalls() {
        return findCalls;
    }

    public long getSuccessfulUnions() {
        return successfulUnions;
    }
}