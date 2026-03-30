package scorers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class BiGramScorer {

    private static final int SIZE = 26 * 26; // 676

    private double[] logProbs;
    private double floor;

    public void loadFromTxt(String path) throws Exception {

        logProbs = new double[SIZE];
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

        floor = Math.log(0.01 / total);

        for (String l : lines) {
            String[] parts = l.split("\\s+");
            String gram = parts[0].toUpperCase();
            if (gram.length() != 2) continue;
            long count = Long.parseLong(parts[1]);
            int idx = biToInt(gram);
            logProbs[idx] = Math.log((double) count / total);
        }

        for (int i = 0; i < SIZE; i++) {
            if (logProbs[i] == Double.NEGATIVE_INFINITY) logProbs[i] = floor;
        }
    }

    public double score(int[] plain) {
        double s = 0.0;
        for (int i = 0; i < plain.length - 1; i++) {
            s += logProbs[biToInt(plain, i)];
        }
        return s;
    }

    public double scoreAt(int[] plain, int pos) {
        return logProbs[plain[pos] * 26 + plain[pos + 1]];
    }

    public double logProb(int id) {
        return logProbs[id];
    }

    private int biToInt(String q) {
        return (q.charAt(0) - 'A') * 26
                + (q.charAt(1) - 'A');
    }

    private int biToInt(int[] plain, int i) {
        return plain[i] * 26
                + plain[i + 1];
    }
}