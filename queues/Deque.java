import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first, last; // first & last nodes
    private int size; // size of deque

    private static class Node<Item> {
        Item item; // the thing in the queue ig
        Node<Item> next; // next node
        Node<Item> previous; // last node
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("item cant be null");
        Node<Item> oldFirst = first;
        first = new Node<>();
        first.item = item;
        first.next = oldFirst;
        if (isEmpty()) last = first;
        else oldFirst.previous = first;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("iten cant be null");
        Node<Item> oldLast = last;
        last = new Node<>();
        last.item = item;
        last.previous = oldLast;
        if (isEmpty()) first = last;
        else oldLast.next = last;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("dequeue underflow");
        Item item = first.item;
        first = first.next;
        size--;
        if (isEmpty()) last = null; // this should fix loitering too right?
        else first.previous = null; // atl i think this way should work
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("dequeue underflow");
        Item item = last.item;
        last = last.previous;
        size--;
        if (isEmpty()) first = null;
        else last.next = null;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private Node<Item> current = first;

            public boolean hasNext() {
                return current != null;
            }

            public Item next() {
                if (!hasNext()) throw new NoSuchElementException();
                Item item = current.item;
                current = current.next;
                return item;
            }

            public void remove() {
                throw new UnsupportedOperationException(); // seriously, why?
            }
        }; // tbh i didnt know the ; was still required here
    }

    // unit testing (required)
    // lets be real, tigerfile is all the testing u need hehe
    public static void main(String[] args) {
        Deque<String> queue = new Deque<>();
        StdOut.println("is empty? (yes)" + queue.isEmpty());

        queue.addFirst("1 - first");
        queue.addLast("2 - last");
        queue.addFirst("0 - new first");
        queue.addLast("3 - new last");
        StdOut.println("size? (4) " + queue.size());

        // iterate testing
        /** for (String s : queue)
         StdOut.println("next thingy:" + queue.); **/
        Iterator<String> iterator = queue.iterator();
        for (String s : queue)
            StdOut.println("next thingy: " + s);
        StdOut.println("next item in queue: " + iterator.next());
        while (iterator.hasNext())
            iterator.remove(); // idk how else to test this thing lol

        StdOut.println("removed from fromt: " + queue.removeFirst());
        StdOut.println("removed from back: " + queue.removeLast());

    }
}
