package helpers;

import it.unimi.dsi.fastutil.ints.IntArrayList;

public class PreprocessResult {
    public int[] ctIdx;
    public IntArrayList[] positionsOfCipher;
    public int numCipherSymbols;

    public PreprocessResult(int[] ctIdx, IntArrayList[] pos, int n) {
        this.ctIdx = ctIdx;
        this.positionsOfCipher = pos;
        this.numCipherSymbols = n;
    }
}
