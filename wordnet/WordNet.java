import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class WordNet {

    // changed to hashset for optimization
    private final Map<String, Set<Integer>> nounToSynsets; // noun to synset IDs
    private final Map<Integer, String> synsetToNouns; // synset ID to nouns
    private final Digraph digraph; // underlying digraph
    private final ShortestCommonAncestor sca; // pre-processed SCA
    private Set<String> allNounsCache; // cache for nouns()

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException(
            "input files cannot be null"
        );

        nounToSynsets = new HashMap<>();
        synsetToNouns = new HashMap<>();

        // read in synsets and populate maps
        readSynsets(synsets);

        // read in hypernyms and build digraph
        digraph = new Digraph(synsetToNouns.size());
        readHypernyms(hypernyms);

        // check for cycles
        DirectedCycle finder = new DirectedCycle(digraph);
        if (finder.hasCycle()) throw new IllegalArgumentException("input has cycle");

        // check for multiple roots
        int roots = 0;
        for (int i = 0; i < digraph.V(); i++) {
            if (digraph.outdegree(i) == 0) roots++;
        }
        if (roots != 1) throw new IllegalArgumentException("input does not have a single root");

        sca = new ShortestCommonAncestor(digraph);
    }

    // read in synsets and populate lookup maps
    private void readSynsets(String filename) {
        In in = new In(filename);
        while (in.hasNextLine()) {
            String[] tokens = in.readLine().split(",");
            int id = Integer.parseInt(tokens[0]);
            synsetToNouns.put(id, tokens[1]);

            String[] nouns = tokens[1].split(" ");
            for (String noun : nouns) {
                nounToSynsets.putIfAbsent(noun, new TreeSet<>());
                nounToSynsets.get(noun).add(id);
            }
        }
    }

    // read in hypernyms and populate digraph
    private void readHypernyms(String filename) {
        In in = new In(filename);
        while (in.hasNextLine()) {
            String[] tokens = in.readLine().split(",");
            int id = Integer.parseInt(tokens[0]);
            for (int i = 1; i < tokens.length; i++) {
                digraph.addEdge(id, Integer.parseInt(tokens[i]));
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        if (allNounsCache == null) {
            allNounsCache = new HashSet<>(nounToSynsets.keySet());
        }
        return allNounsCache;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException("input cannot be null");
        return nounToSynsets.containsKey(word);
    }

    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException(
            "input is not a WordNet noun"
        );
        int ancestor = sca.ancestorSubset(nounToSynsets.get(nounA), nounToSynsets.get(nounB));
        return synsetToNouns.get(ancestor);
    }

    // distance between noun1 and noun2 (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException(
            "input is not a WordNet noun"
        );
        return sca.lengthSubset(nounToSynsets.get(nounA), nounToSynsets.get(nounB));
    }

    // unit testing (required)
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);

        // test isNoun
        StdOut.println("Is 'word' a noun? " + wordnet.isNoun("word"));
        StdOut.println("Is 'notaword' a noun? " + wordnet.isNoun("notaword"));

        // test distance
        StdOut.println(
            "Distance between 'white_marlin' and 'mileage' is " +
            wordnet.distance("white_marlin", "mileage")
        );

        // test sca
        StdOut.println(
            "A common ancestor of 'individual' and 'edible_fruit' is " +
            wordnet.sca("individual", "edible_fruit")
        );

        // test nouns
        StdOut.println("\nAll nouns:");
        int count = 0;
        for (String noun : wordnet.nouns()) {
            System.out.print(noun + " ");
            count++;
            if (count % 10 == 0) StdOut.println();
        }
    }
}
