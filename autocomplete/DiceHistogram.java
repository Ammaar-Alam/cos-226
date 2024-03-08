import edu.princeton.cs.algs4.StdOut;

public class DiceHistogram {
    public static void main(String[] args) {
        int m = Integer.parseInt(args[0]); // Number of dice
        int n = Integer.parseInt(args[1]); // Number of rolls

        // The possible sum of m dice ranges from m to 6*m
        int[] frequency = new int[6 * m - m + 1];

        // Simulate n rolls of m dice
        for (int roll = 0; roll < n; roll++) {
            int sum = 0;
            for (int dice = 0; dice < m; dice++) {
                sum += 1 + (int) (Math.random() * 6); // Each die can roll a value from 1 to 6
            }
            frequency[sum - m]++; // Increment the frequency of this sum
        }

        // Print the histogram
        for (int i = 0; i < frequency.length; i++) {
            StdOut.printf("%3d: ", i + m);
            for (int j = 0; j < frequency[i]; j++) {
                StdOut.print("*");
            }
            StdOut.println();
        }
    }
}
