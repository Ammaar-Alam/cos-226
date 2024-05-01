import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.List;

public class BoostingAlgorithm {
    private final Clustering clustering;    // clustering object
    private final int[][] reducedInput;     // reduced input dimensions
    private final int[] labels;             // labels of the input
    private final double[] weights;         // weights of the input points
    private final List<WeakLearner> weakLearners;  // list of weak learners

    // create the clusters and initialize your data structures
    public BoostingAlgorithm(int[][] input, int[] labels, Point2D[] locations, int k) {
        if (input == null || labels == null || locations == null)
            throw new IllegalArgumentException("Null arguments not allowed");
        if (input.length != labels.length)
            throw new IllegalArgumentException("Input and labels lengths do not match");
        if (k < 1 || k > locations.length)
            throw new IllegalArgumentException("Invalid number of clusters");

        for (int label : labels) {
            if (label != 0 && label != 1)
                throw new IllegalArgumentException("Labels must be either 0 or 1");
        }

        int n = input.length;
        clustering = new Clustering(locations, k);
        reducedInput = new int[n][];
        for (int i = 0; i < n; i++) {
            reducedInput[i] = clustering.reduceDimensions(input[i]);
        }
        this.labels = labels.clone();  // create a defensive copy of labels
        weights = new double[n];
        for (int i = 0; i < n; i++) {
            weights[i] = 1.0 / n;
        }
        weakLearners = new ArrayList<>();
    }

    // return the current weight of the ith point
    public double weightOf(int i) {
        if (i < 0 || i >= weights.length)
            throw new IllegalArgumentException("Invalid index");
        return weights[i];
    }

    // apply one step of the boosting algorithm
    public void iterate() {
        WeakLearner learner = new WeakLearner(reducedInput, weights, labels);
        weakLearners.add(learner);

        double weightSum = 0;
        for (int i = 0; i < weights.length; i++) {
            if (learner.predict(reducedInput[i]) != labels[i])
                weights[i] *= 2;
            weightSum += weights[i];
        }

        for (int i = 0; i < weights.length; i++) {
            weights[i] /= weightSum;
        }
    }

    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {
        if (sample == null)
            throw new IllegalArgumentException("Null argument not allowed");

        int[] reducedSample = clustering.reduceDimensions(sample);
        int[] predictions = new int[weakLearners.size()];
        for (int i = 0; i < weakLearners.size(); i++) {
            predictions[i] = weakLearners.get(i).predict(reducedSample);
        }

        int zeroes = 0;
        int ones = 0;
        for (int prediction : predictions) {
            if (prediction == 0)
                zeroes++;
            else
                ones++;
        }

        if (ones >= zeroes)
            return 1;
        else
            return 0;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // read in the terms from a file
        DataSet training = new DataSet(args[0]);
        DataSet testing = new DataSet(args[1]);
        int k = Integer.parseInt(args[2]);
        int T = Integer.parseInt(args[3]);

        int[][] trainingInput = training.getInput();
        int[][] testingInput = testing.getInput();
        int[] trainingLabels = training.getLabels();
        int[] testingLabels = testing.getLabels();
        Point2D[] trainingLocations = training.getLocations();

        // stopwatch for readme
        Stopwatch timer = new Stopwatch();

        // train the model
        BoostingAlgorithm model = new BoostingAlgorithm(trainingInput, trainingLabels,
                                                        trainingLocations, k);
        for (int t = 0; t < T; t++)
            model.iterate();

        // calculate the training data set accuracy
        double training_accuracy = 0;
        for (int i = 0; i < training.getN(); i++)
            if (model.predict(trainingInput[i]) == trainingLabels[i])
                training_accuracy += 1;
        training_accuracy /= training.getN();

        // calculate the test data set accuracy
        double test_accuracy = 0;
        for (int i = 0; i < testing.getN(); i++)
            if (model.predict(testingInput[i]) == testingLabels[i])
                test_accuracy += 1;
        test_accuracy /= testing.getN();

        StdOut.println("Training accuracy of model: " + training_accuracy);
        StdOut.println("Test accuracy of model: " + test_accuracy);
        StdOut.println("Time elasped: " + timer.elapsedTime());
    }
}
