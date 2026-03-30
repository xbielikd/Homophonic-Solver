package scorers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class QuintGramScorer {

    private static final int SIZE = 26 * 26 * 26 * 26 * 26; // 11,881,376

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
            if (gram.length() != 5) continue;
            long count = Long.parseLong(parts[1]);
            int idx = quintToInt(gram);
            logProbs[idx] = Math.log((double) count / total);
        }

        for (int i = 0; i < SIZE; i++) {
            if (logProbs[i] == Double.NEGATIVE_INFINITY) logProbs[i] = floor;
        }
    }

    public double score(int[] plain) {
        double s = 0.0;
        int end = plain.length - 4;
        for (int i = 0; i < end; i++) {
            s += logProbs[quintToInt(plain, i)];
        }
        return s;
    }

    public double scoreAt(int[] plain, int pos) {
        return logProbs[quintToInt(plain, pos)];
    }

    public double logProb(int id) {
        return logProbs[id];
    }

    public double getFloor() {
        return floor;
    }

    private int quintToInt(String q) {
        return (q.charAt(0) - 'A') * 456976   // 26^4
                + (q.charAt(1) - 'A') * 17576    // 26^3
                + (q.charAt(2) - 'A') * 676      // 26^2
                + (q.charAt(3) - 'A') * 26
                + (q.charAt(4) - 'A');
    }

    private int quintToInt(int[] plain, int i) {
        return plain[i]     * 456976
                + plain[i + 1] * 17576
                + plain[i + 2] * 676
                + plain[i + 3] * 26
                + plain[i + 4];
    }
}
//```
//
//One thing to be aware of — the flat array for quintagrams is `11,881,376` doubles = about **90 MB**. That's fine for most machines but just make sure your JVM heap is large enough. If you get `OutOfMemoryError` add this to your run configuration:
//        ```
//        -Xmx512m