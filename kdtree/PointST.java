import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

// "*" bc im trying out a new IDE (Zed it's amazing) but it doesn't automatically
// import libraries automatically like intelliJ
// pls no deduct points bc technically importing the entire package is inefficient

// act i fixed it myself nvm hehe

public class PointST<Value> {
    private TreeMap<Point2D, Value> st; // tree

    // construct an empty symbol table of points
    public PointST() {
        st = new TreeMap<>();
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return st.isEmpty();
    }

    // number of points
    public int size() {
        return st.size();
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null)
            throw new IllegalArgumentException("argument to put() is null");
        st.put(p, val);
    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null) throw new IllegalArgumentException("argument to get() is null");
        return st.get(p);
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new
                IllegalArgumentException("argument to contains() is null");
        return st.containsKey(p);
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        return st.keySet();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new
                IllegalArgumentException("argument to range() is null");
        List<Point2D> pointsInRange = new ArrayList<>();
        for (Point2D p : st.keySet()) {
            if (rect.contains(p)) pointsInRange.add(p);
        }
        return pointsInRange;
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new
                IllegalArgumentException("argument to nearest() is null");
        Point2D nearest = null;
        double nearestDistance = Double.POSITIVE_INFINITY;

        for (Point2D point : st.keySet()) {
            double dist = point.distanceSquaredTo(p);
            if (dist < nearestDistance) {
                nearest = point;
                nearestDistance = dist;
            }
        }
        return nearest;
    }

    // unit testing (required)
    public static void main(String[] args) {
        PointST<Integer> st = new PointST<>();

        StdOut.println("Testing PointST class");

        // test isEmpty() and size() on empty table
        StdOut.println("Is symbol table empty (should be true)? " + st.isEmpty());
        StdOut.println("Size of symbol table (should be 0): " + st.size());

        // inserting points
        Point2D[] points = {
                new Point2D(0.2, 0.3),
                new Point2D(0.5, 0.5),
                new Point2D(0.8, 0.1),
                new Point2D(0.1, 0.7)
        };

        for (int i = 0; i < points.length; i++) {
            st.put(points[i], i);
        }

        // test isEmpty() and size() after insertions
        StdOut.println("Is symbol table empty (should be false)? " + st.isEmpty());
        StdOut.println("Size of symbol table (should be 4): " + st.size());

        // test get() and contains()
        for (int i = 0; i < points.length; i++) {
            if (!st.contains(points[i])) {
                StdOut.println("Symbol table should contain point: " + points[i]);
            }
            else {
                StdOut.println("Value associated with " + points[i]
                                       + ": " + st.get(points[i]));
            }
        }

        // test points()
        StdOut.println("All points in the symbol table:");
        for (Point2D p : st.points()) {
            StdOut.println(p);
        }

        // test range search
        RectHV rect = new RectHV(0.1, 0.1, 0.7, 0.7);
        StdOut.println("Points within the rectangle " + rect + ":");
        for (Point2D p : st.range(rect)) {
            StdOut.println(p);
        }

        // test nearest neighbor search
        Point2D queryPoint = new Point2D(0.5, 0.5);
        Point2D nearest = st.nearest(queryPoint);
        StdOut.println("Nearest to point " + queryPoint + ": " + nearest);

        StdOut.println("All tests completed.");
    }
}
