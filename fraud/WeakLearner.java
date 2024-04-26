import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class WeakLearner {
    private final int dp;  // dimension predictor
    private final int vp;  // value predictor
    private final int sp;  // sign predictor

    // train the weak learner
    public WeakLearner(int[][] input, double[] weights, int[] labels) {
        if (input == null || weights == null || labels == null)
            throw new IllegalArgumentException("Null arguments not allowed");
        if (input.length != weights.length || input.length != labels.length)
            throw new IllegalArgumentException("Input lengths do not match");
        if (input.length == 0)
            throw new IllegalArgumentException("Input cannot be empty");
        if (input[0].length == 0)
            throw new IllegalArgumentException("Input dimensions cannot be zero");

        for (double weight : weights) {
            if (weight < 0)
                throw new IllegalArgumentException("Weights must be non-negative");
        }

        for (int label : labels) {
            if (label != 0 && label != 1)
                throw new IllegalArgumentException("Labels must be either 0 or 1");
        }

        int n = input.length;
        int k = input[0].length;

        double bestWeight = Double.NEGATIVE_INFINITY;
        int bestDp = -1;
        int bestVp = -1;
        int bestSp = -1;

        for (int d = 0; d < k; d++) {
            // Sort input based on dimension d
            int[][] sortedInput = new int[n][2];
            for (int i = 0; i < n; i++) {
                sortedInput[i][0] = input[i][d];
                sortedInput[i][1] = i;
            }
            java.util.Arrays.sort(sortedInput, (a, b) -> Integer.compare(a[0], b[0]));

            double weightSumPos = 0;
            double weightSumNeg = 0;
            for (int i = 0; i < n; i++) {
                if (labels[sortedInput[i][1]] == 1)
                    weightSumPos += weights[sortedInput[i][1]];
                else
                    weightSumNeg += weights[sortedInput[i][1]];
            }

            double posWeight = 0;
            double negWeight = 0;
            for (int i = 0; i < n; i++) {
                if (labels[sortedInput[i][1]] == 1)
                    posWeight += weights[sortedInput[i][1]];
                else
                    negWeight += weights[sortedInput[i][1]];

                double weightSum = posWeight + (weightSumNeg - negWeight);
                if (weightSum > bestWeight) {
                    bestWeight = weightSum;
                    bestDp = d;
                    bestVp = sortedInput[i][0];
                    bestSp = (posWeight > negWeight) ? 1 : 0;
                }
            }
        }

        dp = bestDp;
        vp = bestVp;
        sp = bestSp;
    }

    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {
        if (sample == null)
            throw new IllegalArgumentException("Null argument not allowed");
        if (sample.length != dp + 1)
            throw new IllegalArgumentException("Sample dimension does not match");

        if (sp == 0)
            return (sample[dp] <= vp) ? 1 : 0;
        else
            return (sample[dp] <= vp) ? 0 : 1;
    }

    // return the dimension the learner uses to separate the data
    public int dimensionPredictor() {
        return dp;
    }

    // return the value the learner uses to separate the data
    public int valuePredictor() {
        return vp;
    }

    // return the sign the learner uses to separate the data
    public int signPredictor() {
        return sp;
    }

    // unit testing (required)
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
