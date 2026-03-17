package analyzers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class SuccessCounter {

    public static void countMatches(String logDirectory) throws Exception {
        long total = 0;
        long matched = 0;

        try (var stream = Files.walk(Path.of(logDirectory))) {
            List<Path> logFiles = stream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".txt"))
                    .toList();

            total = logFiles.size();

            for (Path file : logFiles) {
                String content = Files.readString(file);
                if (content.contains("YES - MATCH")) {
                    matched++;
                }
            }
        }

        System.out.printf("Results: %d / %d files matched (%.1f%%)%n", matched, total, 100.0 * matched / total);
    }

    static void main() throws Exception {
        countMatches("src/main/resources/logs/logFile_/pt_100_");
    }
}
