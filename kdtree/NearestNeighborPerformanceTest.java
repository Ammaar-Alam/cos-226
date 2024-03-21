import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

public class NearestNeighborPerformanceTest {

    public static void main(String[] args) {
        KdTreeST<Integer> kdTree = new KdTreeST<>();

        // Read file input1M.txt and insert those 1 million points into the k-d tree
        // This part of the code is assumed to be implemented

        // Number of nearest() calls to perform
        int m = 1000000; // Adjust as needed to get a measurable and reasonable timing
        double totalTime = 0;

        Stopwatch stopwatch = new Stopwatch();

        // Perform m nearest() calls with random points in the unit square
        for (int i = 0; i < m; i++) {
            Point2D queryPoint = new Point2D(StdRandom.uniformDouble(0.0, 1.0),
                                             StdRandom.uniformDouble(0.0, 1.0));
            Point2D nearestPoint = kdTree.nearest(queryPoint);
        }

        // Measure the total CPU time in seconds for the calls to nearest()
        totalTime = stopwatch.elapsedTime();

        // Calculate the number of calls per second
        double callsPerSecond = m / totalTime;

        StdOut.println("Total time for " + m + " nearest() calls: " + totalTime + " seconds.");
        StdOut.println("Number of nearest() calls per second: " + callsPerSecond);
    }
}
