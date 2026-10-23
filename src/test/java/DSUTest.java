import algorithms.DisjointSetUnion;
import metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DSUTest {

    @Test
    void randomUnions() {
        Random random = new Random(42);

        for (int test = 0; test < 30; test++) {
            int n = 2 + random.nextInt(49);
            Metrics metrics = new Metrics();
            DisjointSetUnion dsu = new DisjointSetUnion(n, metrics);

            int[] labels = new int[n];

            for (int i = 0; i < n; i++) {
                labels[i] = i;
            }

            int successful = 0;

            for (int step = 0; step < 300; step++) {
                int a = random.nextInt(n);
                int b = random.nextInt(n);

                boolean merged = labels[a] != labels[b];

                assertEquals(merged, dsu.union(a, b));

                if (merged) {
                    int oldLabel = labels[b];
                    int newLabel = labels[a];

                    for (int i = 0; i < n; i++) {
                        if (labels[i] == oldLabel) {
                            labels[i] = newLabel;
                        }
                    }

                    successful++;
                }

                long components = Arrays.stream(labels)
                        .distinct()
                        .count();

                assertEquals(components, dsu.components());
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    assertEquals(
                            labels[i] == labels[j],
                            dsu.find(i) == dsu.find(j)
                    );
                }
            }

            assertEquals(successful, dsu.getSuccessfulUnions());
            assertEquals(
                    dsu.getFindCalls() + dsu.getSuccessfulUnions(),
                    metrics.dsuOps
            );

            dsu.makeSet(n);
            assertEquals(n, dsu.components());
        }
    }
}