import cipherGenerators.HomophonicCipherGenerator;
import cipherGenerators.KeyType;
import parsers.CiphertextParser;
import helpers.PreprocessResult;
import helpers.Preprocessor;
import scorers.*;
import solvers.HomophonicAnnealingSolver;
import solvers.SolverResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HomophonicSolverMain {

    private static Double FREQ_CONST = 0.30;
    private static Double freqTempVariable = 0.30;

    private static Integer FIXED_CONST = 2;
    private static Integer fixTempVariable = 2;

    private static final Integer NUMBER_OF_TEXTS = 10;

    // --- THIS IS ONE SUITE ---
    private static String PATH_TO_FILES_LENGTH_100_GREAT_GATSBY = "src/main/resources/extracted/greatGatsby/length100/";
    private static String PATH_TO_FILES_LENGTH_250_GREAT_GATSBY = "src/main/resources/extracted/greatGatsby/length250/";
    private static String PATH_TO_FILES_LENGTH_500_GREAT_GATSBY = "src/main/resources/extracted/greatGatsby/length500/";
    private static String PATH_TO_FILES_LENGTH_1000_GREAT_GATSBY = "src/main/resources/extracted/greatGatsby/length1000/";
    private static String PATH_TO_FILES_LENGTH_2000_GREAT_GATSBY = "src/main/resources/extracted/greatGatsby/length2000/";

    private static String PATH_TO_FILES_LENGTH_100_DORIAN_GRAY = "src/main/resources/extracted/dorianGray/length100/";
    private static String PATH_TO_FILES_LENGTH_250_DORIAN_GRAY = "src/main/resources/extracted/dorianGray/length250/";
    private static String PATH_TO_FILES_LENGTH_500_DORIAN_GRAY = "src/main/resources/extracted/dorianGray/length500/";
    private static String PATH_TO_FILES_LENGTH_1000_DORIAN_GRAY = "src/main/resources/extracted/dorianGray/length1000/";
    private static String PATH_TO_FILES_LENGTH_2000_DORIAN_GRAY = "src/main/resources/extracted/dorianGray/length2000/";

    private static String PATH_TO_FILES_LENGTH_100_FRANKENSTEIN = "src/main/resources/extracted/frankenstein/length100/";
    private static String PATH_TO_FILES_LENGTH_250_FRANKENSTEIN = "src/main/resources/extracted/frankenstein/length250/";
    private static String PATH_TO_FILES_LENGTH_500_FRANKENSTEIN = "src/main/resources/extracted/frankenstein/length500/";
    private static String PATH_TO_FILES_LENGTH_1000_FRANKENSTEIN = "src/main/resources/extracted/frankenstein/length1000/";
    private static String PATH_TO_FILES_LENGTH_2000_FRANKENSTEIN = "src/main/resources/extracted/frankenstein/length2000/";
    // --- THIS IS ONE SUITE ---

    private static String FILENAME_PREFIX_100 = "pt_100_";
    private static String FILENAME_PREFIX_250 = "pt_250_";
    private static String FILENAME_PREFIX_500 = "pt_500_";
    private static String FILENAME_PREFIX_1000 = "pt_1000_";
    private static String FILENAME_PREFIX_2000 = "pt_2000_";

    public static void main(String[] args) throws Exception {

        LocalTime locTimeStart = LocalTime.now();
        System.out.println("Started at: " + locTimeStart);

//        doLoop(PATH_TO_FILES_LENGTH_100_FRANKENSTEIN, FILENAME_PREFIX_100, KeyType.FIXED);
//        doLoop(PATH_TO_FILES_LENGTH_100_FRANKENSTEIN, FILENAME_PREFIX_100, KeyType.FREQ);
//        doLoop(PATH_TO_FILES_LENGTH_100_GREAT_GATSBY, FILENAME_PREFIX_100, KeyType.FIXED);
//        doLoop(PATH_TO_FILES_LENGTH_100_GREAT_GATSBY, FILENAME_PREFIX_100, KeyType.FREQ);
//        doLoop(PATH_TO_FILES_LENGTH_100_DORIAN_GRAY, FILENAME_PREFIX_100, KeyType.FIXED);
//        doLoop(PATH_TO_FILES_LENGTH_100_DORIAN_GRAY, FILENAME_PREFIX_100, KeyType.FREQ);

//        doLoop(PATH_TO_FILES_LENGTH_250_FRANKENSTEIN, FILENAME_PREFIX_250, KeyType.FIXED);
//        doLoop(PATH_TO_FILES_LENGTH_250_FRANKENSTEIN, FILENAME_PREFIX_250, KeyType.FREQ);
//        doLoop(PATH_TO_FILES_LENGTH_250_GREAT_GATSBY, FILENAME_PREFIX_250, KeyType.FIXED);
//        doLoop(PATH_TO_FILES_LENGTH_250_GREAT_GATSBY, FILENAME_PREFIX_250, KeyType.FREQ);
//        doLoop(PATH_TO_FILES_LENGTH_250_DORIAN_GRAY, FILENAME_PREFIX_250, KeyType.FIXED);
//        doLoop(PATH_TO_FILES_LENGTH_250_DORIAN_GRAY, FILENAME_PREFIX_250, KeyType.FREQ);

//        doLoop(PATH_TO_FILES_LENGTH_500_FRANKENSTEIN, FILENAME_PREFIX_500, KeyType.FIXED);
//        doLoop(PATH_TO_FILES_LENGTH_500_FRANKENSTEIN, FILENAME_PREFIX_500, KeyType.FREQ);
//        doLoop(PATH_TO_FILES_LENGTH_500_GREAT_GATSBY, FILENAME_PREFIX_500, KeyType.FIXED);
//        doLoop(PATH_TO_FILES_LENGTH_500_GREAT_GATSBY, FILENAME_PREFIX_500, KeyType.FREQ);
//        doLoop(PATH_TO_FILES_LENGTH_500_DORIAN_GRAY, FILENAME_PREFIX_500, KeyType.FIXED);
//        doLoop(PATH_TO_FILES_LENGTH_500_DORIAN_GRAY, FILENAME_PREFIX_500, KeyType.FREQ);

//        doLoop(PATH_TO_FILES_LENGTH_1000_FRANKENSTEIN, FILENAME_PREFIX_1000, KeyType.FIXED);
//        doLoop(PATH_TO_FILES_LENGTH_1000_FRANKENSTEIN, FILENAME_PREFIX_1000, KeyType.FREQ);
//        doLoop(PATH_TO_FILES_LENGTH_1000_GREAT_GATSBY, FILENAME_PREFIX_1000, KeyType.FIXED);
//        doLoop(PATH_TO_FILES_LENGTH_1000_GREAT_GATSBY, FILENAME_PREFIX_1000, KeyType.FREQ);
//        doLoop(PATH_TO_FILES_LENGTH_1000_DORIAN_GRAY, FILENAME_PREFIX_1000, KeyType.FIXED);
//        doLoop(PATH_TO_FILES_LENGTH_1000_DORIAN_GRAY, FILENAME_PREFIX_1000, KeyType.FREQ);

//        doLoop(PATH_TO_FILES_LENGTH_2000_FRANKENSTEIN, FILENAME_PREFIX_2000, KeyType.FIXED);
//        doLoop(PATH_TO_FILES_LENGTH_2000_FRANKENSTEIN, FILENAME_PREFIX_2000, KeyType.FREQ);
//        doLoop(PATH_TO_FILES_LENGTH_2000_GREAT_GATSBY, FILENAME_PREFIX_2000, KeyType.FIXED);
//        doLoop(PATH_TO_FILES_LENGTH_2000_GREAT_GATSBY, FILENAME_PREFIX_2000, KeyType.FREQ);
//        doLoop(PATH_TO_FILES_LENGTH_2000_DORIAN_GRAY, FILENAME_PREFIX_2000, KeyType.FIXED);
//        doLoop(PATH_TO_FILES_LENGTH_2000_DORIAN_GRAY, FILENAME_PREFIX_2000, KeyType.FREQ);

        LocalTime locTimeEnd = LocalTime.now();
        System.out.println("Ended at: " + locTimeEnd);
        System.out.println("Elapsed time: " + locTimeStart + " - " + locTimeEnd);
    }

//    public static void test() throws IOException {
//        String pt1 = "The king sends a secret message to his envoy in a distant city. He must learn about enemy troop movements and prepare allies for a possible conflict. It is important to remain silent and use trusted messengers. Any mistake could endanger the entire kingdom and its future safety. The message must remain hidden at all times.";
//        String pt2 = "The king has ordered his most trusted advisor to prepare a secret message for an allied court. The message contains information about troop movements, supplies, and the enemy’s plans for the coming months. The messenger must travel quietly, avoid main roads, and stop only at safe locations. Every detail is important, as even a small mistake could expose the entire operation. The allies must be ready to respond quickly and in coordination. The message also includes codes for commanders’ names and place identifiers to prevent sensitive information from being revealed if intercepted.";
//        String pt3 = "The king issued a strict order that a carefully encoded message be prepared for a distant ally. The message includes detailed information about the state of the army, the number of soldiers, supplies, and the exact routes of the planned movement. Special attention is given to reports about the enemy, their weaknesses, and possible strategies they might use in the near future. The messenger was chosen from the most reliable men and instructed to avoid dangerous areas and contact with strangers. In case of danger, he must destroy the message immediately. The encoding uses a combination of symbols and numbers to make it resistant to decryption. Upon receiving the message, the allied court must prepare defensive measures and coordinate further actions with the king’s forces.";
//        StringBuilder log = new StringBuilder(); // one log per iteration
//        freqTempVariable = 0.65;
//        String plaintext = pt3.toUpperCase().replaceAll(" ", "");
//        plaintext = plaintext.toUpperCase().replaceAll("[^A-Z]", "");
//        log.append(plaintext);
//        List<Integer>[] key = generateKey(plaintext, KeyType.FREQ, log);
//        String ct = generateCT(plaintext, key);
//        log.append("CT: ").append(ct);
//        System.out.println(plaintext);
//        System.out.println(plaintext.length());
//        System.out.println(key);
//        System.out.println(ct);
//
//        String logPath = "src/main/resources/forLaco/pt3Log.txt";
//        Path logFile = Path.of(logPath);
//        Files.createDirectories(logFile.getParent());
//        Files.writeString(logFile, log.toString());
//    }


    public static void doLoop(String path, String fileNamePrefix, KeyType type) throws Exception {
        if (type == KeyType.FREQ) {
            freqTempVariable = FREQ_CONST;
        } else {
            fixTempVariable = FIXED_CONST;
        }


        // ======= Scorer — quadgrams only =======
//        QuadGramScorer quad = new QuadGramScorer();
//        quad.loadFromTxt("src/main/resources/quadgrams.txt");
//        TriGramScorer tri = new TriGramScorer();
//        tri.loadFromTxt("src/main/resources/trigrams.txt");
//        BiGramScorer bi = new BiGramScorer();
//        bi.loadFromTxt("src/main/resources/bigrams.txt");
        QuintGramScorer quint = new QuintGramScorer();
        quint.loadFromTxt("src/main/resources/english_quintgrams.txt");

//        NGramScorer ngram = new NGramScorer();
//        ngram.bi = new BiGramScorer();
//        ngram.tri = new TriGramScorer();
//        ngram.quad = new QuadGramScorer();
//
//        ngram.bi.loadFromTxt("src/main/resources/bigrams.txt");
//        ngram.tri.loadFromTxt("src/main/resources/trigrams.txt");
//        ngram.quad.loadFromTxt("src/main/resources/quadgrams.txt");


        for (int i = 1; i<=NUMBER_OF_TEXTS; i++) {
            for (int j=0; j<4; j++) {
//                freqTempVariable = FREQ_CONST + (j * 0.02);
//                fixTempVariable = FIXED_CONST + (j - 1);
                if (type == KeyType.FREQ) {
                    freqTempVariable = FREQ_CONST + (j * 0.3);
                } else {
                    fixTempVariable = FIXED_CONST + j;
                }
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
                HomophonicAnnealingSolver solver = new HomophonicAnnealingSolver(prep, quint);
//                HomophonicAnnealingSolver solver = new HomophonicAnnealingSolver(prep, ngram);
                SolverResult result = solver.solve(type);

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
                String logPath = "";
                if (type == KeyType.FREQ){
                    if (path.contains("greatGatsby")) {
                        logPath = "src/main/resources/logs/quints/greatGatsby/freq/" + fileNamePrefix + "/" + inputFilename + "_keyFreq_" + (j + 1) + "_" + freqTempVariable + ".txt";
                    } else if (path.contains("frankenstein")) {
                        logPath = "src/main/resources/logs/quints/frankenstein/freq/" + fileNamePrefix + "/" + inputFilename + "_keyFreq_" + (j + 1) + "_" + freqTempVariable + ".txt";
                    } else if (path.contains("dorianGray")) {
                        logPath = "src/main/resources/logs/quints/dorianGray/freq/" + fileNamePrefix + "/" + inputFilename + "_keyFreq_" + (j + 1) + "_" + freqTempVariable + ".txt";
                    }
                } else {
                    if (path.contains("greatGatsby")) {
                        logPath = "src/main/resources/logs/quints/greatGatsby/fix/" + fileNamePrefix +"/" + inputFilename + "_keyFix_" + (j+1) + "_" + fixTempVariable + ".txt";
                    } else if (path.contains("frankenstein")) {
                        logPath = "src/main/resources/logs/quints/frankenstein/fix/" + fileNamePrefix +"/" + inputFilename + "_keyFix_" + (j+1) + "_" + fixTempVariable + ".txt";
                    } else if (path.contains("dorianGray")) {
                        logPath = "src/main/resources/logs/quints/dorianGray/fix/" + fileNamePrefix +"/" + inputFilename + "_keyFix_" + (j+1) + "_" + fixTempVariable + ".txt";
                    }
                }
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

    //TODO this is mine
    public static String generateCT(String plaintext, List<Integer>[] key){
        int[] ciphertext = HomophonicCipherGenerator.encrypt(plaintext, key);
        return Arrays.stream(ciphertext)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" "));
    }

    //this is for laco
//    public static String generateCT(String plaintext, List<Integer>[] key) {
//        int[] ciphertext = HomophonicCipherGenerator.encrypt(plaintext, key);
//        return Arrays.stream(ciphertext)
//                .mapToObj(n -> String.format("%02d", n))
//                .collect(Collectors.joining(" "));
//    }

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
