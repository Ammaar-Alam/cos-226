import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StopwatchCPU;

public class PointSTTest {
    public static void main(String[] args) {
        PointST<Integer> pointST = new PointST<>();
        int M = 100000; // # of tests
        String filename = args[0];
        In in = new In(filename);

        // Insert N random points into the PointST
        for (int i = 0; !in.isEmpty(); i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            pointST.put(p, i);
        }

        StopwatchCPU stopwatchNearest = new StopwatchCPU();
        for (int i = 0; i < M; i++) {
            Point2D queryPoint = new Point2D(StdRandom.uniformDouble(0.0, 1.0),
                                             StdRandom.uniformDouble(0.0, 1.0));
            pointST.nearest(queryPoint);
        }
        double timeNearest = stopwatchNearest.elapsedTime();

        // StopwatchCPU stopwatchPut = new StopwatchCPU();
        // for (int i = 0; i < M; i++) {
        //     Point2D queryPoint = new Point2D(StdRandom.uniformDouble(0.0, 1.0),
        //                                      StdRandom.uniformDouble(0.0, 1.0));
        //     pointST.put(queryPoint, i);
        // }
        // double timePut = stopwatchPut.elapsedTime();
        //
        // StopwatchCPU stopwatchContains = new StopwatchCPU();
        // for (int i = 0; i < M; i++) {
        //     Point2D queryPoint = new Point2D(StdRandom.uniformDouble(0.0, 1.0),
        //                                      StdRandom.uniformDouble(0.0, 1.0));
        //     pointST.contains(queryPoint);
        // }
        // double timeContains = stopwatchContains.elapsedTime();
        //
        // StopwatchCPU stopwatchRange = new StopwatchCPU();
        // for (int i = 0; i < M; i++) {
        //     double xmin = StdRandom.uniformDouble(0.0, 0.5);
        //     double ymin = StdRandom.uniformDouble(0.0, 0.5);
        //     double xmax = StdRandom.uniformDouble(xmin, 1.0);
        //     double ymax = StdRandom.uniformDouble(ymin, 1.0);
        //     RectHV rangeRect = new RectHV(xmin, ymin, xmax, ymax);
        //     pointST.range(rangeRect);
        // }
        // double timeRange = stopwatchRange.elapsedTime();

        double nearestCPS = M / timeNearest;
        // double putCPS = M / timePut;
        // double containsCPS = M / timeContains;
        // double rangeCPS = M / timeRange;
        double sum = nearestCPS;

        // StdOut.println("put() calls per second: " + putCPS);
        // StdOut.println("contains() calls per second: " + containsCPS);
        // StdOut.println("range() calls per second: " + rangeCPS);
        StdOut.println("nearest() calls per second: " + nearestCPS);
        StdOut.println("Sum of all calls " + sum);
    }
}
