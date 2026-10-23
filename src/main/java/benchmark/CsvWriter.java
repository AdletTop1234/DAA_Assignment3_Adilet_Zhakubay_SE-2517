package benchmark;

import metrics.Metrics;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class CsvWriter implements AutoCloseable {

    private final BufferedWriter writer;

    public CsvWriter(String filename) throws IOException {
        Path path = Path.of(filename);
        Files.createDirectories(path.getParent());

        writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
        writer.write("algorithm,graph,n,m,time_ms,dsu_ops,edge_scans,comparisons,max_depth,result");
        writer.newLine();
    }

    public void write(
            String algorithm,
            String graph,
            int n,
            int m,
            double timeMs,
            Metrics metrics,
            long result) throws IOException {

        writer.write(String.format(
                Locale.US,
                "%s,%s,%d,%d,%.3f,%d,%d,%d,%d,%d",
                algorithm,
                graph,
                n,
                m,
                timeMs,
                metrics.dsuOps,
                metrics.edgeScans,
                metrics.comparisons,
                metrics.maxDepth,
                result
        ));

        writer.newLine();
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
}