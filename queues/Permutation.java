import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        /** while (!StdIn.isEmpty()) {
         String value = StdIn.readString();
         StdRandom.uniform()
         } **/
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        int counter = 0; // count of items read

        // edge case
        if (k == 0)
            while (!StdIn.isEmpty()) {
                StdIn.readString(); // just ignore it lol? idk wat else to do
            }

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            counter++;
            if (queue.size() < k)
                queue.enqueue(item);
            else {
                // randomly decide to replace other item,,, can i do this? idk lol
                int randomNumber = StdRandom.uniformInt(counter); // idk wtf im doing
                if (randomNumber < k) {
                    queue.dequeue();
                    queue.enqueue(item);
                }
            }
        }

        for (int i = 0; i < k && !queue.isEmpty(); i++)
            StdOut.println(queue.dequeue());
    }
}
