import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

// Immutable data type for autocomplete terms.
public class Term implements Comparable<Term> {

    private final String query; // query string
    private final long weight;  // associated weight for the term

    // Initializes a term with the given query string and weight.
    public Term(String query, long weight) {
        if (query == null) throw new IllegalArgumentException("query cannot be null");
        if (weight < 0) throw new IllegalArgumentException(
                "weight must be non-negative");

        this.query = query;
        this.weight = weight;
    }

    // Compares the two terms in descending order by weight.
    public static Comparator<Term> byReverseWeightOrder() {
        return (o1, o2) -> Long.compare(o2.weight, o1.weight);
    }

    // Compares the two terms in lexicographic order,
    // but using only the first r characters of each query.
    public static Comparator<Term> byPrefixOrder(int r) {
        if (r < 0) throw new IllegalArgumentException(
                "number of characters to consider cannot be negative");

        return (o1, o2) -> {
            String s1 = o1.query.substring(0, Math.min(r, o1.query.length()));
            String s2 = o2.query.substring(0, Math.min(r, o2.query.length()));
            return s1.compareTo(s2);
        };
    }

    // Compares the two terms in lexicographic order by query.
    public int compareTo(Term that) {
        return this.query.compareTo(that.query);
    }

    // Returns a string representation of this term in the following format:
    // the weight, followed by a tab, followed by the query.
    public String toString() {
        return weight + "\t" + query;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // create two terms
        Term term1 = new Term("apple", 50);
        Term term2 = new Term("orange", 30);

        // compare the terms using compareTo
        StdOut.println(term1.compareTo(term2));

        // compare by reverse weight order
        Comparator<Term> comp = Term.byReverseWeightOrder();
        StdOut.println(comp.compare(term1, term2));

        // compare by prefix order of first 3 chars
        comp = Term.byPrefixOrder(3);
        StdOut.println(comp.compare(term1, term2));

        // print terms
        StdOut.println(term1);
        StdOut.println(term2);
    }
}
