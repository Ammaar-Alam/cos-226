import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class KdTreeST<Value> {
    private static final boolean VERTICAL = true; // caps bc constant

    private Node root; // root of the KdTree

    // node representation
    private class Node {
        // the point
        private Point2D p;
        // the symbol table maps the point to this value
        private Value val;
        // the axis-aligned rectangle corresponding to this node
        private RectHV rect;
        // the left/bottom subtree
        private Node lb;
        // the right/top subtree
        private Node rt;
        // size of the subtree
        private int size;

        // constructor for a new node
        public Node(Point2D p, Value val, RectHV rect, int size) {
            this.p = p;
            this.val = val;
            this.rect = rect;
            this.size = size;
        }
    }

    // construct an empty kd tree
    public KdTreeST() {
        root = null;
    }

    // is the tree empty
    public boolean isEmpty() {
        return size(root) == 0;
    }

    // return size of the node
    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    }

    // return size of the tree
    public int size() {
        return size(root);
    }

    // put a new key value pair into the tree
    public void put(Point2D p, Value val) {
        if (p == null || val == null) throw new
                IllegalArgumentException("arg to put() is null");
        root = put(root, p, val, new RectHV(Double.NEGATIVE_INFINITY,
                                            Double.NEGATIVE_INFINITY,
                                            Double.POSITIVE_INFINITY,
                                            Double.POSITIVE_INFINITY),
                   VERTICAL);
    }

    // helper method to insert a new node into the tree
    private Node put(Node h, Point2D p, Value val, RectHV rect, boolean orientation) {
        if (h == null) return new Node(p, val, rect, 1);
        if (h.p.equals(p)) {
            h.val = val;
            return h;
        }

        double cmp;
        if (orientation) {
            cmp = p.x() - h.p.x();
        }
        else {
            cmp = p.y() - h.p.y();
        }

        RectHV subRect;
        if (cmp < 0) {
            if (orientation) {
                subRect = new RectHV(rect.xmin(), rect.ymin(), h.p.x(), rect.ymax());
            }
            else {
                subRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), h.p.y());
            }
            h.lb = put(h.lb, p, val, subRect, !orientation);
        }
        else {
            if (orientation) {
                subRect = new RectHV(h.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
            }
            else {
                subRect = new RectHV(rect.xmin(), h.p.y(), rect.xmax(), rect.ymax());
            }
            h.rt = put(h.rt, p, val, subRect, !orientation);
        }

        h.size = 1 + size(h.lb) + size(h.rt);
        return h;
        // im hoping and begging that this properly handles recursive rectangle stuff
    }

    // get the value associated with point p
    public Value get(Point2D p) {
        if (p == null) throw new IllegalArgumentException("arg to get() is null");
        return get(root, p, VERTICAL);
    }

    // helper method to retrieve value given a point
    private Value get(Node h, Point2D p, boolean orientation) {
        if (h == null) return null;
        if (h.p.equals(p)) return h.val;

        double cmp;
        if (orientation) {
            cmp = p.x() - h.p.x();
        }
        else {
            cmp = p.y() - h.p.y();
        }

        if (cmp < 0) {
            return get(h.lb, p, !orientation);
        }
        else {
            return get(h.rt, p, !orientation);
        }
    }

    // does the tree contain point p
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("arg to contain() is null");
        return get(p) != null;
    }

    // return all points in the tree in level order
    public Iterable<Point2D> points() {
        Queue<Node> queue = new Queue<>();
        Queue<Point2D> points = new Queue<>();
        queue.enqueue(root);

        while (!queue.isEmpty()) {
            Node node = queue.dequeue();
            if (node == null) continue;
            points.enqueue(node.p);
            queue.enqueue(node.lb);
            queue.enqueue(node.rt);
        }

        return points;
    }

    // return all points within the given rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("arg to range() is null");
        Stack<Point2D> stack = new Stack<>();
        range(root, rect, stack);
        return stack;
    }

    // helper method to get all points within a rectangle
    private void range(Node x, RectHV rect, Stack<Point2D> stack) {
        if (x == null) return;
        if (!rect.intersects(x.rect)) return;

        if (rect.contains(x.p)) stack.push(x.p);
        range(x.lb, rect, stack);
        range(x.rt, rect, stack);
    }

    // find the closest point in the tree to a given point
    public Point2D nearest(Point2D point) {
        if (point == null) throw new IllegalArgumentException(" to nearest() is null");
        return nearest(root, point, null, Double.POSITIVE_INFINITY, VERTICAL);
    }

    // helper method to find the nearest point
    private Point2D nearest(Node h, Point2D p, Point2D nearest, double nearestDist,
                            boolean orientation) {
        if (h == null) return nearest;

        double dist = h.p.distanceSquaredTo(p);
        if (dist < nearestDist) {
            nearestDist = dist;
            nearest = h.p;
        }

        Node first, second;
        double cmp;
        if (orientation) {
            cmp = p.x() - h.p.x();
        }
        else {
            cmp = p.y() - h.p.y();
        }

        if (cmp < 0) {
            first = h.lb;
            second = h.rt;
        }
        else {
            first = h.rt;
            second = h.lb;
        }

        if (first != null && first.rect.distanceSquaredTo(p) < nearestDist) {
            nearest = nearest(first, p, nearest, nearestDist, !orientation);
            nearestDist = nearest.distanceSquaredTo(p);
        }
        if (second != null && second.rect.distanceSquaredTo(p) < nearestDist) {
            nearest = nearest(second, p, nearest, nearestDist, !orientation);
        }

        return nearest;
    }

    public static void main(String[] args) {
        KdTreeST<Integer> kdTree = new KdTreeST<>();

        StdOut.println("Testing isEmpty on empty tree: " + kdTree.isEmpty());

        // add points
        Point2D[] testPoints = {
                new Point2D(0.2, 0.3),
                new Point2D(0.4, 0.7),
                new Point2D(0.5, 0.1),
                new Point2D(0.9, 0.6),
                new Point2D(0.7, 0.2)
        };

        for (int i = 0; i < testPoints.length; i++) {
            kdTree.put(testPoints[i], i);
        }

        // testing size
        StdOut.println("Testing size: " + kdTree.size());

        // testing contains
        for (int i = 0; i < testPoints.length; i++) {
            boolean contains = kdTree.contains(testPoints[i]);
            StdOut.println("Testing contains for point " +
                                   testPoints[i] + ": " + contains);
        }

        // testing range
        Iterable<Point2D> range = kdTree.range(new RectHV(0.0, 0.0, 0.5, 0.5));
        StdOut.println("Points within the rectangle [0.0, 0.0] x [0.5, 0.5]:");
        for (Point2D p : range) {
            StdOut.println(p);
        }

        // testing nearest
        Point2D nearestPoint = kdTree.nearest(new Point2D(0.5, 0.5));
        StdOut.println("Nearest point to (0.5, 0.5): " + nearestPoint);

        // testing points
        Iterable<Point2D> allPoints = kdTree.points();
        StdOut.println("All points in the tree:");
        for (Point2D p : allPoints) {
            StdOut.println(p);
        }

        // testing get
        for (int i = 0; i < testPoints.length; i++) {
            int value = kdTree.get(testPoints[i]);
            StdOut.println("Value associated with point "
                                   + testPoints[i] + ": " + value);
        }
    }

    // public static void main(String[] args) {
    //     // Create a KdTreeST instance
    //     KdTreeST<Integer> kdTree = new KdTreeST<>();
    //     String filename = args[0];
    //     In in = new In(filename);
    //
    //     // Insert points with associated values
    //     for (int i = 0; !in.isEmpty(); i++) {
    //         double x = in.readDouble();
    //         double y = in.readDouble();
    //         Point2D p = new Point2D(x, y);
    //         kdTree.put(p, i);
    //     }
    //
    //     // Check isEmpty() and size()
    //     StdOut.println("Is the kd-tree empty? " + kdTree.isEmpty());
    //     StdOut.println("Size of kd-tree: " + kdTree.size());
    //
    //     // Test contains()
    //     StdOut.println(
    //             "Does the kd-tree contain (0.7, 0.2)? " +
    //             kdTree.contains(new Point2D(0.7, 0.2)));
    //
    //     // Test get()
    //     StdOut.println(
    //             "Value associated with (0.7, 0.2): " +
    //             kdTree.get(new Point2D(0.7, 0.2)));
    //
    //     // Test range search
    //     StdOut.println("Points within the rectangle [0.2, 0.2] x [0.8, 0.8]:");
    //     for (Point2D p : kdTree.range(new RectHV(0.2, 0.2, 0.8, 0.8))) {
    //         StdOut.println(p);
    //     }
    //
    //     // Test nearest neighbor search
    //     StdOut.println(
    //             "Nearest neighbor to point (0.6, 0.6): " +
    //             kdTree.nearest(new Point2D(0.6, 0.6)));
    // }
}
