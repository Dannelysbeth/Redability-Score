package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        String text = new String();
        String endSentenceCharacter = "[.!?]";
        int characters = 1;
        List<Integer> numberOfWordsSentences = new ArrayList<>();
        int polysyllablesCounter = 0;
        int syllablesCounter = 0;
        int wordsInSentenceCounter = 1;
        List<Character> word = new ArrayList<>();

        try {
            File myObj = new File(args[0]);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                text = myReader.nextLine();
            }
            myReader.close();
            for (int i = 1; i < text.length(); i++) {
                characters++;
                if (text.charAt(i) != ' ' && text.charAt(i - 1) == ' ') {
                    wordsInSentenceCounter++;
                    characters--;
                    syllablesCounter += getSyllablesInWord(word.toString());
                    polysyllablesCounter = isPolysyllable(word.toString()) ? polysyllablesCounter++ : polysyllablesCounter;
                } else if (String.valueOf(text.charAt(i)).matches(endSentenceCharacter)) {
                    numberOfWordsSentences.add(wordsInSentenceCounter);
                    characters--;
                    wordsInSentenceCounter = 0;
                    syllablesCounter += getSyllablesInWord(word.toString());
                    polysyllablesCounter = isPolysyllable(word.toString()) ? polysyllablesCounter++ : polysyllablesCounter;
                } else if ((String.valueOf(text.charAt(i)).matches("[\n\t]"))) {
                    characters--;
                    syllablesCounter += getSyllablesInWord(word.toString());
                    polysyllablesCounter = isPolysyllable(word.toString()) ? polysyllablesCounter++ : polysyllablesCounter;
                    word.clear();

                }
                word.add(text.charAt(i));
            }
            if (!text.matches(".*[.!?]$")) {
                numberOfWordsSentences.add(wordsInSentenceCounter);
            }
            int sumOfWords = numberOfWordsSentences.stream()
                    .mapToInt(Integer::intValue).sum();
            double avgWordsInSentence = (double) sumOfWords / (double) numberOfWordsSentences.size();
            characters += numberOfWordsSentences.size();
            if (text.charAt(text.length() - 1) != '.') {
                characters--;
            }
            String output = (avgWordsInSentence > 10.0) ? "HARD" : "EASY";

            double score = countScore(characters, sumOfWords, numberOfWordsSentences.size());
            String ageGroup = getAgeGroup(score);
            System.out.printf("\nWords: %d", sumOfWords);
            System.out.printf("\nSentences: %d", numberOfWordsSentences.size());
            System.out.printf("\nCharacters: %d", characters);
            System.out.printf("\nThe score is: %s", score);
            System.out.printf("\nThis text should be understood by %s year-olds.", ageGroup);
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    static double countScore(int characters, int words, int sentences) {
        double score = 4.71 * (double) characters / (double) words + 0.5 * (double) words / (double) sentences - 21.43;

        return score;
    }

    static String getAgeGroup(double score) {
        if (score > 13.0) {
            return "18-22";
        } else if (score > 12.0) {
            return "17-18";
        } else if (score > 11.0) {
            return "16-17";
        } else if (score > 10.0) {
            return "15-16";
        } else if (score > 9.0) {
            return "14-15";
        } else if (score > 8.0) {
            return "13-14";
        } else if (score > 7.0) {
            return "12-13";
        } else if (score > 6.0) {
            return "11-12";
        } else if (score > 5.0) {
            return "10-11";
        } else if (score > 4.0) {
            return "9-10";
        } else if (score > 3.0) {
            return "8-9";
        } else if (score > 2.0) {
            return "7-8";
        } else if (score > 1.0) {
            return "6-7";
        } else {
            return "5-6";
        }
    }

    public static int getSyllablesInWord(String word) {
        String vowels = "[aeiouy]";
        int syllables = String.valueOf(word.charAt(0)).matches(vowels) ? 1 : 0;
        for (int i = 1; i < word.length(); i++) {
            if (String.valueOf(word.charAt(i)).matches(vowels) && !String.valueOf(word.charAt(i - 1)).matches(vowels)) {
                syllables++;
            }
        }
        return syllables == 0 ? 1 : syllables;
    }

    public static boolean isPolysyllable(String word) {
        return getSyllablesInWord(word) > 2 ? true: false;
    }


}
