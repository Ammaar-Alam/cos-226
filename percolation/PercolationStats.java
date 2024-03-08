import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;


public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;

    private final double[] thresholds; // keeps track of the results
    private final int trials; // number of trials
    private double mean; // the mean lol
    private double stdDev; // the standard deviation lol


    // perform trials on a n by b grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials must be greater than 0");
        }
        this.trials = trials;
        thresholds = new double[trials];
        for (int trial = 0; trial < trials; trial++) {
            Percolation percolation = new Percolation(n);
            int openedSites = 0;
            while (!percolation.percolates()) {
                int row = StdRandom.uniformInt(n);
                int col = StdRandom.uniformInt(n);
                if (!percolation.isOpen(row, col)) {
                    percolation.open(row, col);
                    openedSites++;
                }
            }
            thresholds[trial] = (double) openedSites / (n * n);
        }
        mean = StdStats.mean(thresholds);
        stdDev = StdStats.stddev(thresholds);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stdDev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean - (CONFIDENCE_95 * stdDev / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean + (CONFIDENCE_95 * stdDev / Math.sqrt(trials));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        Stopwatch timer = new Stopwatch();
        PercolationStats trialStats = new PercolationStats(n, trials);

        StdOut.println("mean()           = " + trialStats.mean());
        StdOut.println("stddev()         = " + trialStats.stddev());
        StdOut.println("confidenceLow()  = " + trialStats.confidenceLow());
        StdOut.println("confidenceHigh() = " + trialStats.confidenceHigh());
        StdOut.println("elapsed time     = " + timer.elapsedTime());
    }

}
