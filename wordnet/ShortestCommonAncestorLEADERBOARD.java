import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ShortestCommonAncestor {
    private final Digraph digraph; // the digraph
    // cache for ancestor and length calculations
    private final Map<VertexPair, int[]> cache = new HashMap<>();

    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph digraph) {
        if (digraph == null) throw new IllegalArgumentException("digraph is null");
        this.digraph = new Digraph(digraph);
        DirectedCycle finder = new DirectedCycle(this.digraph);
        if (finder.hasCycle()) throw new IllegalArgumentException("diagraph is not a DAG");
        int roots = 0;
        for (int v = 0; v < digraph.V(); v++) {
            if (digraph.outdegree(v) == 0) roots++;
        }
        if (roots != 1) throw new IllegalArgumentException("digraph is not a rooted DAG");
    }

    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        return ancestorAndLength(new HashSet<>(Set.of(v)), new HashSet<>(Set.of(w)))[1];
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        return ancestorAndLength(new HashSet<>(Set.of(v)), new HashSet<>(Set.of(w)))[0];
    }

    // length of shortest ancestral path of vertex subsets A and B
    public int lengthSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (subsetA == null || subsetB == null)
            throw new IllegalArgumentException("input files cannot be null");
        validateSubset(subsetA);
        validateSubset(subsetB);
        return ancestorAndLength(subsetA, subsetB)[1];
    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestorSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        validateSubset(subsetA);
        validateSubset(subsetB);
        return ancestorAndLength(subsetA, subsetB)[0];
    }

    // returns ancestor and length of the shortest ancestral path
    private int[] ancestorAndLength(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        Set<Integer> setA = iterableToSet(subsetA);
        Set<Integer> setB = iterableToSet(subsetB);
        VertexPair pair = new VertexPair(setA, setB);
        if (cache.containsKey(pair)) {
            return cache.get(pair);
        }
        AncestorBFS bfs = new AncestorBFS(digraph, setA, setB);
        int[] result = { bfs.getAncestor(), bfs.getMinDistance() };
        cache.put(pair, result);
        return result;
    }

    // converts iterable to set
    private Set<Integer> iterableToSet(Iterable<Integer> iterable) {
        Set<Integer> resultSet = new HashSet<>();
        iterable.forEach(resultSet::add);
        return resultSet;
    }

    // validates vertex
    private void validateVertex(int v) {
        if (v < 0 || v >= digraph.V()) {
            throw new IllegalArgumentException(
                    "vertex " + v + " is not between 0 and " + (digraph.V() - 1));
        }
    }

    // validates vertesx subset
    private void validateSubset(Iterable<Integer> vertices) {
        if (vertices == null) throw new IllegalArgumentException("Argument is null");
        if (!vertices.iterator().hasNext())
            throw new IllegalArgumentException("Subset is empty");
        for (Integer v : vertices) {
            if (v == null)
                throw new IllegalArgumentException("Subset contains null item");
            validateVertex(v);
        }
    }

    private class AncestorBFS {
        // dist from subset A to each vertex
        private final Map<Integer, Integer> distToA = new HashMap<>();
        // dist from subset B to each vertex
        private final Map<Integer, Integer> distToB = new HashMap<>();
        // min dist found so far
        private int minDistance = Integer.MAX_VALUE;
        // ancestor corresponding to the min dist
        private int ancestor = -1;

        public AncestorBFS(Digraph digraph, Iterable<Integer> subsetA,
                           Iterable<Integer> subsetB) {
            bfs(digraph, subsetA, distToA);
            bfs(digraph, subsetB, distToB);

            for (Integer common : distToA.keySet()) {
                if (distToB.containsKey(common)) {
                    int totalDist = distToA.get(common) + distToB.get(common);
                    if (totalDist < minDistance) {
                        minDistance = totalDist;
                        ancestor = common;
                    }
                }
            }
        }

        private void bfs(Digraph d, Iterable<Integer> sources,
                         Map<Integer, Integer> distTo) {
            Queue<Integer> q = new Queue<>();
            for (int s : sources) {
                distTo.put(s, 0);
                q.enqueue(s);
            }
            while (!q.isEmpty()) {
                int v = q.dequeue();
                for (int w : d.adj(v)) {
                    if (!distTo.containsKey(w)) {
                        distTo.put(w, distTo.get(v) + 1);
                        q.enqueue(w);
                    }
                }
            }
        }

        public int getMinDistance() {
            return minDistance == Integer.MAX_VALUE ? -1 : minDistance;
        }

        public int getAncestor() {
            return ancestor;
        }
    }

    private class VertexPair {
        // vertices of subset A
        private final Set<Integer> vertexSetA;
        // vertices of subset B
        private final Set<Integer> vertexSetB;

        // constructor creates a pair from two subsets
        public VertexPair(Set<Integer> vertexSetA, Set<Integer> vertexSetB) {
            this.vertexSetA = vertexSetA;
            this.vertexSetB = vertexSetB;
        }

        // checks if two VertexPairs are equal
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            VertexPair that = (VertexPair) obj;
            return Objects.equals(vertexSetA, that.vertexSetA) &&
                    Objects.equals(vertexSetB, that.vertexSetB);
        }

        @Override
        public int hashCode() {
            return Objects.hash(vertexSetA, vertexSetB);
        }
    }

    // unit testing
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
            int lengthSubset = sca.lengthSubset(Set.of(v), Set.of(w));
            int ancestorSubset = sca.ancestorSubset(Set.of(v), Set.of(w));
            StdOut.printf("lengthSubset = %d, ancestorSubset = %d\n",
                          lengthSubset, ancestorSubset);
        }
    }
}
