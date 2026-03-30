package extractors;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class TextExtractor {
    private static final Random rand = new Random();
    private static final int MAX_ATTEMPTS = 50;

    /**
     * Extracts a chunk of text from a plain text file (e.g. Project Gutenberg .txt).
     *
     * Rules:
     *  - Picks a random starting position that is the beginning of a new sentence
     *    (i.e. after a period/exclamation/question mark followed by whitespace).
     *  - Reads at least `minLetters` letters from that point.
     *  - Stops at the end of the current word once minLetters is reached.
     *  - Strips everything that is not A-Z (digits, punctuation, spaces removed).
     *  - If the scanned region contains a digit, the starting point is rejected
     *    and a new one is picked — preserving the true meaning of the text.
     *  - Returns UPPERCASE result.
     *
     * @param filepath   path to the .txt file
     * @param minLetters minimum number of letters in the result
     */
    public static String extractFromTxtFile(String filepath, int minLetters) throws Exception {

        String raw = Files.readString(Path.of(filepath));
        // Find all sentence-start positions:
        // A sentence starts after  .  !  ?  followed by one or more whitespace chars.
        // We collect the index of the first letter of each new sentence.
        java.util.List<Integer> sentenceStarts = new java.util.ArrayList<>();

        // Also treat the very beginning of the file as a valid start
        // (skip any leading whitespace/header)
        int firstLetter = 0;
        while (firstLetter < raw.length() && !Character.isLetter(raw.charAt(firstLetter))) {
            firstLetter++;
        }
        if (firstLetter < raw.length()) sentenceStarts.add(firstLetter);

        for (int i = 1; i < raw.length() - 1; i++) {
            char c = raw.charAt(i);
            if (c == '.' || c == '!' || c == '?') {
                // skip any whitespace after the punctuation
                int j = i + 1;
                while (j < raw.length() && Character.isWhitespace(raw.charAt(j))) j++;
                // j should now point to the first letter of the next sentence
                if (j < raw.length() && Character.isUpperCase(raw.charAt(j))) {
                    sentenceStarts.add(j);
                }
            }
        }

        if (sentenceStarts.isEmpty()) {
            throw new IllegalStateException("No sentence starts found in file: " + filepath);
        }

        // Try up to MAX_ATTEMPTS random starting positions
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {

            // Pick a random sentence start
            int startIdx = sentenceStarts.get(rand.nextInt(sentenceStarts.size()));

            // Scan forward from startIdx, collecting the raw chunk
            // We stop when we have >= minLetters AND we are at a word boundary
            // (i.e. the next character is a space or punctuation)
            StringBuilder lettersOnly = new StringBuilder();
            StringBuilder rawChunk    = new StringBuilder();
            boolean containsDigit     = false;
            boolean reachedMin        = false;
            boolean endedAtWordBound  = false;

            int i = startIdx;
            while (i < raw.length()) {
                char c = raw.charAt(i);

                // If we hit a digit anywhere in the region — reject this start
                if (Character.isDigit(c)) {
                    containsDigit = true;
                    break;
                }

                rawChunk.append(c);

                if (Character.isLetter(c)) {
                    lettersOnly.append(Character.toUpperCase(c));
                }

                // Check if we've reached the minimum letter count
                if (!reachedMin && lettersOnly.length() >= minLetters) {
                    reachedMin = true;
                }

                // If we've reached minimum, wait for end of current word
                // (end of word = next char is not a letter)
                if (reachedMin) {
                    boolean nextIsLetter = (i + 1 < raw.length()) && Character.isLetter(raw.charAt(i + 1));
                    if (!nextIsLetter) {
                        endedAtWordBound = true;
                        break;
                    }
                }

                i++;
            }

            if (containsDigit) continue;  // try another start
            if (!endedAtWordBound) continue; // ran off end of file, try another

            return lettersOnly.toString();
        }

        throw new IllegalStateException(
                "Could not find a valid starting position after " + MAX_ATTEMPTS +
                        " attempts in file: " + filepath);
    }

    public static void storeIntoTxtFile(String text, String filename) throws Exception {
        Path outputPath = Path.of("src/main/resources/extracted/greatGatsby/length"+MIN_LETTERS+"/" + filename);
        Files.createDirectories(outputPath.getParent());
        Files.writeString(outputPath, text);
    }

    public static int MIN_LETTERS = 250;
    static void main() throws Exception {
        for (int i = 1; i<=10; i++) {
            String text = TextExtractor.extractFromTxtFile("src/main/resources/book/greatGatsby.txt", MIN_LETTERS);
            System.out.println(text);
            System.out.println(text.length());
            storeIntoTxtFile(text, "pt_" + MIN_LETTERS + "_" + i + ".txt");
        }
    }
}
