import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class WeakLearner {
    private final int dp; // dimension predictor
    private final int vp; // value predictor
    private final int sp; // sign predictor

    public WeakLearner(int[][] input, double[] weights, int[] labels) {
        // Initial sanity checks would remain the same.

        int bestDp = -1;
        int bestVp = -1;
        int bestSp = -1;
        double bestError = Double.MAX_VALUE; // Optimizing to minimize error.

        for (int d = 0; d < input[0].length; d++) {
            int[][] sortedInput = new int[input.length][2];
            for (int i = 0; i < input.length; i++) {
                sortedInput[i][0] = input[i][d];
                sortedInput[i][1] = i; // Storing indices for weights alignment.
            }

            java.util.Arrays.sort(sortedInput, (a, b) -> Integer.compare(a[0], b[0]));

            for (int i = 0; i <= input.length; i++) {
                for (int sign = 0; sign <= 1; sign++) {
                    double error = calculateError(input, weights, labels, d,
                                                  i < input.length ? sortedInput[i][0] :
                                                  Integer.MAX_VALUE, sign);
                    if (error < bestError) {
                        bestError = error;
                        bestDp = d;
                        bestVp = i < input.length ? sortedInput[i][0] : Integer.MAX_VALUE;
                        bestSp = sign;
                    }
                }
            }
        }

        dp = bestDp;
        vp = bestVp;
        sp = bestSp;
    }

    private double calculateError(int[][] input, double[] weights, int[] labels, int dimension,
                                  int value, int sign) {
        double error = 0;
        for (int i = 0; i < input.length; i++) {
            int predicted = predictWithDimension(input[i][dimension], value, sign);
            if (predicted != labels[i]) {
                error += weights[i]; // Accumulate error based on weights
            }
        }
        return error;
    }

    // Helper method to predict with fixed dimension, value, and sign, not affecting class fields
    private int predictWithDimension(int dimensionValue, int value, int sign) {
        if (sign == 0)
            return (dimensionValue <= value) ? 1 : 0;
        else
            return (dimensionValue > value) ? 1 : 0;
    }

    public int predict(int[] sample) {
        if (sample == null) throw new IllegalArgumentException("Null argument not allowed");
        if (sample.length <= dp)
            throw new IllegalArgumentException("Sample dimension is too small");
        return predictWithDimension(sample[dp], vp, sp);
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
