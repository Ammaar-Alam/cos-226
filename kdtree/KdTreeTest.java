import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StopwatchCPU;

public class KdTreeTest {
    public static void main(String[] args) {
        KdTreeST<Integer> kdTree = new KdTreeST<>();
        int M = 10000; // # of tests
        String filename = args[0];
        In in = new In(filename);


        // Insert N random points into the KD-Tree
        for (int i = 0; !in.isEmpty(); i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdTree.put(p, i);
        }

        StopwatchCPU stopwatchNearest = new StopwatchCPU();
        for (int i = 0; i < M; i++) {
            Point2D queryPoint = new Point2D(StdRandom.uniformDouble(0.0, 1.0),
                                             StdRandom.uniformDouble(0.0, 1.0));
            kdTree.nearest(queryPoint);
        }
        double timeNearest = stopwatchNearest.elapsedTime();

        StopwatchCPU stopwatchPut = new StopwatchCPU();
        for (int i = 0; i < M; i++) {
            Point2D queryPoint = new Point2D(StdRandom.uniformDouble(0.0, 1.0),
                                             StdRandom.uniformDouble(0.0, 1.0));
            kdTree.put(queryPoint, i);
        }
        double timePut = stopwatchPut.elapsedTime();

        StopwatchCPU stopwatchContains = new StopwatchCPU();
        for (int i = 0; i < M; i++) {
            Point2D queryPoint = new Point2D(StdRandom.uniformDouble(0.0, 1.0),
                                             StdRandom.uniformDouble(0.0, 1.0));
            kdTree.contains(queryPoint);
        }
        double timeContains = stopwatchContains.elapsedTime();

        StopwatchCPU stopwatchRange = new StopwatchCPU();
        // this range test is def not right lol
        for (int i = 0; i < M; i++) {
            RectHV rangeRect = new RectHV(0.0, 0.0, 0.5, 0.5);
            kdTree.range(rangeRect);
        }
        double timeRange = stopwatchRange.elapsedTime();


        double nearestCPS = M / timeNearest;
        double putCPS = M / timePut;
        double containsCPS = M / timeContains;
        double rangeCPS = M / timeRange;
        double sum = putCPS + containsCPS + nearestCPS;

        StdOut.println("put() calls per second: " + putCPS);
        StdOut.println("contains() calls per second: " + containsCPS);
        StdOut.println("range() calls per second: " + rangeCPS);
        StdOut.println("nearest() calls per second: " + nearestCPS);
        StdOut.println("Sum of all calls " + sum);
    }
}
