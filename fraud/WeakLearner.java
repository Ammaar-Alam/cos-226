import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class WeakLearner {
    private final int dp;  // dimension predictor
    private final int vp;  // value predictor
    private final int sp;  // sign predictor

    public WeakLearner(int[][] input, double[] weights, int[] labels) {
        if (input == null || weights == null || labels == null)
            throw new IllegalArgumentException("Input arrays must not be null");
        if (input.length == 0)
            throw new IllegalArgumentException("Input data cannot be empty");
        if (input.length != weights.length || input.length != labels.length)
            throw new IllegalArgumentException("Input arrays must be of equal length");

        for (double weight : weights) {
            if (weight < 0)
                throw new IllegalArgumentException("Weights must be non-negative");
        }

        for (int label : labels) {
            if (label != 0 && label != 1)
                throw new IllegalArgumentException("Labels must be 0 or 1");
        }

        int n = input.length;
        int k = input[0].length;

        double bestWeightCorrect = Double.NEGATIVE_INFINITY;
        int bestDp = -1, bestVp = Integer.MAX_VALUE, bestSp = -1;

        for (int dimension = 0; dimension < k; dimension++) {
            for (int splitIndex = 0; splitIndex < n; splitIndex++) {
                int threshold = input[splitIndex][dimension];
                for (int sign = 0; sign <= 1; sign++) {
                    double weightCorrect = 0;

                    for (int i = 0; i < n; i++) {
                        int predicted = predictWith(input[i][dimension], threshold, sign);
                        if (predicted == labels[i]) {
                            weightCorrect += weights[i];
                        }
                    }

                    if (weightCorrect > bestWeightCorrect ||
                            (weightCorrect == bestWeightCorrect && (dimension < bestDp ||
                                    (dimension == bestDp && threshold < bestVp) ||
                                    (dimension == bestDp && threshold == bestVp
                                            && sign < bestSp)))) {
                        bestWeightCorrect = weightCorrect;
                        bestDp = dimension;
                        bestVp = threshold;
                        bestSp = sign;
                    }
                }
            }
        }

        this.dp = bestDp;
        this.vp = bestVp;
        this.sp = bestSp;
    }

    private int predictWith(int value, int threshold, int sign) {
        if (sign == 0) {
            return value > threshold ? 1 : 0;
        }
        else {
            return value <= threshold ? 1 : 0;
        }
    }

    public int predict(int[] sample) {
        if (sample == null || sample.length <= dp)
            throw new IllegalArgumentException("Invalid sample");
        return predictWith(sample[dp], vp, sp);
    }

    public int dimensionPredictor() {
        return dp;
    }

    public int valuePredictor() {
        return vp;
    }

    public int signPredictor() {
        return sp;
    }
    
    public static void main(String[] args) {
        In datafile = new In(args[0]);
        int n = datafile.readInt();
        int k = datafile.readInt();
        int[][] input = new int[n][k];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                input[i][j] = datafile.readInt();
            }
        }

        int[] labels = new int[n];
        for (int i = 0; i < n; i++) {
            labels[i] = datafile.readInt();
        }

        double[] weights = new double[n];
        for (int i = 0; i < n; i++) {
            weights[i] = datafile.readDouble();
        }

        WeakLearner weakLearner = new WeakLearner(input, weights, labels);
        StdOut.printf("vp = %d, dp = %d, sp = %d\n", weakLearner.valuePredictor(),
                      weakLearner.dimensionPredictor(), weakLearner.signPredictor());
    }
}
