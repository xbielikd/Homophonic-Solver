package helpers;

import it.unimi.dsi.fastutil.ints.IntArrayList;

public class PreprocessResult {
    public int[] ctIdx;
    public IntArrayList[] positionsOfCipher;
    public int numCipherSymbols;

    /** Maps internal index (0..N-1) back to the original cipher symbol number.
     *  e.g. originalSymbol[3] = 45 means internal index 3 was original symbol 45. */
    public int[] originalSymbol;

    public PreprocessResult(int[] ctIdx, IntArrayList[] pos, int n, int[] originalSymbol) {
        this.ctIdx = ctIdx;
        this.positionsOfCipher = pos;
        this.numCipherSymbols = n;
        this.originalSymbol = originalSymbol;
    }
}