import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue; // the stack of items
    private int size; // size of queue

    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[]) new Object[1];
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("cant add null item");
        if (size == queue.length) resize(2 * queue.length);
        queue[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("queue underflow");
        int randomIndex = StdRandom.uniformInt(size);
        Item item = queue[randomIndex];
        queue[randomIndex] = queue[size - 1];
        queue[size - 1] = null; // no loitering hehe
        size--;
        if (size > 0 && size == queue.length / 4) resize(queue.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("queue underflow");
        return queue[StdRandom.uniformInt(size)];
    }

    // resize the underlying array
    // lowk idk if this is needed but y not steal given code from the website :P
    private void resize(int capacity) {
        assert capacity >= size;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            temp[i] = queue[i];
        }
        queue = temp;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int current = 0; // current queue index @ iterate
        private final Item[] shuffled; // shuffled queue

        // to iterate randomly over items in queue w/o modifying
        public RandomizedQueueIterator() {
            shuffled = (Item[]) new Object[size];
            System.arraycopy(queue, 0, shuffled, 0, size);
            StdRandom.shuffle(shuffled);
        }

        public boolean hasNext() {
            return current < size;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return shuffled[current++];
        }

        // idrk why this remove is needed, but the booksite said so?
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    // naur no more unit testing :( just give it to us :(
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        queue.enqueue("1");
        queue.enqueue("2");
        queue.enqueue("3");
        StdOut.println("sizse? (3) " + queue.size());
        StdOut.println("is empty? " + queue.isEmpty());
        StdOut.println("sample: " + queue.sample());
        for (String s : queue)
            StdOut.println(s); // all of that just for this line
        StdOut.println("dequeued: " + queue.dequeue());

        Iterator<String> iterator = queue.iterator();
        while (iterator.hasNext())
            iterator.remove(); // idk how else to test this thing lol
    }
}
