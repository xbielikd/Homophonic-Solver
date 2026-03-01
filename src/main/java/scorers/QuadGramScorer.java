package scorers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuadGramScorer {

    private final Map<Integer, Double> logProbs = new HashMap<>();
    private double floor;

    public void loadFromTxt(String path) throws Exception {

        BufferedReader br = new BufferedReader(new FileReader(path));

        long total = 0;
        List<String> lines = new ArrayList<>();

        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
            String[] parts = line.split("\\s+");
            total += Long.parseLong(parts[1]);
        }
        br.close();

        for (String l : lines) {
            String[] parts = l.split("\\s+");
            String quad = parts[0];
            long count = Long.parseLong(parts[1]);

            int key = quadToInt(quad);

            double p = (double) count / total;
            logProbs.put(key, Math.log(p));
        }

        floor = Math.log(0.01 / total);
    }

    public double score(int[] plain) {
        double s = 0.0;
        for (int i = 0; i < plain.length - 3; i++) {
            int key = quadToInt(plain, i);
            s += logProbs.getOrDefault(key, floor);
        }
        return s;
    }

    private int quadToInt(String q) {
        return (q.charAt(0) - 'A') * 26 * 26 * 26
                + (q.charAt(1) - 'A') * 26 * 26
                + (q.charAt(2) - 'A') * 26
                + (q.charAt(3) - 'A');
    }

    private int quadToInt(int[] plain, int i) {
        return plain[i]     * 26 * 26 * 26
                + plain[i + 1] * 26 * 26
                + plain[i + 2] * 26
                + plain[i + 3];
    }

    public double logProb(int quadId) {
        return logProbs.getOrDefault(quadId, floor);
    }

}
