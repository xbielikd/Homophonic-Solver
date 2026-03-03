package scorers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Quadgram scorer backed by a flat double[] array instead of HashMap.
 * 26^4 = 456,976 entries — fits easily in ~3.5 MB.
 * Lookup is a single array index, no boxing, no hashing — much faster.
 *
 * Also exposes scoreAt(plain, pos) for delta scoring:
 * score the single quadgram starting at position pos.
 */
public class QuadGramScorer {

    private static final int SIZE = 26 * 26 * 26 * 26; // 456976

    // flat log-prob array, indexed by quadToInt
    private double[] logProbs;
    private double floor;

    public void loadFromTxt(String path) throws Exception {

        logProbs = new double[SIZE];

        // fill with a very negative sentinel first (we'll set floor after)
        // We use Double.NEGATIVE_INFINITY as placeholder, replaced after counting
        java.util.Arrays.fill(logProbs, Double.NEGATIVE_INFINITY);

        BufferedReader br = new BufferedReader(new FileReader(path));
        long total = 0;
        List<String> lines = new ArrayList<>();

        String line;
        while ((line = br.readLine()) != null) {
            if (line.isBlank()) continue;
            lines.add(line);
            String[] parts = line.split("\\s+");
            total += Long.parseLong(parts[1]);
        }
        br.close();

        // floor: probability for unseen quadgrams
        // Using 0.01 / total gives a very small but nonzero probability.
        floor = Math.log(0.01 / total);

        for (String l : lines) {
            String[] parts = l.split("\\s+");
            String gram = parts[0].toUpperCase();
            if (gram.length() != 4) continue;
            long count = Long.parseLong(parts[1]);
            int idx = quadToInt(gram);
            logProbs[idx] = Math.log((double) count / total);
        }

        // Replace sentinels with floor
        for (int i = 0; i < SIZE; i++) {
            if (logProbs[i] == Double.NEGATIVE_INFINITY) {
                logProbs[i] = floor;
            }
        }
    }

    /** Full score of the entire plaintext array. Used for initial scoring only. */
    public double score(int[] plain) {
        double s = 0.0;
        int end = plain.length - 3;
        for (int i = 0; i < end; i++) {
            s += logProbs[quadToInt(plain, i)];
        }
        return s;
    }

    /**
     * Score the single quadgram starting at position pos.
     * The caller must ensure pos >= 0 and pos + 3 < plain.length.
     * Used by delta scoring in the solver.
     */
    public double scoreAt(int[] plain, int pos) {
        return logProbs[quadToInt(plain, pos)];
    }

    public double getFloor() {
        return floor;
    }

    // ----------- helpers -----------

    private int quadToInt(String q) {
        return (q.charAt(0) - 'A') * 17576
             + (q.charAt(1) - 'A') * 676
             + (q.charAt(2) - 'A') * 26
             + (q.charAt(3) - 'A');
    }

    private int quadToInt(int[] plain, int i) {
        return plain[i]     * 17576
             + plain[i + 1] * 676
             + plain[i + 2] * 26
             + plain[i + 3];
    }

    /** Used by NGramScorer.logProb() */
    public double logProb(int quadId) {
        return logProbs[quadId];
    }
}
