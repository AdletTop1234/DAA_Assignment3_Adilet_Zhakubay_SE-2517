package graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GraphReader {

    public static Graph read(String filename) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new File(filename))) {
            int n = sc.nextInt();
            int m = sc.nextInt();
            String type = sc.next();

            boolean directed = type.equals("directed");
            Graph graph = new Graph(n, directed);

            for (int i = 0; i < m; i++) {
                int u = sc.nextInt();
                int v = sc.nextInt();
                int w = sc.nextInt();

                graph.addEdge(u, v, w);
            }

            return graph;
        }
    }
}
