package solvers;

public class SolverResult {

    public SolverState state;
    public String plaintext;
    public double score;

    public SolverResult(SolverState s, String pt) {
        this.state = s;
        this.plaintext = pt;
        this.score = s.score;
    }

    public void printKey() {
        // Sort by original cipher symbol number for readable output
        int n = state.key.length;
        int[] origSymbols = state.prep.originalSymbol;

        // Build index array sorted by original symbol value
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) indices[i] = i;
        java.util.Arrays.sort(indices, (a, b) -> origSymbols[a] - origSymbols[b]);

        for (int idx : indices) {
            System.out.printf("%-6d -> %c%n", origSymbols[idx], (char)('A' + state.key[idx]));
        }
    }
}