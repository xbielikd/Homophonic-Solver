import parsers.CiphertextParser;
import helpers.PreprocessResult;
import helpers.Preprocessor;
import scorers.QuadGramScorer;
import solvers.HomophonicAnnealingSolver;
import solvers.SolverResult;

public class HomophonicSolverMain {

    public static void main(String[] args) throws Exception {

        // ======= CIPHERTEXT as integers =======
        // 31 homophones
//        String ct = "24 9 6 20 25 11 3 13 2 21 17 27 16 7 18 28 12 25 15 19 22 18 26 5 21 24 9 6 14 1 30 29 4 18 8 1 16 4 23 9 5 16 21 25 16 22 10 16 24 17 7 17 21 5 22 23 27 9 6 21 5 11 24 15 6 5 23 22 0 16 18 24 9 5 21 7 17 28 23 9 1 24 23 5 14 14 22 1 22 23 21 0 16 8 6 22 23 17 21 29 1 2 17 25 23 9 11 4 4 5 16 24 21 6 1 22 25 21 5 1 16 4 22 6 3 21 6 23 15 0 19 22 2 25 21 11 5 4 25 16 4 5 21 18 14 4 18 0 13 23 21 5 5 22";

        // 55
        String ct = "45 18 13 41 49 29 39 34 41 11 34 50 14 38 46 18 13 33 25 8 6 20 45 53 51 2 25 26 42 2 31 8 44 18 12 27 0 40 24 12 47 36 26 0 6 13 5 1 27 9 1 25 20 50 13 51 21 47 19 45 19 14 41 33 48 31 7 41 33 15 50 9 29 8 34 38 43 5 3 25 26 20 30 16 32 49 47 45 18 11 21 38 36 39 21 6 10 43 15 39 9 41 19 15 38 49 22 45 42 3 31 8 50 11 16 12 47 2 4 25 11 43 51 12 38 10 36 22 25 10 7 18 21 16 17 32 30 6 1 38 45 43 51 33 34 8 12 30 5 39 3 47 9 41 32 15 3 36 36 25 11 43 35 38 0 29 16 9 42 0 29 8 36 12 3 40 41 43 2 46 3 26 35 31 16 43 22 8 10 4 3 43 24 12 45 42 34 15 9 16 16 41 29 9 51 26 53 4 2 24 12 8 4 38 9 0 8 15 22 26 26 11 8 46 19 9 3 22 40 51 21 47 17 0 38 20 6 19 41 6 11 29 45 4 48 46 46 13 39 43 45 22 25 26 16 25 22 42 47 14 29 22 29 16 15 39 33 28 44 17 12 5 17 48 39 42 46 32 32 7 30 10 1 40 45 18 13 15 33 48 29 46 2 22 30 51 19 11 38 11 51 34 27 12 29 19 1 7 16 0 46 17 10 39 10 7 44 33 15 14 46 5 19 46 17 14 20 38 27 35 40 31 22 31 16 51 3 47 12 40 47 19 9 6 17 20 26 8 40 12 30 38 0 30 36 1 43 46 25 1 48 16 19 20 30 16 1 31 8 43 17 34 49 46 20 30 16 51 18 20 25 12 47 18 14 20 39 36 0 38 14 30 45 42 4 1 40 46 12 39 14 8 30 33 20 43 22 26 53 51 22 44 17 28 9 38 5 18 2 31 46 42 51 18 35 41 12 43 46 2 26 26 43 42 45 38 9 45 6 17 9 8 1 26 34 29 16 46 17 12 30 2 38 39 33 51 5 32 4 4 25 9 42 45 34 30 13 41 47 40 13 9 44 3 31 32 26 7 27 0 29 51 22 44 19 0 16 38 14 53 4 10 0 38 8 41 2 45 37 49 21 11 45 25 53 35 30 0 41 46 32 29 12 4 9 30 6 17 30 13 2 39 44 19 13 12 31 44 40 3 30 5 9 47 33 47 19 12 27 0 39 24 14 45 36 26 3 6 11 17 12 51 0 47 5 17 12 7 47 17 12 4 49 43 25 53 5 38 33 51 7 51 22 47 18 51 22 41 10 53 10 42 0 30 7 42 28 21 25 14 7 42 32 15 46 26 53";


        int[] rawCipher = CiphertextParser.parseCiphertext(ct);

        // ======= Preprocessing =======
        PreprocessResult prep = Preprocessor.preprocessCiphertext(rawCipher);
        System.out.println("Cipher symbols: " + prep.numCipherSymbols);
        System.out.println("Text length:    " + prep.ctIdx.length);

        // ======= Scorer — quadgrams only =======
        QuadGramScorer quad = new QuadGramScorer();
        quad.loadFromTxt("src/main/resources/quadgrams.txt");

        // ======= Solve =======
        HomophonicAnnealingSolver solver = new HomophonicAnnealingSolver(prep, quad);
        SolverResult result = solver.solve();

        // ======= Print =======
        System.out.println("\n=== BEST SCORE ===");
        System.out.println(result.score);

        System.out.println("\n=== PLAINTEXT ===");
        System.out.println(result.plaintext);

        System.out.println("\n=== KEY (cipher -> plain) ===");
        result.printKey();
    }
}
