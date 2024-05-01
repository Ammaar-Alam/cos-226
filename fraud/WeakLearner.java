import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class WeakLearner {
    private final int dp;  // dimension predictor
    private final int vp;  // value predictor
    private final int sp;  // sign predictor
    private final int k;   // number of clusters

    // train the weak learner
    public WeakLearner(int[][] input, double[] weights, int[] labels) {
        if (input == null || weights == null || labels == null ||
                input.length != weights.length
                || input.length != labels.length)
            throw new IllegalArgumentException("input arrays cant be null and "
                                                       + "must be equal length");
        for (double weight : weights) {
            if (weight < 0)
                throw new IllegalArgumentException("weights cant be negative");
        }
        for (int label : labels) {
            if (label != 0 && label != 1)
                throw new IllegalArgumentException("lavles must be 0 or 1");
        }

        int n = input.length;
        k = input[0].length;

        double bestsumWeight = Double.NEGATIVE_INFINITY;
        int bestDp = -1, bestVp = Integer.MAX_VALUE, bestSp = -1;

        for (int d = 0; d < k; d++) {
            IndexValue[] indexValue = new IndexValue[n];
            for (int i = 0; i < n; i++) {
                indexValue[i] = new IndexValue(i, input[i][d]);
            }
            Arrays.sort(indexValue);

            for (int sign = 0; sign <= 1; sign++) {
                double sumWeight = 0.0;

                for (int i = 0; i < n; i++) {
                    int index = indexValue[i].index;
                    if (labels[index] != sign) sumWeight += weights[index];
                }

                for (int i = 0; i < n; i++) {
                    int index = indexValue[i].index;
                    double weight = weights[index];

                    if (labels[index] == sign) sumWeight += weight;
                    else sumWeight -= weight;

                    if ((i + 1 < n) && (indexValue[i + 1].val
                            == indexValue[i].val)) continue;

                    if (sumWeight > bestsumWeight ||
                            (sumWeight == bestsumWeight && (d < bestDp ||
                                    (d == bestDp && indexValue[i].val < bestVp) ||
                                    (d == bestDp && indexValue[i].val == bestVp
                                            && sign < bestSp)))) {
                        bestsumWeight = sumWeight;
                        bestDp = d;
                        bestVp = indexValue[i].val;
                        bestSp = sign;
                    }
                }
            }
        }

        this.dp = bestDp;
        this.vp = bestVp;
        this.sp = bestSp;
    }

    private static class IndexValue implements Comparable<IndexValue> {
        // represents the index of the value in the input array
        private final int index;
        // represents the value in the input array
        private final int val;

        // initializes index and value
        public IndexValue(int index, int val) {
            this.index = index;
            this.val = val;
        }

        // compares the values between two IndexValue and returns an
        // int based on the result of the compare method
        public int compareTo(IndexValue that) {
            return Integer.compare(this.val, that.val);
        }
    }

    // predicts the label based on the value, threshold, and sign
    private int predictWith(int value, int threshold, int sign) {
        if (sign == 0) {
            return value <= threshold ? 0 : 1; // pls just lemme use these for once :(
        }
        else {
            return value <= threshold ? 1 : 0; // pls i beg
        }
    }

    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {
        if (sample == null || sample.length != k)
            throw new IllegalArgumentException("invalid smaple");
        return predictWith(sample[dp], vp, sp);
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
