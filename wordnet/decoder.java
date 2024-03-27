import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.Map;

public class decoder {

    public static String decodeMessage(String filePath) {
        Map<Integer, String> lineToWord = new HashMap<>();
        In in = new In(filePath);

        while (!in.isEmpty()) {
            int number = in.readInt();
            String word = in.readString();
            lineToWord.put(number, word);
        }

        StringBuilder decodedMessage = new StringBuilder();
        for (int i = 1; lineToWord.containsKey(i); i++) {
            decodedMessage.append(lineToWord.get(i)).append(" ");
        }

        return decodedMessage.toString().trim();
    }

    // Example usage
    public static void main(String[] args) {
        String message = decodeMessage("coding_qual_input.txt");
        System.out.println(message);
    }
}
