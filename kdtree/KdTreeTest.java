import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StopwatchCPU;

public class KdTreeTest {
    public static void main(String[] args) {
        int trials = 1; // Number of test iterations
        int M = 1000000; // Number of operations per iteration
        String filename = args[0];

        double cumulativeNearestTime = 0;
        double cumulativePutTime = 0;
        double cumulativeContainsTime = 0;
        double cumulativeRangeTime = 0;

        for (int iter = 0; iter < trials; iter++) {
            In in = new In(filename);
            KdTreeST<Integer> kdTree = new KdTreeST<>();

            // Insert all points from file into the KD-Tree
            while (!in.isEmpty()) {
                double x = in.readDouble();
                double y = in.readDouble();
                Point2D p = new Point2D(x, y);
                kdTree.put(p, 0); // Value is arbitrary, here just using 0
            }

            StopwatchCPU stopwatch;

            // Testing nearest()
            stopwatch = new StopwatchCPU();
            for (int i = 0; i < M; i++) {
                Point2D queryPoint = new Point2D(StdRandom.uniformDouble(0.0, 1.0),
                                                 StdRandom.uniformDouble(0.0, 1.0));
                kdTree.nearest(queryPoint);
            }
            cumulativeNearestTime += stopwatch.elapsedTime();

            // Testing contains()
            stopwatch = new StopwatchCPU();
            for (int i = 0; i < M; i++) {
                Point2D queryPoint = new Point2D(StdRandom.uniformDouble(0.0, 1.0),
                                                 StdRandom.uniformDouble(0.0, 1.0));
                kdTree.contains(queryPoint);
            }
            cumulativeContainsTime += stopwatch.elapsedTime();

            // Testing range()
            stopwatch = new StopwatchCPU();
            for (int i = 0; i < M; i++) {
                double xmin = StdRandom.uniformDouble(0.0, 1.0);
                double ymin = StdRandom.uniformDouble(0.0, 1.0);
                double xmax = StdRandom.uniformDouble(xmin, 1.0);
                double ymax = StdRandom.uniformDouble(ymin, 1.0);
                RectHV rect = new RectHV(xmin, ymin, xmax, ymax);
                kdTree.range(rect);
            }
            cumulativeRangeTime += stopwatch.elapsedTime();

            // Log the progress of iterations (optional)
            StdOut.println("Completed iteration " + (iter + 1) + "/" + trials);
        }

        // Calculate average times and calls per second
        double avgNearestTime = cumulativeNearestTime / trials;
        double avgPutTime = cumulativePutTime / trials;
        double avgContainsTime = cumulativeContainsTime / trials;
        double avgRangeTime = cumulativeRangeTime / trials;

        double nearestCPS = M / avgNearestTime;
        double putCPS = M / avgPutTime;
        double containsCPS = M / avgContainsTime;
        double rangeCPS = M / avgRangeTime;

        // Present results
        StdOut.printf("Average put() calls per second: %.2f\n", putCPS);
        StdOut.printf("Average contains() calls per second: %.2f\n", containsCPS);
        StdOut.printf("Average range() calls per second: %.2f\n", rangeCPS);
        StdOut.printf("Average nearest() calls per second: %.2f\n", nearestCPS);
    }
}
