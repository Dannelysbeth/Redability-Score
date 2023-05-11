package readability;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String endSentenceCharacter = "[.!?]";
        List<Integer> numberOfWordsSentences = new ArrayList<>();
        int wordsInSentenceCounter = 1;
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
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
        OptionalDouble avgWordsInSentence =  numberOfWordsSentences.stream()
                .mapToInt(Integer::intValue).average() ;
        String output = (avgWordsInSentence.orElse(0.0) >= 10.0) ? "HARD" : "EASY";

        System.out.print(output);
    }
}
