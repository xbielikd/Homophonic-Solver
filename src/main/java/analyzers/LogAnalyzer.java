package analyzers;

import java.io.*;
import java.nio.file.*;
import java.util.regex.*;
import java.util.List;
import java.util.ArrayList;

public class LogAnalyzer {

    public static void extractToCsv(String logDirectory, String outputCsvPath) throws Exception {

        List<Path> logFiles;
        try (var stream = Files.walk(Path.of(logDirectory))) {
            logFiles = stream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".txt"))
                    .sorted()
                    .toList();
        }

        // regex patterns for each value we want
        Pattern keyTypePattern     = Pattern.compile("KEY TYPE:\\s+(\\S+)");
        Pattern keyConstPattern    = Pattern.compile("KEY CONST:\\s+(\\S+)");
        Pattern cipherSymsPattern  = Pattern.compile("CIPHER SYMBOLS:\\s+(\\d+)");
        Pattern textLenPattern     = Pattern.compile("TEXT LENGTH:\\s+(\\d+)");
        Pattern accuracyPattern    = Pattern.compile("ACCURACY:\\s+\\d+ / \\d+ chars correct \\((\\d+\\.\\d+)%\\)");
        Pattern correctPattern     = Pattern.compile("--- CORRECT\\? ---\\s+(YES[^\\n]*|NO[^\\n]*)");

        StringBuilder csv = new StringBuilder();
        csv.append("filename,keyType,keyConst,cipherSymbols,textLength,accuracy,correct\n");

        for (Path file : logFiles) {
            String content = Files.readString(file);

            String keyType    = extractGroup(keyTypePattern,    content, "UNKNOWN");
            String keyConst   = extractGroup(keyConstPattern,   content, "UNKNOWN");
            String cipherSyms = extractGroup(cipherSymsPattern, content, "0");
            String textLen    = extractGroup(textLenPattern,    content, "0");
            String accuracy   = extractGroup(accuracyPattern,   content, "0.0");
            String correct    = extractGroup(correctPattern,    content, "UNKNOWN")
                    .trim().startsWith("YES") ? "YES" : "NO";

            csv.append(String.format("%s,%s,%s,%s,%s,%s,%s%n",
                    file.getFileName(),
                    keyType,
                    keyConst,
                    cipherSyms,
                    textLen,
                    accuracy,
                    correct));
        }

        Path out = Path.of(outputCsvPath);
        Files.createDirectories(out.getParent());
        Files.writeString(out, csv.toString());

        System.out.println("CSV written to: " + outputCsvPath);
        System.out.println("Total log files processed: " + logFiles.size());
    }

    private static String extractGroup(Pattern pattern, String content, String defaultVal) {
        Matcher m = pattern.matcher(content);
        return m.find() ? m.group(1).trim() : defaultVal;
    }

    static void main() throws Exception {
        extractToCsv("src/main/resources/logs/logFile_/pt_1000_", "src/main/resources/logs/logResults1000.csv");
    }

}
