import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class ShortestCommonAncestor {
    private Digraph G;  // the digraph

    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        if (G == null) throw new
                IllegalArgumentException("argument to constructor is null");
        this.G = new Digraph(G);
        if (!isRootedDAG(this.G)) throw new
                IllegalArgumentException("input is not a rooted DAG");
    }

    // is the given digraph a rooted DAG?
    private boolean isRootedDAG(Digraph G) {
        int roots = 0;
        for (int v = 0; v < G.V(); v++) {
            // find vertices with no outgoing edges (potential roots)
            if (G.outdegree(v) == 0) roots++;
        }
        return roots == 1; // Only one root should exist for a rooted DAG
    }

    // // depth-first search
    // private void dfs(Digraph G, int v, boolean[] visited) {
    //     visited[v] = true;
    //     for (int w : G.adj(v)) {
    //         if (!visited[w]) dfs(G, w, visited);
    //     }
    // }

    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        CustomBFS vBFS = new CustomBFS(G, v);
        CustomBFS wBFS = new CustomBFS(G, w);
        return calculateLength(vBFS, wBFS);
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        CustomBFS vBFS = new CustomBFS(G, v);
        CustomBFS wBFS = new CustomBFS(G, w);
        return calculateAncestor(vBFS, wBFS);
    }

    // length of shortest ancestral path of vertex subsets A and B
    public int lengthSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        validateSubset(subsetA);
        validateSubset(subsetB);
        CustomBFS bfsA = new CustomBFS(G, subsetA);
        CustomBFS bfsB = new CustomBFS(G, subsetB);
        return calculateLength(bfsA, bfsB);
    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestorSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        validateSubset(subsetA);
        validateSubset(subsetB);
        CustomBFS bfsA = new CustomBFS(G, subsetA);
        CustomBFS bfsB = new CustomBFS(G, subsetB);
        return calculateAncestor(bfsA, bfsB);
    }

    private int calculateLength(CustomBFS bfs1, CustomBFS bfs2) {
        int minDistance = Integer.MAX_VALUE;
        for (int v = 0; v < G.V(); v++) {
            if (bfs1.hasPathTo(v) && bfs2.hasPathTo(v)) {
                int distance = bfs1.distTo(v) + bfs2.distTo(v);
                minDistance = Math.min(minDistance, distance);
            }
        }
        return minDistance;
    }

    private int calculateAncestor(CustomBFS bfs1, CustomBFS bfs2) {
        int minDistance = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int v = 0; v < G.V(); v++) {
            if (bfs1.hasPathTo(v) && bfs2.hasPathTo(v)) {
                int distance = bfs1.distTo(v) + bfs2.distTo(v);
                if (distance < minDistance) {
                    minDistance = distance;
                    ancestor = v;
                }
            }
        }
        return ancestor;
    }

    // throw an IllegalArgumentException if vertex v is not between 0 and V-1
    private void validateVertex(int v) {
        if (v < 0 || v >= G.V())
            throw new IllegalArgumentException(
                    "vertex " + v + " is not between 0 and " + (G.V() - 1));
    }

    // throw an IllegalArgumentException if vertices is null, has zero vertices,
    // or has a vertex that is not between 0 and V-1
    private void validateSubset(Iterable<Integer> vertices) {
        if (vertices == null) throw new IllegalArgumentException("argument is null");
        int V = G.V();
        for (int v : vertices) {
            if (v < 0 || v >= V)
                throw new IllegalArgumentException(
                        "vertex " + v + " is not between 0 and " + (V - 1));
        }
        if (!vertices.iterator().hasNext())
            throw new IllegalArgumentException("subset is empty");
    }

    private class CustomBFS {
        private boolean[] marked;  // is there a path from source to v?
        private int[] distTo;      // length of shortest path from source to v
        private int[] edgeTo;      // last edge on shortest path from source to v

        // Breadth-first search from a single source
        private CustomBFS(Digraph G, int s) {
            marked = new boolean[G.V()];
            distTo = new int[G.V()];
            edgeTo = new int[G.V()];
            bfs(G, s);
        }

        // Breadth-first search from multiple sources
        private CustomBFS(Digraph G, Iterable<Integer> sources) {
            marked = new boolean[G.V()];
            distTo = new int[G.V()];
            edgeTo = new int[G.V()];
            bfs(G, sources);
        }

        private void bfs(Digraph G, int s) {
            for (int v = 0; v < G.V(); v++) distTo[v] = Integer.MAX_VALUE;
            distTo[s] = 0;
            marked[s] = true;
            Queue<Integer> q = new Queue<>();
            q.enqueue(s);
            while (!q.isEmpty()) {
                int v = q.dequeue();
                for (int w : G.adj(v)) {
                    if (!marked[w]) {
                        edgeTo[w] = v;
                        distTo[w] = distTo[v] + 1;
                        marked[w] = true;
                        q.enqueue(w);
                    }
                }
            }
        }

        private void bfs(Digraph G, Iterable<Integer> sources) {
            for (int v = 0; v < G.V(); v++) distTo[v] = Integer.MAX_VALUE;
            for (int s : sources) {
                distTo[s] = 0;
                marked[s] = true;
            }
            Queue<Integer> q = new Queue<>();
            for (int s : sources) {
                q.enqueue(s);
            }
            while (!q.isEmpty()) {
                int v = q.dequeue();
                for (int w : G.adj(v)) {
                    if (!marked[w]) {
                        edgeTo[w] = v;
                        distTo[w] = distTo[v] + 1;
                        marked[w] = true;
                        q.enqueue(w);
                    }
                }
            }
        }

        private boolean hasPathTo(int v) {
            return marked[v];
        }

        private int distTo(int v) {
            return distTo[v];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
