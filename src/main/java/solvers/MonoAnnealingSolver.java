package solvers;

import helpers.PreprocessResult;
import helpers.SimulatedAnnealing;
import scorers.NGramScorer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MonoAnnealingSolver {

    public static final int NUM_RESTARTS = 1000;
    public static final int STEPS = 500/*10000000*/;
    public static final int STARTING_TEMPERATURE = 5;

    private final PreprocessResult prep;
    private final NGramScorer scorer;

    public MonoAnnealingSolver(PreprocessResult prep, NGramScorer scorer) {
        this.prep = prep;
        this.scorer = scorer;
    }

    public SolverResult solve() {

        SolverState globalBest = null;

        for (int r = 0; r < NUM_RESTARTS; r++) {

            SolverState state = initRandomPermutationState();

            SimulatedAnnealing sa =
                    new SimulatedAnnealing(STARTING_TEMPERATURE, STEPS);

            while (sa.isHot()) {

                for (int i = 0; i < 26 - 1; i++) {
                    for (int j = i + 1; j < 26; j++) {

                        double oldScore = state.score;

                        applySwap(state, i, j);

                        if (!sa.acceptWithTemperature(state.score, oldScore)) {
                            applySwap(state, i, j); // undo
                        }
                    }
                }
            }
            hillClimb(state);

            if (globalBest == null || state.score > globalBest.score) {
                globalBest = state.copy();
                System.out.println("New best: " + globalBest.score);
            }
        }

        return new SolverResult(globalBest, decryptToString(globalBest));
    }

    public void hillClimb(SolverState s) {

        boolean improved;

        do {
            improved = false;

            for (int i = 0; i < 26 - 1; i++) {
                for (int j = i + 1; j < 26; j++) {

                    double oldScore = s.score;

                    applySwap(s, i, j);

                    if (s.score > oldScore) {
                        improved = true;
                    } else {
                        applySwap(s, i, j);
                    }
                }
            }

        } while (improved);
    }

    public SolverState initRandomPermutationState() {

        SolverState s = new SolverState();
        s.ctIdx = prep.ctIdx;
        s.scorer = scorer;

        s.key = new int[26];
        List<Integer> letters = new ArrayList<>();
        for (int i = 0; i < 26; i++) letters.add(i);
        Collections.shuffle(letters);

        for (int i = 0; i < 26; i++)
            s.key[i] = letters.get(i);

        s.ptIdx = new int[s.ctIdx.length];
        for (int i = 0; i < s.ctIdx.length; i++)
            s.ptIdx[i] = s.key[s.ctIdx[i]];

        s.score = scorer.score(s.ptIdx);

        return s;
    }

    void applyReassign(SolverState s, int cipherIndex, int newPlain) {

        s.key[cipherIndex] = newPlain;

        for (int pos : prep.positionsOfCipher[cipherIndex]) {
            s.ptIdx[pos] = newPlain;
        }

        s.score = scorer.score(s.ptIdx);
    }

    public void applySwap(SolverState s, int a, int b) {

        int tmp = s.key[a];
        s.key[a] = s.key[b];
        s.key[b] = tmp;

        for (int i = 0; i < s.ctIdx.length; i++) {
            int c = s.ctIdx[i];
            s.ptIdx[i] = s.key[c];
        }

        s.score = scorer.score(s.ptIdx);
    }



    public String decryptToString(SolverState s) {
        StringBuilder sb = new StringBuilder(s.ptIdx.length);
        for (int i = 0; i < s.ptIdx.length; i++) {
            sb.append((char) ('A' + s.ptIdx[i]));
        }
        return sb.toString();
    }


}
