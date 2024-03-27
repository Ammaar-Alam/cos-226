import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordnet;  // wordnet object to for dist calc

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        if (wordnet == null) throw new IllegalArgumentException("wordnet cannot be null");
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        if (nouns == null || nouns.length < 2)
            throw new IllegalArgumentException("input must contain at least 2 nouns");

        int n = nouns.length;
        int[] distances = new int[n];

        // calculate sum of distances for each noun
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    distances[i] += wordnet.distance(nouns[i], nouns[j]);
                }
            }
        }

        // find noun with maximum distance
        int maxIndex = 0;
        for (int i = 1; i < n; i++) {
            if (distances[i] > distances[maxIndex]) {
                maxIndex = i;
            }
        }

        return nouns[maxIndex];
    }

    // test client (see below)
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
