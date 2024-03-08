import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class BinarySearchDeluxe {
    // Returns the index of the first key in the sorted array a[]
    // that is equal to the search key, or -1 if no such key.
    public static <Key> int firstIndexOf(Key[] a, Key key,
                                         Comparator<Key> comparator) {
        if (a == null || key == null || comparator == null)
            throw new IllegalArgumentException("args cannot be null");
        int low = 0, high = a.length - 1, result = -1;
        while (low <= high) {
            // calculate mid to split
            int mid = low + (high - low) / 2;
            // compare mid w key
            int cmp = comparator.compare(a[mid], key);
            if (cmp < 0) low = mid + 1; // if key is greater -> search right half
            else {
                if (cmp == 0) result = mid; // track pos of key
                high = mid - 1; // if key is less/found -> search left half
            }
        }
        return result; // return recorded pos
    }

    // Returns the index of the last key in the sorted array a[]
    // that is equal to the search key, or -1 if no such key.
    public static <Key> int lastIndexOf(Key[] a, Key key, Comparator<Key> comparator) {
        if (a == null || key == null || comparator == null)
            throw new IllegalArgumentException("args cannot be null");
        int low = 0, high = a.length - 1, result = -1;
        while (low <= high) {
            // calculate mid to split
            int mid = low + (high - low) / 2;
            // compare mid w key
            int cmp = comparator.compare(a[mid], key);
            if (cmp > 0) high = mid - 1; // if key is less -> search left half
            else {
                if (cmp == 0) result = mid;
                low = mid + 1; // if key is greater/found -> search right half
            }
        }
        return result; // return recorded pos
    }

    // unit testing (required)
    public static void main(String[] args) {
        // prolly good enough testing idrk
        Integer[] a = { 1, 2, 2, 2, 3, 3, 3, 3, 4 };
        Comparator<Integer> comparator = Integer::compare;
        // should print 1
        StdOut.println(firstIndexOf(a, 2, comparator));
        // should print 3
        StdOut.println(lastIndexOf(a, 2, comparator));
        // add binary testing case where term is found on the first attempt
    }
}
