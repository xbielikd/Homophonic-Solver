package solvers;

import helpers.PreprocessResult;
import helpers.SimulatedAnnealing;
import scorers.NGramScorer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomophonicAnnealingSolver {
    // worked at monoalphabetic
//    public static final int NUM_RESTARTS = 1000;
    public static final int NUM_RESTARTS = 1500;

    // worked at monoalphabetic
//    public static final int STEPS = 500;
    public static final int STEPS = 4000;

    // worked at monoalphabetic
//    public static final int STARTING_TEMPERATURE = 5;
    public static final int STARTING_TEMPERATURE = 5;
    // 20 best i think

    private final PreprocessResult prep;
    private final NGramScorer scorer;

    public HomophonicAnnealingSolver(PreprocessResult prep, NGramScorer scorer) {
        this.prep = prep;
        this.scorer = scorer;
    }

    public SolverResult solve() {

        SolverState globalBest = null;

        for (int r = 0; r < NUM_RESTARTS; r++) {

            SolverState state;

            if (prep.numCipherSymbols == 26) {
                state = initRandomPermutationState();
            } else {
                state = initRandomHomophonicState();
            }

            SimulatedAnnealing sa =
                    new SimulatedAnnealing(STARTING_TEMPERATURE, STEPS);

//            while (sa.isHot()) {
//
//                for (int i = 0; i < 26 - 1; i++) {
//                    for (int j = i + 1; j < 26; j++) {
//
//                        double oldScore = state.score;
//
//                        applySwap(state, i, j);
//
//                        if (!sa.acceptWithTemperature(state.score, oldScore)) {
//                            applySwap(state, i, j); // undo
//                        }
//                    }
//                }
//            }
            while (sa.isHot()) {
                doMoveWithAnnealing(state, sa);
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

    public SolverState initRandomHomophonicState() {

        SolverState s = new SolverState();
        s.ctIdx = prep.ctIdx;
        s.scorer = scorer;

        int N = prep.numCipherSymbols;

        s.key = new int[N];

        // evenly divided plaintext letters
        for (int i = 0; i < N; i++) {
            s.key[i] = i % 26;
        }

        // mix mapping
        List<Integer> temp = new ArrayList<>();
        for (int i = 0; i < N; i++) temp.add(s.key[i]);
        Collections.shuffle(temp);

        for (int i = 0; i < N; i++) {
            s.key[i] = temp.get(i);
        }

        // create plaintext
        s.ptIdx = new int[s.ctIdx.length];
        for (int i = 0; i < s.ctIdx.length; i++) {
            s.ptIdx[i] = s.key[s.ctIdx[i]];
        }

        s.score = scorer.score(s.ptIdx);

        return s;
    }

    private void doMoveWithAnnealing(SolverState state, SimulatedAnnealing sa) {

        double oldScore = state.score;

        if (prep.numCipherSymbols == 26) {

            // MONO -> swap
            int a = randCipher();
            int b = randCipher();
            while (b == a) b = randCipher();

            applySwap(state, a, b);

            if (!sa.acceptWithTemperature(state.score, oldScore)) {
                applySwap(state, a, b);
            }

        } else {

            // HOMOPHONIC -> reassign

//            if (Math.random() < 0.025) {
//
//                // REASSIGN MOVE
//                int c = randCipher();
//                int oldPlain = state.key[c];
//                int newPlain = randPlain();
//
//                applyReassign(state, c, newPlain);
//
//                if (!sa.acceptWithTemperature(state.score, oldScore)) {
//                    applyReassign(state, c, oldPlain);
//                }

            double tRatio = sa.getTemperatureRatio();
            double reassignProb = 0.1 * tRatio;  // max 8%

            if (Math.random() < reassignProb) {
//            if (Math.random() < 0.9) {

                // TODO maybe stop doin rand positions
                int c = randCipher();

                int oldPlain = state.key[c];
                int newPlain = randPlain();

                if (oldPlain != newPlain) {

                    int[] counts = countAssignments(state);

                    // every pt letter has at least one symbol
                    if (counts[oldPlain] > 1) {

                        applyReassign(state, c, newPlain);

                        if (!sa.acceptWithTemperature(state.score, oldScore)) {
                            applyReassign(state, c, oldPlain);
                        }
                    }
                }
//            }
            } else {

                // SWAP MOVE
                int a = randCipher();
                int b = randCipher();
                while (b == a) b = randCipher();

                applySwap(state, a, b);

                if (!sa.acceptWithTemperature(state.score, oldScore)) {
                    applySwap(state, a, b);
                }
            }
        }
    }

    private void applyReassign(SolverState s, int cipherIndex, int newPlain) {

        s.key[cipherIndex] = newPlain;

        for (int pos : prep.positionsOfCipher[cipherIndex]) {
            s.ptIdx[pos] = newPlain;
        }

//        s.score = scorer.score(s.ptIdx);double baseScore = scorer.score(s.ptIdx);

        double baseScore = scorer.score(s.ptIdx);

        if (prep.numCipherSymbols > 26) {
            s.score = baseScore - homophonePenalty(s);
        } else {
            s.score = baseScore;
        }

    }

    private double homophonePenalty(SolverState s) {

        int[] counts = new int[26];

        for (int i = 0; i < s.key.length; i++) {
            counts[s.key[i]]++;
        }

        double penalty = 0.0;

        //  TODO MAYBE uncomment and leave penalty there
//        double ideal = (double) s.key.length / 26.0;

        for (int i = 0; i < 26; i++) {
            // SHOULDNT HAPPEN
            if (counts[i] == 0) {
                penalty += 3;   // TODO was 50
            }
            //  TODO MAYBE uncomment and leave penalty there
//            double diff = counts[i] - ideal;
//            penalty += diff * diff;
        }

        return penalty;
    }

    private int randCipher() {
        return (int)(Math.random() * prep.numCipherSymbols);
    }

    private int randPlain() {
        return (int)(Math.random() * 26);
    }

    public void applySwap(SolverState s, int a, int b) {

        int tmp = s.key[a];
        s.key[a] = s.key[b];
        s.key[b] = tmp;

        for (int i = 0; i < s.ctIdx.length; i++) {
            int c = s.ctIdx[i];
            s.ptIdx[i] = s.key[c];
        }

//        s.score = scorer.score(s.ptIdx);
        double baseScore = scorer.score(s.ptIdx);

        if (prep.numCipherSymbols > 26) {
            s.score = baseScore - homophonePenalty(s);
        } else {
            s.score = baseScore;
        }
    }


    private int[] countAssignments(SolverState s) {

        int[] counts = new int[26];

        for (int i = 0; i < s.key.length; i++) {
            counts[s.key[i]]++;
        }

        return counts;
    }


    public String decryptToString(SolverState s) {
        StringBuilder sb = new StringBuilder(s.ptIdx.length);
        for (int i = 0; i < s.ptIdx.length; i++) {
            sb.append((char) ('A' + s.ptIdx[i]));
        }
        return sb.toString();
    }


}


