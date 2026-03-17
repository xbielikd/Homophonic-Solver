import cipherGenerators.HomophonicCipherGenerator;
import cipherGenerators.KeyType;
import parsers.CiphertextParser;
import helpers.PreprocessResult;
import helpers.Preprocessor;
import scorers.QuadGramScorer;
import solvers.HomophonicAnnealingSolver;
import solvers.SolverResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HomophonicSolverMain {

    private static Double FREQ_CONST = 0.10;
    private static Double freqTempVariable = 0.10;

    private static Integer FIXED_CONST = 2;
    private static Integer fixTempVariable = 2;

    private static final Integer NUMBER_OF_TEXTS = 10;
//    private static String PATH_TO_FILES_LENGTH_100 = "src/main/resources/plaintexts/length100/";
//    private static String PATH_TO_FILES_LENGTH_200 = "src/main/resources/plaintexts/length200/";
//    private static String PATH_TO_FILES_LENGTH_500 = "src/main/resources/plaintexts/length500/";
//    private static String PATH_TO_FILES_LENGTH_1000 = "src/main/resources/plaintexts/length1000/";

    private static String PATH_TO_FILES_LENGTH_100 = "src/main/resources/extracted/length100/";
    private static String PATH_TO_FILES_LENGTH_200 = "src/main/resources/extracted/length200/";
    private static String PATH_TO_FILES_LENGTH_500 = "src/main/resources/extracted/length500/";
    private static String PATH_TO_FILES_LENGTH_1000 = "src/main/resources/extracted/length1000/";

    private static String FILENAME_PREFIX_100 = "pt_100_";
    private static String FILENAME_PREFIX_200 = "pt_200_";
    private static String FILENAME_PREFIX_500 = "pt_500_";
    private static String FILENAME_PREFIX_1000 = "pt_1000_";
    public static void main(String[] args) throws Exception {

        System.out.println("Started at: " + LocalTime.now());

        doLoop(PATH_TO_FILES_LENGTH_100, FILENAME_PREFIX_100, KeyType.FIXED);
        doLoop(PATH_TO_FILES_LENGTH_200, FILENAME_PREFIX_200, KeyType.FIXED);
        doLoop(PATH_TO_FILES_LENGTH_500, FILENAME_PREFIX_500, KeyType.FIXED);
        doLoop(PATH_TO_FILES_LENGTH_1000, FILENAME_PREFIX_1000, KeyType.FIXED);


        System.out.println("Ended at: " + LocalTime.now());

    }

    public static void doLoop(String path, String fileNamePrefix, KeyType type) throws Exception {
//        freqTempVariable = FREQ_CONST;
        fixTempVariable = FIXED_CONST;
        // ======= Scorer — quadgrams only =======
        QuadGramScorer quad = new QuadGramScorer();
        quad.loadFromTxt("src/main/resources/quadgrams.txt");
        for (int i = 1; i<=NUMBER_OF_TEXTS; i++) {
            for (int j=0; j<5; j++) {
//                freqTempVariable = FREQ_CONST + (j * 0.02);
                fixTempVariable = FIXED_CONST + (j - 1);
                StringBuilder log = new StringBuilder(); // one log per iteration

                String inputFilename = fileNamePrefix + i;
//                String inputFilename = fileNamePrefix + i + "_keyFix_" + j;
                String plaintext = extractFromTxtFile(STR."\{path}\{inputFilename}.txt");
                List<Integer>[] key = generateKey(plaintext, type, log);
                String ct = generateCT(plaintext, key);


                int[] rawCipher = CiphertextParser.parseCiphertext(ct);

                // ======= Preprocessing =======
                PreprocessResult prep = Preprocessor.preprocessCiphertext(rawCipher);
                System.out.println("Cipher symbols: " + prep.numCipherSymbols);
                System.out.println("Text length:    " + prep.ctIdx.length);


                // ======= Solve =======
                HomophonicAnnealingSolver solver = new HomophonicAnnealingSolver(prep, quad);
                SolverResult result = solver.solve();

                // ======= Print =======
                System.out.println("\n=== BEST SCORE ===");
                System.out.println(result.score);

                System.out.println("\n=== PLAINTEXT ===");
                System.out.println(result.plaintext);

                System.out.println("\n=== KEY (cipher -> plain) ===");
                result.printKey();

                // ======= Log to file =======
//            String logPath = "src/main/resources/logs/"+ fileNamePrefix +"/logFile_" + inputFilename + FREQ_CONST +".txt";
//                String logPath = "src/main/resources/logs/logFile_/" + fileNamePrefix +"/" + inputFilename + "_keyFreq_" + (j+1) + "_" + freqTempVariable + ".txt";
                String logPath = "src/main/resources/logs/logFile_/" + fileNamePrefix +"/" + inputFilename + "_keyFix_" + (j+1) + "_" + fixTempVariable + ".txt";
                writeLog(logPath, plaintext, ct, prep, result, type, log);
            }

        }
    }

    public static List<Integer>[] generateKey(String plaintext, KeyType type, StringBuilder log){
        List<Integer>[] key = null;
        if (type == KeyType.FREQ){
            key = HomophonicCipherGenerator.generateHomophonicKey(plaintext, freqTempVariable, log);
        } else if (type == KeyType.FIXED) {
            key = HomophonicCipherGenerator.generateHomophonicKey(plaintext, fixTempVariable, log);
        }
        return key;
    }

    public static String generateCT(String plaintext, List<Integer>[] key){
        int[] ciphertext = HomophonicCipherGenerator.encrypt(plaintext, key);
        return Arrays.stream(ciphertext)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" "));
    }

    public static String extractFromTxtFile(String filepath) throws Exception {
        return Files.readString(Path.of(filepath)).trim().toUpperCase().replaceAll("[^A-Z]", "");
    }


    public static void writeLog(String logPath, String plaintext, String ct,
                                PreprocessResult prep, SolverResult result, KeyType type, StringBuilder log) throws Exception {

        StringBuilder sb = new StringBuilder();

        sb.append("=====================================\n");
        sb.append("  HOMOPHONIC SOLVER LOG\n");
        sb.append("=====================================\n\n");

        sb.append("--- GENERATOR INFO ---\n");
        sb.append(log).append("\n\n"); // append everything collected from the generator

        sb.append("KEY TYPE:         ").append(type).append("\n");
        sb.append("KEY CONST:        ").append(type == KeyType.FIXED ? fixTempVariable : freqTempVariable).append("\n");

        sb.append("CIPHER SYMBOLS:   ").append(prep.numCipherSymbols).append("\n");
        sb.append("TEXT LENGTH:      ").append(prep.ctIdx.length).append("\n\n");

        sb.append("--- ORIGINAL PLAINTEXT ---\n");
        sb.append(plaintext).append("\n\n");

        sb.append("--- CIPHERTEXT ---\n");
        sb.append(ct).append("\n\n");

        sb.append("--- DECRYPTED PLAINTEXT ---\n");
        sb.append(result.plaintext).append("\n\n");

        sb.append("--- SCORE ---\n");
        sb.append(String.format("%.4f%n%n", result.score));



        // count how many chars match for partial score
        int correct = 0;
        for (int i = 0; i < Math.min(plaintext.length(), result.plaintext.length()); i++) {
            if (plaintext.charAt(i) == result.plaintext.charAt(i)) correct++;
        }
        double pct = 100.0 * correct / plaintext.length();

        sb.append("--- CORRECT? ---\n");
        sb.append(pct>=80 ? "YES - MATCH" : "NO - MISMATCH").append("\n\n");

        sb.append(String.format("ACCURACY: %d / %d chars correct (%.1f%%)%n%n", correct, plaintext.length(), pct));

        sb.append("--- KEY (cipher -> plain) ---\n");
        int[] origSymbols = result.state.prep.originalSymbol;
        Integer[] indices = new Integer[result.state.key.length];
        for (int i = 0; i < indices.length; i++) indices[i] = i;
        Arrays.sort(indices, (a, b) -> origSymbols[a] - origSymbols[b]);
        for (int idx : indices) {
            sb.append(String.format("%-6d -> %c%n", origSymbols[idx], (char)('A' + result.state.key[idx])));
        }

        // create logs directory if it doesn't exist
        Path logFile = Path.of(logPath);
        Files.createDirectories(logFile.getParent());
        Files.writeString(logFile, sb.toString());

        System.out.println("Log written to: " + logPath);
    }

}
