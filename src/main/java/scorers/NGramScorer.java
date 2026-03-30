package scorers;

public class NGramScorer {
    public QuadGramScorer quad;
    public TriGramScorer tri;
    public BiGramScorer bi;

    public double score(int[] pt) {
        return 0.7*quad.score(pt) + 0.2*tri.score(pt) + 0.1*bi.score(pt);
    }
    // toto  skusat az po tom co otestujem a vygenerujem grafy pre cisto 3gramy, 2 gramy

    // 7 2 1 uz je
    // 6 3 1
    // 0.8 0.15 0.05
    // 0.5 0.25 0.25





    public double scoreAt(int[] plain, int pos) {
        double s = 0.0;
        // quadgram: needs pos+3 < plain.length
        if (pos + 3 < plain.length) s += 0.7 * quad.scoreAt(plain, pos);
        // trigram: needs pos+2 < plain.length
        if (pos + 2 < plain.length) s += 0.2 * tri.scoreAt(plain, pos);
        // bigram: needs pos+1 < plain.length
        if (pos + 1 < plain.length) s += 0.1 * bi.scoreAt(plain, pos);
        return s;
    }

    public double logProb(int n, int id) {
        if (n == 2) return bi.logProb(id);
        if (n == 3) return tri.logProb(id);
        if (n == 4) return quad.logProb(id);
        return 0.0;
    }
}

