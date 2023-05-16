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
        String text = scanner.nextLine().trim();
        for (char word : text.toCharArray()) {
            if (word == ' ') {
                wordsInSentenceCounter++;
            } else if (String.valueOf(word).matches(endSentenceCharacter)) {
                numberOfWordsSentences.add(wordsInSentenceCounter);
                wordsInSentenceCounter = 1;
            }
        }
        if (!text.matches(".*[.!?]$")) {
            numberOfWordsSentences.add(wordsInSentenceCounter);
        }
        int sumOfWords = numberOfWordsSentences.stream()
                .mapToInt(Integer::intValue).sum() - 1;
        double avgWordsInSentence = (double) sumOfWords / (double) numberOfWordsSentences.size();
        String output = (avgWordsInSentence >= 10.0) ? "HARD" : "EASY";

        System.out.print(output);
    }
}
