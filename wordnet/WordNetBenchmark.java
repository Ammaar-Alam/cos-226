import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.List;

public class WordNetBenchmark {
    private static final int NUM_ITERATIONS = 100000;

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);

        List<String> nounList = new ArrayList<>();
        for (String noun : wordnet.nouns()) {
            nounList.add(noun);
        }

        String[] nouns = nounList.toArray(new String[0]);

        Stopwatch stopwatch = new Stopwatch();
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            String nounA = nouns[StdRandom.uniformInt(nouns.length)];
            String nounB = nouns[StdRandom.uniformInt(nouns.length)];
            wordnet.distance(nounA, nounB);
            wordnet.sca(nounA, nounB);
        }
        double time = stopwatch.elapsedTime();

        double callsPerSecond = 2 * NUM_ITERATIONS / time;
        StdOut.printf("Number of calls per second: %.2f\n", callsPerSecond);
    }
}
