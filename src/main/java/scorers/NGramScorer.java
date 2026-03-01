package scorers;

public class NGramScorer {
    public QuadGramScorer quad;
    public TriGramScorer tri;
    public BiGramScorer bi;
    public UniGramsScorer uni;

    public double score(int[] pt) {
        return 1.0*quad.score(pt) + 0.8*tri.score(pt) + 0.5*bi.score(pt) + 0.1*uni.score(pt);
//        return 2.0*quad.score(pt) + 1.0*tri.score(pt) + 0.5*bi.score(pt) + 0.25*uni.score(pt);
    }

    public double logProb(int n, int id) {
        if (n == 1) return uni.logProb(id);
        if (n == 2) return bi.logProb(id);
        if (n == 3) return tri.logProb(id);
        if (n == 4) return quad.logProb(id);
        return 0.0;
    }
}

