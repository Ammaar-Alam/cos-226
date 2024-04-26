import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.CC;

public class Clustering {
    private final int[] clusters;
    private final int k;

    // run the clustering algorithm and create the clusters
    public Clustering(Point2D[] locations, int k) {
        if (locations == null || k < 1 || k > locations.length)
            throw new IllegalArgumentException("Invalid arguments");

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
            throw new IllegalArgumentException("Invalid index");
        return clusters[i];
    }

    // use the clusters to reduce the dimensions of an input
    public int[] reduceDimensions(int[] input) {
        if (input == null || input.length != clusters.length)
            throw new IllegalArgumentException("Invalid input");

        int[] reduced = new int[k];
        for (int i = 0; i < input.length; i++) {
            reduced[clusters[i]] += input[i];
        }
        return reduced;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // Test case 1: Normal usage
        Point2D[] locations = {
                new Point2D(0, 0),
                new Point2D(1, 1),
                new Point2D(2, 2),
                new Point2D(3, 3),
                new Point2D(4, 4)
        };
        int k = 2;
        Clustering clustering = new Clustering(locations, k);

        // Test clusterOf method
        System.out.println("Cluster assignments:");
        for (int i = 0; i < locations.length; i++) {
            System.out.println("Point " + i + ": Cluster " + clustering.clusterOf(i));
        }

        // Test reduceDimensions method
        int[] input = { 1, 2, 3, 4, 5 };
        int[] reduced = clustering.reduceDimensions(input);
        System.out.println("Reduced dimensions: ");
        for (int i = 0; i < reduced.length; i++) {
            System.out.println("Cluster " + i + ": " + reduced[i]);
        }

        // Test case 2: Invalid arguments
        try {
            new Clustering(null, k);
        } catch (IllegalArgumentException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }

        try {
            new Clustering(locations, 0);
        } catch (IllegalArgumentException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }

        try {
            new Clustering(locations, locations.length + 1);
        } catch (IllegalArgumentException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }

        try {
            clustering.clusterOf(-1);
        } catch (IllegalArgumentException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }

        try {
            clustering.clusterOf(locations.length);
        } catch (IllegalArgumentException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }

        try {
            clustering.reduceDimensions(null);
        } catch (IllegalArgumentException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }

        try {
            clustering.reduceDimensions(new int[locations.length - 1]);
        } catch (IllegalArgumentException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }
    }
}