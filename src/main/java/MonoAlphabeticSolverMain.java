import parsers.CiphertextParser;
import helpers.PreprocessResult;
import helpers.Preprocessor;
import scorers.*;
import solvers.SolverResult;
import solvers.MonoAnnealingSolver;

public class MonoAlphabeticSolverMain {

    public static void main(String[] args) throws Exception {

        // ======= CIPHERTEXT as integers =======
//        String ct = "19 7 4 16 20 8 2 10 1 17 14 22 13 5 14 23 9 20 12 15 18 14 21 4 17 19 7 4 11 0 25 24 3 14 6 0 13 3 19 7 4 13 17 20 13 18 0 6 0 8 13 19 14 2 7 4 2 10 0 11 11 19 7 4 11 4 19 19 4 17 5 17 4 16 20 4 13 2 8 4 18";
        String ct = "19 7 4 16 20 8 2 10 1 17 14 22 13 5 14 23 9 20 12 15 18 14 21 4 17 19 7 4 11 0 25 24 3 14 6 0 13 3 19 7 4 13 17 20 13 18 8 13 19 14 5 14 17 4 18 19 22 7 4 17 4 8 19 12 4 4 19 18 0 13 14 19 7 4 17 5 14 23 19 7 0 19 19 4 11 11 18 0 18 19 17 0 13 6 4 18 19 14 17 24 0 1 14 20 19 7 8 3 3 4 13 19 17 4 0 18 20 17 4 0 13 3 18 4 2 17 4 19 12 0 15 18 1 20 17 8 4 3 20 13 3 4 17 14 11 3 14 0 10 19 17 4 4 18";
        int[] rawCipher = CiphertextParser.parseCiphertext(ct);


        // ======= Preprocessing =======
        PreprocessResult prep = Preprocessor.preprocessCiphertext(rawCipher);

        // ======= Solver =======
        NGramScorer scorer = new NGramScorer();
        scorer.uni = new UniGramsScorer();
        scorer.bi = new BiGramScorer();
        scorer.tri = new TriGramScorer();
        scorer.quad = new QuadGramScorer();

        // load grams
        scorer.uni.loadFromTxt("C:\\Users\\Dominik\\Desktop\\FEI STUBA\\9_semester\\DP\\odznova\\src\\main\\resources\\unigrams.txt");
        scorer.bi.loadFromTxt("C:\\Users\\Dominik\\Desktop\\FEI STUBA\\9_semester\\DP\\odznova\\src\\main\\resources\\bigrams.txt");
        scorer.tri.loadFromTxt("C:\\Users\\Dominik\\Desktop\\FEI STUBA\\9_semester\\DP\\odznova\\src\\main\\resources\\trigrams.txt");
        scorer.quad.loadFromTxt("C:\\Users\\Dominik\\Desktop\\FEI STUBA\\9_semester\\DP\\odznova\\src\\main\\resources\\quadgrams.txt");

        MonoAnnealingSolver monoAnnealingSolver = new MonoAnnealingSolver(prep, scorer);
        SolverResult result = monoAnnealingSolver.solve();


        // ======= Print =======
        System.out.println("=== BEST SCORE ===");
        System.out.println(result.score);

        System.out.println("=== PLAINTEXT ===");
        System.out.println(result.plaintext);

        System.out.println("=== KEY (cipher -> plain) ===");
        result.printKey();
    }
}
