package readability;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String endSentenceCharacter = "[.!?]";
        List<Integer> numberOfWordsSentences = new ArrayList<>();
        int wordsInSentenceCounter = 1;
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) != ' ' && text.charAt(i - 1) == ' ') {
                wordsInSentenceCounter++;
            } else if (String.valueOf(text.charAt(i)).matches(endSentenceCharacter)) {
                numberOfWordsSentences.add(wordsInSentenceCounter);
                wordsInSentenceCounter = 0;
            }
        }
        if (!text.matches(".*[.!?]$")) {
            numberOfWordsSentences.add(wordsInSentenceCounter);
        }
        int sumOfWords = numberOfWordsSentences.stream()
                .mapToInt(Integer::intValue).sum();
        double avgWordsInSentence = (double) sumOfWords / (double) numberOfWordsSentences.size();
        String output = (avgWordsInSentence > 10.0) ? "HARD" : "EASY";

        System.out.print(output);
    }
    //TODO exercise 3 b
}
