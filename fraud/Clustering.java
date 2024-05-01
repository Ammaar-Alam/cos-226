import edu.princeton.cs.algs4.CC;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;

public class Clustering {
    private final int[] clusters;  // cluster assignments for each point
    private final int k;           // number of clusters

    // run the clustering algorithm and create the clusters
    public Clustering(Point2D[] locations, int k) {
        if (locations == null || k < 1 || k > locations.length)
            throw new IllegalArgumentException("invalid arguments");

        this.k = k;
        int n = locations.length;

        // Create a complete edge weighted graph
        EdgeWeightedGraph graph = new EdgeWeightedGraph(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double distance = locations[i].distanceTo(locations[j]);
                Edge edge = new Edge(i, j, distance);
                graph.addEdge(edge);
            }
        }

        // Compute the minimum spanning tree
        KruskalMST mst = new KruskalMST(graph);

        // Create the cluster graph by removing the k-1 longest edges
        EdgeWeightedGraph clusterGraph = new EdgeWeightedGraph(n);
        int edgesAdded = 0;
        for (Edge e : mst.edges()) {
            if (edgesAdded >= n - k)
                break;
            clusterGraph.addEdge(e);
            edgesAdded++;
        }

        // Assign clusters based on connected components
        CC cc = new CC(clusterGraph);
        clusters = new int[n];
        for (int i = 0; i < n; i++) {
            clusters[i] = cc.id(i);
        }
    }

    // return the cluster of the ith point
    public int clusterOf(int i) {
        if (i < 0 || i >= clusters.length)
            throw new IllegalArgumentException("in alid index");
        return clusters[i];
    }

    // use the clusters to reduce the dimensions of an input
    public int[] reduceDimensions(int[] input) {
        if (input == null || input.length != clusters.length)
            throw new IllegalArgumentException("invalid input");

        int[] reduced = new int[k];
        for (int i = 0; i < input.length; i++) {
            reduced[clusters[i]] += input[i];
        }
        return reduced;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // t1
        Point2D[] locations1 = {
                new Point2D(0, 0),
                new Point2D(1, 1),
                new Point2D(2, 2),
                new Point2D(3, 3),
                new Point2D(4, 4)
        };
        int k1 = 2;
        Clustering clustering1 = new Clustering(locations1, k1);

        // test clusterOf method
        StdOut.println("cluster assignments:");
        for (int i = 0; i < locations1.length; i++) {
            StdOut.println("Point " + i + ": Cluster " + clustering1.clusterOf(i));
        }

        // test reduceDimensions method
        int[] input1 = { 1, 2, 3, 4, 5 };
        int[] reduced1 = clustering1.reduceDimensions(input1);
        StdOut.println("reduced dimensions: ");
        for (int i = 0; i < reduced1.length; i++) {
            StdOut.println("Cluster " + i + ": " + reduced1[i]);
        }

        // invalid args test
        try {
            Clustering clustering2 = new Clustering(null, k1);
        }
        catch (IllegalArgumentException e) {
            StdOut.println("Exception caught: " + e.getMessage());
        }

        try {
            Clustering clustering3 = new Clustering(locations1, 0);
        }
        catch (IllegalArgumentException e) {
            StdOut.println("Exception caught: " + e.getMessage());
        }

        try {
            Clustering clustering4 = new Clustering(locations1, locations1.length + 1);
        }
        catch (IllegalArgumentException e) {
            StdOut.println("Exception caught: " + e.getMessage());
        }

        try {
            clustering1.clusterOf(-1);
        }
        catch (IllegalArgumentException e) {
            StdOut.println("Exception caught: " + e.getMessage());
        }

        try {
            clustering1.clusterOf(locations1.length);
        }
        catch (IllegalArgumentException e) {
            StdOut.println("Exception caught: " + e.getMessage());
        }

        try {
            clustering1.reduceDimensions(null);
        }
        catch (IllegalArgumentException e) {
            StdOut.println("Exception caught: " + e.getMessage());
        }

        try {
            clustering1.reduceDimensions(new int[locations1.length - 1]);
        }
        catch (IllegalArgumentException e) {
            StdOut.println("Exception caught: " + e.getMessage());
        }
    }
}
