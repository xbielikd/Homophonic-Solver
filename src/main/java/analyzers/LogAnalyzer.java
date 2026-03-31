package analyzers;

import java.io.*;
import java.nio.file.*;
import java.util.Comparator;
import java.util.regex.*;
import java.util.List;
import java.util.ArrayList;

public class LogAnalyzer {

    public static void extractToCsv(List<String> logDirectories, String outputCsvPath) throws Exception {

        List<Path> logFiles = new ArrayList<>();

        for (String dir : logDirectories) {
            try (var stream = Files.walk(Path.of(dir))) {
                stream.filter(Files::isRegularFile)
                        .filter(p -> p.toString().endsWith(".txt"))
                        .forEach(logFiles::add);
            }
        }

        logFiles.sort(Comparator.naturalOrder());

        // regex patterns for each value we want
        Pattern keyTypePattern    = Pattern.compile("KEY TYPE:\\s+(\\S+)");
        Pattern keyConstPattern   = Pattern.compile("KEY CONST:\\s+(\\S+)");
        Pattern cipherSymsPattern = Pattern.compile("CIPHER SYMBOLS:\\s+(\\d+)");
        Pattern textLenPattern    = Pattern.compile("TEXT LENGTH:\\s+(\\d+)");
        Pattern accuracyPattern   = Pattern.compile("ACCURACY:\\s+\\d+ / \\d+ chars correct \\((\\d+\\.\\d+)%\\)");
        Pattern correctPattern    = Pattern.compile("--- CORRECT\\? ---\\s+(YES[^\\n]*|NO[^\\n]*)");

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


    public static void addSuffixToFiles(String directory, String suffix) throws Exception {
        try (var stream = Files.walk(Path.of(directory), 1)) {
            stream.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".txt"))
                    .forEach(file -> {
                        String oldName = file.getFileName().toString();
                        String newName = oldName.replace(".txt", suffix + ".txt");
                        try {
                            Files.move(file, file.resolveSibling(newName));
                        } catch (Exception e) {
                            System.err.println("Failed to rename: " + oldName);
                        }
                    });
        }
    }

    private static final String DORIAN_GRAY= "dorianGray";
    private static final String FRANKENSTEIN= "frankenstein";
    private static final String GREAT_GATSBY= "greatGatsby";

    private static final String BIS = "bis";
    private static final String TRIS = "tris";
    private static final String QUADS = "quads";
    private static final String QUINTS = "quints";


    static void main() throws Exception {
//        String ngrams = BIS;
//        String book = DORIAN_GRAY;
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_100_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_250_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_500_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_1000_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_2000_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_100_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_250_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_500_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_1000_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_2000_", "_"+book);
//
//        book = FRANKENSTEIN;
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_100_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_250_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_500_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_1000_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_2000_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_100_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_250_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_500_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_1000_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_2000_", "_"+book);
//
//        book = GREAT_GATSBY;
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_100_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_250_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_500_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_1000_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_2000_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_100_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_250_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_500_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_1000_", "_"+book);
//        addSuffixToFiles("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_2000_", "_"+book);





        String ngrams = QUINTS;
        List<String> directories100Fix = new ArrayList<>();
        List<String> directories250Fix = new ArrayList<>();
        List<String> directories500Fix = new ArrayList<>();
        List<String> directories1000Fix = new ArrayList<>();
        List<String> directories2000Fix = new ArrayList<>();

        List<String> directories100Freq = new ArrayList<>();
        List<String> directories250Freq = new ArrayList<>();
        List<String> directories500Freq = new ArrayList<>();
        List<String> directories1000Freq = new ArrayList<>();
        List<String> directories2000Freq = new ArrayList<>();

        String book = DORIAN_GRAY;
        directories100Fix.add("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_100_");
        directories250Fix.add("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_250_");
        directories500Fix.add("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_500_");
        directories1000Fix.add("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_1000_");
        directories2000Fix.add("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_2000_");

        directories100Freq.add("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_100_");
        directories250Freq.add("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_250_");
        directories500Freq.add("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_500_");
        directories1000Freq.add("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_1000_");
        directories2000Freq.add("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_2000_");

        book = FRANKENSTEIN;
        directories100Fix.add("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_100_");
        directories250Fix.add("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_250_");
        directories500Fix.add("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_500_");
        directories1000Fix.add("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_1000_");
        directories2000Fix.add("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_2000_");

        directories100Freq.add("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_100_");
        directories250Freq.add("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_250_");
        directories500Freq.add("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_500_");
        directories1000Freq.add("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_1000_");
        directories2000Freq.add("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_2000_");

        book = GREAT_GATSBY;
        directories100Fix.add("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_100_");
        directories250Fix.add("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_250_");
        directories500Fix.add("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_500_");
        directories1000Fix.add("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_1000_");
        directories2000Fix.add("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_2000_");

        directories100Freq.add("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_100_");
        directories250Freq.add("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_250_");
        directories500Freq.add("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_500_");
        directories1000Freq.add("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_1000_");
        directories2000Freq.add("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_2000_");

        extractToCsv(directories100Fix, "src/main/resources/logs/"+ngrams+"/logResults100Fix.csv");
        extractToCsv(directories250Fix, "src/main/resources/logs/"+ngrams+"/logResults250Fix.csv");
        extractToCsv(directories500Fix, "src/main/resources/logs/"+ngrams+"/logResults500Fix.csv");
        extractToCsv(directories1000Fix, "src/main/resources/logs/"+ngrams+"/logResults1000Fix.csv");
        extractToCsv(directories2000Fix, "src/main/resources/logs/"+ngrams+"/logResults2000Fix.csv");

        extractToCsv(directories100Freq, "src/main/resources/logs/"+ngrams+"/logResults100Freq.csv");
        extractToCsv(directories250Freq, "src/main/resources/logs/"+ngrams+"/logResults250Freq.csv");
        extractToCsv(directories500Freq, "src/main/resources/logs/"+ngrams+"/logResults500Freq.csv");
        extractToCsv(directories1000Freq, "src/main/resources/logs/"+ngrams+"/logResults1000Freq.csv");
        extractToCsv(directories2000Freq, "src/main/resources/logs/"+ngrams+"/logResults2000Freq.csv");


//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_100_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults100Fix.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_250_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults250Fix.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_500_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults500Fix.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_1000_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults1000Fix.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_2000_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults2000Fix.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_100_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults100Freq.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_250_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults250Freq.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_500_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults500Freq.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_1000_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults1000Freq.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_2000_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults2000Freq.csv");
//
//        book = FRANKENSTEIN;
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_100_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults100Fix.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_250_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults250Fix.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_500_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults500Fix.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_1000_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults1000Fix.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_2000_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults2000Fix.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_100_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults100Freq.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_250_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults250Freq.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_500_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults500Freq.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_1000_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults1000Freq.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_2000_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults2000Freq.csv");
//
//        book = GREAT_GATSBY;
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_100_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults100Fix.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_250_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults250Fix.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_500_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults500Fix.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_1000_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults1000Fix.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/fix"+"/pt_2000_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults2000Fix.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_100_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults100Freq.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_250_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults250Freq.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_500_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults500Freq.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_1000_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults1000Freq.csv");
//        extractToCsv("src/main/resources/logs/"+ngrams+"/"+book+"/freq"+"/pt_2000_", "src/main/resources/logs/"+ngrams+"/"+book+"/logResults2000Freq.csv");
    }

}
