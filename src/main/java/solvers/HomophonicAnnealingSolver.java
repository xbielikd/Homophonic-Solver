package solvers;

import helpers.PreprocessResult;
import helpers.SimulatedAnnealing;
import scorers.QuadGramScorer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HomophonicAnnealingSolver {

    // Tuning parameters
    public static final int    NUM_RESTARTS      = 30;
    public static final long   STEPS_PER_RESTART = 3_000_000L;
    public static final double START_TEMP        = 10.0;
    public static final double END_TEMP          = 0.001;
    public static final double MAX_REASSIGN_PROB = 0.15;

    private static final double[] ENGLISH_FREQ = {
            0.08167, 0.01492, 0.02782, 0.04253, 0.12702, 0.02228, 0.02015,
            0.06094, 0.06966, 0.00153, 0.00772, 0.04025, 0.02406, 0.06749,
            0.07507, 0.01929, 0.00095, 0.05987, 0.06327, 0.09056, 0.02758,
            0.00978, 0.02360, 0.00150, 0.01974, 0.00074
    };

    private final PreprocessResult prep;
    private final QuadGramScorer   quad;
    private final Random           rng     = new Random();
    private final int[]            ctIdx;
    private final int              textLen;
    private final int              N;

    // Reusable scratch buffers — avoids allocation in the hot inner loop.
    // visited[] is a dedup flag array (indexed by quadgram start position).
    // affected[] collects the unique start positions each move.
    private final boolean[] visited;
    private final int[]     affected;

    public HomophonicAnnealingSolver(PreprocessResult prep, QuadGramScorer quad) {
        this.prep    = prep;
        this.quad    = quad;
        this.ctIdx   = prep.ctIdx;
        this.textLen = prep.ctIdx.length;
        this.N       = prep.numCipherSymbols;
        this.visited = new boolean[textLen];
        this.affected = new int[textLen];
    }

    // Entry point

    public SolverResult solve() {

        int[]  bestKey   = null;
        int[]  bestPt    = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (int r = 0; r < NUM_RESTARTS; r++) {

            int[]  key   = (N == 26) ? initPermutation() : initFrequencyBased();
            int[]  pt    = applyKeyFull(key);
            double score = quad.score(pt);

            SimulatedAnnealing sa = new SimulatedAnnealing(START_TEMP, END_TEMP, STEPS_PER_RESTART);

            while (sa.isHot()) {
                if (N > 26 && rng.nextDouble() < MAX_REASSIGN_PROB * sa.getTemperatureRatio()) {
                    score = doReassign(key, pt, score, sa);
                } else {
                    score = doSwap(key, pt, score, sa);
                }
            }

            score = hillClimb(key, pt, score);
            score = fixRareSymbols(key, pt, score);  // ← add this


            if (score > bestScore) {
                bestScore = score;
                bestKey   = key.clone();
                bestPt    = pt.clone();
                System.out.printf("Restart %d — new best: %.4f%n", r, bestScore);
            }
        }

        return buildResult(bestKey, bestPt, bestScore);
    }

    // SWAP mov
    //
    // THE KEY CORRECTNESS RULE for delta scoring:
    //   1. Collect ALL affected quadgram start-indices (for both symbols, deduplicated).
    //   2. Score them ALL with the OLD pt[] (before any mutation).
    //   3. Apply the FULL mutation to pt[] atomically.
    //   4. Score the SAME indices with the NEW pt[].
    //   5. delta = newSum - oldSum.
    //
    // The old broken version did: subtract-a, subtract-b, mutate-a, mutate-b,
    // add-a, add-b — which both double-counts overlapping quadgrams AND reads
    // mixed old/new state when computing "new" scores for b after mutating a.
    //
    private double doSwap(int[] key, int[] pt, double score, SimulatedAnnealing sa) {

        int a = rng.nextInt(N);
        int b = rng.nextInt(N);
        while (b == a) b = rng.nextInt(N);

        int letterA = key[a];
        int letterB = key[b];
        if (letterA == letterB) return score;

        // 1. collect affected quadgram start indices (deduplicated)
        int n = collectAffected(a, b);

        // 2. old scores
        double oldSum = 0.0;
        for (int i = 0; i < n; i++) oldSum += quad.scoreAt(pt, affected[i]);

        // 3. mutate pt[] atomically
        for (int pos : prep.positionsOfCipher[a]) pt[pos] = letterB;
        for (int pos : prep.positionsOfCipher[b]) pt[pos] = letterA;

        // 4. new scores over exact same indices
        double newSum = 0.0;
        for (int i = 0; i < n; i++) newSum += quad.scoreAt(pt, affected[i]);

        double newScore = score + (newSum - oldSum);

        if (sa.accept(newScore, score)) {
            key[a] = letterB;
            key[b] = letterA;
            return newScore;
        } else {
            // undo
            for (int pos : prep.positionsOfCipher[a]) pt[pos] = letterA;
            for (int pos : prep.positionsOfCipher[b]) pt[pos] = letterB;
            return score;
        }
    }

    // REASSIGN move

    private double doReassign(int[] key, int[] pt, double score, SimulatedAnnealing sa) {

        int c         = rng.nextInt(N);
        int oldLetter = key[c];
        int newLetter = rng.nextInt(26);
        if (oldLetter == newLetter) return score;

        // Only protect oldLetter from losing its last symbol if it actually
        // has ciphertext positions. A letter with 0 occurrences in the text
        // (e.g. X, Z, J not present in plaintext) should never block reassignment.
        if (countForLetter(key, oldLetter) <= 1 && letterHasCiphertextPositions(key, oldLetter)) return score;

        int n = collectAffectedSingle(c);

        double oldSum = 0.0;
        for (int i = 0; i < n; i++) oldSum += quad.scoreAt(pt, affected[i]);

        for (int pos : prep.positionsOfCipher[c]) pt[pos] = newLetter;

        double newSum = 0.0;
        for (int i = 0; i < n; i++) newSum += quad.scoreAt(pt, affected[i]);

        double newScore = score + (newSum - oldSum);

        if (sa.accept(newScore, score)) {
            key[c] = newLetter;
            return newScore;
        } else {
            for (int pos : prep.positionsOfCipher[c]) pt[pos] = oldLetter;
            return score;
        }
    }

    // Hill climb

    private double hillClimb(int[] key, int[] pt, double score) {

        boolean improved;
        do {
            improved = false;

            for (int a = 0; a < N - 1; a++) {
                for (int b = a + 1; b < N; b++) {

                    int letterA = key[a];
                    int letterB = key[b];
                    if (letterA == letterB) continue;

                    int n = collectAffected(a, b);

                    double oldSum = 0.0;
                    for (int i = 0; i < n; i++) oldSum += quad.scoreAt(pt, affected[i]);

                    for (int pos : prep.positionsOfCipher[a]) pt[pos] = letterB;
                    for (int pos : prep.positionsOfCipher[b]) pt[pos] = letterA;

                    double newSum = 0.0;
                    for (int i = 0; i < n; i++) newSum += quad.scoreAt(pt, affected[i]);

                    double delta = newSum - oldSum;

                    if (delta > 0.0) {
                        key[a] = letterB;
                        key[b] = letterA;
                        score += delta;
                        improved = true;
                    } else {
                        for (int pos : prep.positionsOfCipher[a]) pt[pos] = letterA;
                        for (int pos : prep.positionsOfCipher[b]) pt[pos] = letterB;
                    }
                }
            }
        } while (improved);

        return score;
    }

    // Affected-quadgram index collection
    //
    // A quadgram starting at index q covers positions q, q+1, q+2, q+3.
    // Therefore, changing position p affects quadgrams starting at
    // max(0, p-3) through min(textLen-4, p).
    //
    // We collect these start-indices for all positions of the given symbols,
    // deduplicating via the visited[] boolean array.
    // We clear only the entries we set (not the whole array) so this stays O(k).

    private int collectAffected(int symA, int symB) {
        int count = 0;

        for (int pos : prep.positionsOfCipher[symA]) {
            int lo = Math.max(0, pos - 3);
            int hi = Math.min(textLen - 4, pos);
            for (int q = lo; q <= hi; q++) {
                if (!visited[q]) { visited[q] = true; affected[count++] = q; }
            }
        }
        for (int pos : prep.positionsOfCipher[symB]) {
            int lo = Math.max(0, pos - 3);
            int hi = Math.min(textLen - 4, pos);
            for (int q = lo; q <= hi; q++) {
                if (!visited[q]) { visited[q] = true; affected[count++] = q; }
            }
        }

        // clear only the flags we set
        for (int i = 0; i < count; i++) visited[affected[i]] = false;
        return count;
    }

    private int collectAffectedSingle(int sym) {
        int count = 0;

        for (int pos : prep.positionsOfCipher[sym]) {
            int lo = Math.max(0, pos - 3);
            int hi = Math.min(textLen - 4, pos);
            for (int q = lo; q <= hi; q++) {
                if (!visited[q]) { visited[q] = true; affected[count++] = q; }
            }
        }

        for (int i = 0; i < count; i++) visited[affected[i]] = false;
        return count;
    }

    // ── Initialization ────────────────────────────────────────────────────────

    private int[] initPermutation() {
        List<Integer> letters = new ArrayList<>();
        for (int i = 0; i < 26; i++) letters.add(i);
        Collections.shuffle(letters, rng);
        int[] key = new int[26];
        for (int i = 0; i < 26; i++) key[i] = letters.get(i);
        return key;
    }

    /**
     * Assign cipher symbols to plaintext letters proportional to English
     * letter frequencies. The most frequent cipher symbols go to the most
     * frequent letters, then shuffle within each group for restart diversity.
     */
    private int[] initFrequencyBased() {

        double[] cipherFreq = new double[N];
        for (int sym : ctIdx) cipherFreq[sym]++;
        for (int i = 0; i < N; i++) cipherFreq[i] /= textLen;

        Integer[] sortedSyms = new Integer[N];
        for (int i = 0; i < N; i++) sortedSyms[i] = i;
        Arrays.sort(sortedSyms, (x, y) -> Double.compare(cipherFreq[y], cipherFreq[x]));

        Integer[] sortedLetters = new Integer[26];
        for (int i = 0; i < 26; i++) sortedLetters[i] = i;
        Arrays.sort(sortedLetters, (x, y) -> Double.compare(ENGLISH_FREQ[y], ENGLISH_FREQ[x]));

        int[] alloc = computeAllocation(N, sortedLetters);

        int[] key = new int[N];
        int symIdx = 0;
        List<Integer> group = new ArrayList<>();

        for (int li = 0; li < 26; li++) {
            int letter = sortedLetters[li];
            int count  = alloc[li]; // 0 is valid — rare letters (X, Z, J, Q) get no symbols

            if (count == 0) continue; // this letter doesn't appear in the text — skip it

            group.clear();
            for (int k = 0; k < count && symIdx < N; k++) group.add(sortedSyms[symIdx++]);

            Collections.shuffle(group, rng);
            for (int sym : group) key[sym] = letter;
        }

        // handle any overflow
        for (; symIdx < N; symIdx++) key[sortedSyms[symIdx]] = rng.nextInt(26);

        return key;
    }

    private int[] computeAllocation(int total, Integer[] sortedLetters) {
        double freqSum = 0;
        for (double f : ENGLISH_FREQ) freqSum += f;

        int[] alloc   = new int[26];
        int   assigned = 0;
        for (int i = 0; i < 26; i++) {
            alloc[i]  = (int)(ENGLISH_FREQ[sortedLetters[i]] / freqSum * total);
            assigned += alloc[i];
        }
        for (int i = 0; assigned < total; i = (i + 1) % 26) { alloc[i]++; assigned++; }
        return alloc;
    }

    // Utility

    private int[] applyKeyFull(int[] key) {
        int[] pt = new int[textLen];
        for (int i = 0; i < textLen; i++) pt[i] = key[ctIdx[i]];
        return pt;
    }

    private int countForLetter(int[] key, int letter) {
        int c = 0;
        for (int k : key) if (k == letter) c++;
        return c;
    }

    private SolverResult buildResult(int[] key, int[] pt, double score) {
        SolverState s = new SolverState();
        s.key   = key;
        s.ptIdx = pt;
        s.ctIdx = ctIdx;
        s.score = score;
        s.prep  = prep;  // needed for printKey() to access originalSymbol map

        StringBuilder sb = new StringBuilder(textLen);
        for (int v : pt) sb.append((char)('A' + v));
        return new SolverResult(s, sb.toString());
    }

    // After hill climb, for any cipher symbol appearing <= RARE_THRESHOLD times,
    // try all 26 letter assignments and keep the best
    private double fixRareSymbols(int[] key, int[] pt, double score) {
        for (int c = 0; c < N; c++) {
            if (prep.positionsOfCipher[c].size() <= 3) {
                int bestLetter = key[c];
                double bestScore = score;
                int oldLetter = key[c];

                for (int letter = 0; letter < 26; letter++) {
                    if (letter == oldLetter) continue;
                    // don't strip any letter of its last symbol unless it has no ciphertext positions
                    if (countForLetter(key, oldLetter) <= 1 && letterHasCiphertextPositions(key, oldLetter)) continue;

                    // try reassigning c -> letter
                    key[c] = letter;
                    for (int pos : prep.positionsOfCipher[c]) pt[pos] = letter;
                    double s = quad.score(pt);  // full rescore — only done once per rare symbol
                    if (s > bestScore) {
                        bestScore = s;
                        bestLetter = letter;
                    }
                    // undo
                    key[c] = oldLetter;
                    for (int pos : prep.positionsOfCipher[c]) pt[pos] = oldLetter;
                }

                // apply best
                if (bestLetter != oldLetter) {
                    key[c] = bestLetter;
                    for (int pos : prep.positionsOfCipher[c]) pt[pos] = bestLetter;
                    score = bestScore;
                }
            }
        }
        return score;
    }

    /**
     * Returns true if at least one cipher symbol currently mapped to `letter`
     * actually appears in the ciphertext (i.e. has >= 1 position).
     * Used to decide whether stripping a letter of its last symbol is safe:
     * - If the letter has ciphertext positions → protect it (decryption depends on it)
     * - If the letter has NO ciphertext positions → it's a "ghost" letter (like X or Z
     *   when they don't appear in the plaintext) → freely reassignable
     */
    private boolean letterHasCiphertextPositions(int[] key, int letter) {
        for (int sym = 0; sym < N; sym++) {
            if (key[sym] == letter && prep.positionsOfCipher[sym].size() > 0) {
                return true;
            }
        }
        return false;
    }

}