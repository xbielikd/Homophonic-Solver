import parsers.CiphertextParser;
import helpers.PreprocessResult;
import helpers.Preprocessor;
import scorers.*;
import solvers.HomophonicAnnealingSolver;
import solvers.SolverResult;
import solvers.MonoAnnealingSolver;

public class HomophonicSolverMain {

    public static void main(String[] args) throws Exception {

        // ======= CIPHERTEXT as integers =======
        // mono ct = 26 homophones -------> zvladne to (pri 0.0 reassign, aj pri 0.15, na tom asi nezalezi pri mono), teplota 20 asi
//        String ct = "19 7 4 16 20 8 2 10 1 17 14 22 13 5 14 23 9 20 12 15 18 14 21 4 17 19 7 4 11 0 25 24 3 14 6 0 13 3 19 7 4 13 17 20 13 18 8 13 19 14 5 14 17 4 18 19 22 7 4 17 4 8 19 12 4 4 19 18 0 13 14 19 7 4 17 5 14 23 19 7 0 19 19 4 11 11 18 0 18 19 17 0 13 6 4 18 19 14 17 24 0 1 14 20 19 7 8 3 3 4 13 19 17 4 0 18 20 17 4 0 13 3 18 4 2 17 4 19 12 0 15 18 1 20 17 8 4 3 20 13 3 4 17 14 11 3 14 0 10 19 17 4 4 18";

        // homophonic ct = 27 homophones ------> zvladlo to pri 0.15 reassing, teplota 20
//        String ct = "20 8 4 17 21 9 2 11 1 18 15 23 14 6 15 24 10 21 13 16 19 15 22 5 18 20 8 4 12 0 26 25 3 15 7 0 14 3 20 8 4 14 18 21 14 19 9 14 20 15 6 15 18 5 19 20 23 8 4 18 4 9 20 13 4 5 20 19 0 14 15 20 8 5 18 6 15 24 20 8 0 20 20 5 12 12 19 0 19 20 18 0 14 7 5 19 20 15 18 25 0 1 15 21 20 8 9 3 3 5 14 20 18 5 0 19 21 18 4 0 14 3 19 4 2 18 4 20 13 0 16 19 1 21 18 9 5 3 21 14 3 4 18 15 12 3 15 0 11 20 18 4 4 19";

        // 28 (a 2, e 2) ----------> zvladne to pri reassign 0.5, teplota 20
//        String ct = "21 9 5 18 22 10 3 12 2 19 16 24 15 7 16 25 11 22 14 17 20 16 23 6 19 21 9 5 13 1 27 26 4 16 8 0 15 4 21 9 6 15 19 22 15 20 10 15 21 16 7 16 19 5 20 21 24 9 5 19 5 10 21 14 5 5 21 20 1 15 16 21 9 6 19 7 16 25 21 9 0 21 21 5 13 13 20 1 20 21 19 1 15 8 5 20 21 16 19 26 0 2 16 22 21 9 10 4 4 6 15 21 19 6 0 20 22 19 5 0 15 4 20 5 3 19 6 21 14 1 17 20 2 22 19 10 6 4 22 15 4 6 19 16 13 4 16 1 12 21 19 6 5 20";

        // 29 96.75%, teplota 20
//        String ct = "22 9 6 19 23 10 3 12 2 20 16 25 15 7 17 26 11 23 14 18 21 16 24 6 20 22 9 5 13 0 28 27 4 16 8 1 15 4 22 9 6 15 20 23 15 21 10 15 22 16 7 16 20 6 21 22 25 9 6 20 6 10 22 14 6 5 22 21 0 15 17 22 9 5 20 7 16 26 22 9 1 22 22 6 13 13 21 0 21 22 20 1 15 8 5 21 22 17 20 27 1 2 16 23 22 9 10 4 4 5 15 22 20 5 0 21 23 20 6 1 15 4 21 6 3 20 5 22 14 0 18 21 2 23 20 10 5 4 23 15 4 6 20 16 13 4 17 1 12 22 20 5 6 21";

        // 30
//        String ct = "22 9 5 19 24 10 3 12 2 20 16 26 15 7 17 27 11 24 14 18 21 16 25 5 20 23 9 6 13 0 29 28 4 17 8 0 15 4 22 9 5 15 20 24 15 21 10 15 22 16 7 17 20 6 21 23 26 9 5 20 6 10 23 14 6 5 22 21 1 15 16 23 9 6 20 7 16 27 22 9 0 23 22 6 13 13 21 0 21 22 20 0 15 8 5 21 22 17 20 28 0 2 16 24 23 9 10 4 4 6 15 22 20 5 0 21 24 20 5 0 15 4 21 5 3 20 6 22 14 1 18 21 2 24 20 10 6 4 24 15 4 6 20 17 13 4 16 0 12 23 20 6 5 21 \n";

        // 31
        String ct = "24 9 6 20 25 11 3 13 2 21 17 27 16 7 18 28 12 25 15 19 22 18 26 5 21 24 9 6 14 1 30 29 4 18 8 1 16 4 23 9 5 16 21 25 16 22 10 16 24 17 7 17 21 5 22 23 27 9 6 21 5 11 24 15 6 5 23 22 0 16 18 24 9 5 21 7 17 28 23 9 1 24 23 5 14 14 22 1 22 23 21 0 16 8 6 22 23 17 21 29 1 2 17 25 23 9 11 4 4 5 16 24 21 6 1 22 25 21 5 1 16 4 22 6 3 21 6 23 15 0 19 22 2 25 21 11 5 4 25 16 4 5 21 18 14 4 18 0 13 23 21 5 5 22";



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
        scorer.uni.loadFromTxt("src/main/resources/unigrams.txt");
        scorer.bi.loadFromTxt("src/main/resources/bigrams.txt");
        scorer.tri.loadFromTxt("src/main/resources/trigrams.txt");
        scorer.quad.loadFromTxt("src/main/resources/quadgrams.txt");

        HomophonicAnnealingSolver homophonicAnnealingSolver = new HomophonicAnnealingSolver(prep, scorer);
        SolverResult result = homophonicAnnealingSolver.solve();


        // ======= Print =======
        System.out.println("=== BEST SCORE ===");
        System.out.println(result.score);

        System.out.println("=== PLAINTEXT ===");
        System.out.println(result.plaintext);

        System.out.println("=== KEY (cipher -> plain) ===");
        result.printKey();
    }
}
