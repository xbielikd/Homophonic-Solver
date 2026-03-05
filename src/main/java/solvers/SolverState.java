package solvers;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import helpers.PreprocessResult;

public class SolverState {

    public int[] ctIdx;                     // ID CT
    public int[] ptIdx;                     // ID PT (0..25)
    public int[] key;                       // cipher symbol -> plaintext letter
    public IntArrayList[] positionsOfCipher;
    public PreprocessResult prep;           // holds originalSymbol reverse map
    public double score;                    // current score

}