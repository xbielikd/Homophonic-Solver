package scorers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UniGramsScorer {

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

            int key = biToInt(quad);

            double p = (double) count / total;
            logProbs.put(key, Math.log(p));
        }

        floor = Math.log(0.01 / total);
    }

    public double score(int[] plain) {
        double s = 0.0;
        for (int i = 0; i < plain.length - 1; i++) {
            int key = biToInt(plain, i);
            s += logProbs.getOrDefault(key, floor);
        }
        return s;
    }

    private int biToInt(String q) {
        return (q.charAt(0) - 'A');
    }

    private int biToInt(int[] plain, int i) {
        return plain[i];
    }

    public double logProb(int quadId) {
        return logProbs.getOrDefault(quadId, floor);
    }

}

