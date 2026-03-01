package solvers;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import scorers.NGramScorer;

public class SolverState {

    public int[] ctIdx;                     // ID CT
    public int[] ptIdx;                     // ID PT (0..25)
    public int[] key;                       // cipher symbol -> plaintext letter
    public IntArrayList[] positionsOfCipher;

    public double score;                    // current score

    public NGramScorer scorer;              // contains uni, bi, tri, quad

    public SolverState copy() {
        SolverState s = new SolverState();
        s.ctIdx = this.ctIdx;
        s.positionsOfCipher = this.positionsOfCipher;
        s.scorer = this.scorer;

        s.ptIdx = this.ptIdx.clone();
        s.key = this.key.clone();
        s.score = this.score;
        return s;
    }

}
