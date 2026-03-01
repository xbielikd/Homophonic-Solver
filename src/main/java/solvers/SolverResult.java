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
        for (int c = 0; c < state.key.length; c++) {
            System.out.println(c + " -> " + (char)('A' + state.key[c]));
        }
    }
}
