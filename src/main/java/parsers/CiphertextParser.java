package parsers;

public class CiphertextParser {

    // Expect numbers separated by whitespace
    public static int[] parseCiphertext(String ct) {

        String[] parts = ct.trim().split("\\s+");

        int[] raw = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            raw[i] = Integer.parseInt(parts[i]);
        }
        return raw;
    }
}
