package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Step 1: Read text from file
        String text = getTextFromFile(args);

        // Step 2: Count words, sentences, characters, syllables, and polysyllables
        List<Integer> numberOfWordsSentences = getNumberOfWordsSentences(text);
        int characters = getCharacterCount(text);
        int sumOfWords = getSumOfWords(numberOfWordsSentences);
        int syllablesCounter = getSyllablesCount(text);
        int polysyllablesCounter = getPolysyllablesCount(text);

        // Step 3: Print statistics
        printStatistics(sumOfWords, numberOfWordsSentences, characters, syllablesCounter, polysyllablesCounter);

        // Step 4: Calculate readability scores
        calculateReadabilityScores(sumOfWords, numberOfWordsSentences, characters, syllablesCounter, polysyllablesCounter);
    }

    private static String getTextFromFile(String[] args) {
        String text = "";
        try {
            // Read text from the file specified in command-line arguments
            File myObj = new File(args[0]);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                text = myReader.nextLine();
                System.out.println(text);
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
        return text;
    }

    private static List<Integer> getNumberOfWordsSentences(String text) {
        // Step 1: Count the number of words and sentences in the text
        String endSentenceCharacter = "[.!?]";
        List<Integer> numberOfWordsSentences = new ArrayList<>();
        List<Character> word = new ArrayList<>();
        int wordsInSentenceCounter = 1;

        word.add(text.charAt(0));
        for (int i = 1; i < text.length(); i++) {
            // Check for word boundaries and sentence endings
            if (text.charAt(i) != ' ' && text.charAt(i - 1) == ' ') {
                wordsInSentenceCounter++;
                word.clear();
            } else if (String.valueOf(text.charAt(i)).matches(endSentenceCharacter)) {
                numberOfWordsSentences.add(wordsInSentenceCounter);
                wordsInSentenceCounter = 0;
                word.clear();
            } else if ((String.valueOf(text.charAt(i)).matches("[\n\t]"))) {
                word.clear();
            }
            word.add(text.charAt(i));
        }
        // Add the count for the last sentence if it doesn't end with punctuation
        if (!text.matches(".*[.!?]$")) {
            numberOfWordsSentences.add(wordsInSentenceCounter);
        }
        return numberOfWordsSentences;
    }

    private static int getCharacterCount(String text) {
        // Step 2: Count the number of characters in the text (excluding whitespaces)
        return text.replaceAll("\\s", "").length();
    }

    private static int getSumOfWords(List<Integer> numberOfWordsSentences) {
        // Step 2: Calculate the sum of words from the list of word counts in sentences
        return numberOfWordsSentences.stream().mapToInt(Integer::intValue).sum();
    }

    private static int getSyllablesCount(String text) {
        // Step 2: Count the number of syllables in the text
        int syllablesCounter = 0;
        String[] words = text.trim().split("\\s+");
        for (String word : words) {
            syllablesCounter += getSyllablesInWord(word);
        }
        return syllablesCounter;
    }

    private static int getSyllablesInWord(String word) {
        // Helper method to count syllables in a word using a simple heuristic
        int syllables = word.toLowerCase().replaceAll("e$", "").replaceAll("[^aeiouy]+", " ").trim().split("\\s+").length;
        return syllables == 0 ? 1 : syllables;
    }

    private static int getPolysyllablesCount(String text) {
        // Step 2: Count the number of polysyllables in the text
        int polysyllablesCounter = 0;
        String[] words = text.trim().split("\\s+");
        for (String word : words) {
            polysyllablesCounter += isPolysyllable(word);
        }
        return polysyllablesCounter;
    }

    private static int isPolysyllable(String word) {
        // Helper method to determine if a word is a polysyllable
        int syllables = getSyllablesInWord(word);
        return syllables > 2 ? 1 : 0;
    }

    private static void printStatistics(int sumOfWords, List<Integer> numberOfWordsSentences,
                                        int characters, int syllablesCounter, int polysyllablesCounter) {
        // Step 3: Print the calculated statistics
        System.out.printf("\nWords: %d", sumOfWords);
        System.out.printf("\nSentences: %d", numberOfWordsSentences.size());
        System.out.printf("\nCharacters: %d", characters);
        System.out.printf("\nSyllables: %d", syllablesCounter);
        System.out.printf("\nPolysyllables: %d", polysyllablesCounter);
    }

    private static void calculateReadabilityScores(int sumOfWords, List<Integer> numberOfWordsSentences,
                                                   int characters, int syllablesCounter, int polysyllablesCounter) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter the score you want to calculate (ARI, FK, SMOG, CL, all): all");
        String option = scanner.next();
        double score;
        String ageGroup;

        switch (option) {
            case "ARI" -> {
                // Calculate Automated Readability Index (ARI) score
                score = automatedReadabilityIndex(characters, sumOfWords, numberOfWordsSentences.size());
                ageGroup = upperAgeBound(score);
                System.out.printf("Automated Readability Index: %s (about %s-year-olds).\n", score, ageGroup);
            }
            case "FK" -> {
                // Calculate Flesch-Kincaid Readability Tests (FK) score
                score = fleschKincaidReadabilityTests(sumOfWords, numberOfWordsSentences.size(), syllablesCounter);
                ageGroup = upperAgeBound(score);
                System.out.printf("Flesch–Kincaid readability tests: %s (about %s-year-olds).\n", score, ageGroup);
            }
            case "SMOG" -> {
                // Calculate Simple Measure of Gobbledygook (SMOG) score
                score = smogIndex(numberOfWordsSentences.size(), polysyllablesCounter);
                ageGroup = upperAgeBound(score);
                System.out.printf("Simple Measure of Gobbledygook: %s (about %s-year-olds).\n", score, ageGroup);
            }
            case "CL" -> {
                // Calculate Coleman-Liau Index (CL) score
                score = colemanLiauIndex(characters, sumOfWords, numberOfWordsSentences.size());
                ageGroup = upperAgeBound(score);
                System.out.printf("Coleman–Liau index: %s (about %s-year-olds).\n", score, ageGroup);
            }
            case "all" -> {
                // Calculate all readability scores
                score = automatedReadabilityIndex(characters, sumOfWords, numberOfWordsSentences.size());
                ageGroup = upperAgeBound(score);
                System.out.printf("Automated Readability Index: %s (about %s-year-olds).\n", score, ageGroup);
                score = fleschKincaidReadabilityTests(sumOfWords, numberOfWordsSentences.size(), syllablesCounter);
                ageGroup = upperAgeBound(score);
                System.out.printf("Flesch–Kincaid readability tests: %s (about %s-year-olds).\n", score, ageGroup);
                score = smogIndex(numberOfWordsSentences.size(), polysyllablesCounter);
                ageGroup = upperAgeBound(score);
                System.out.printf("Simple Measure of Gobbledygook: %s (about %s-year-olds).\n", score, ageGroup);
                score = colemanLiauIndex(characters, sumOfWords, numberOfWordsSentences.size());
                ageGroup = upperAgeBound(score);
                System.out.printf("Coleman–Liau index: %s (about %s-year-olds).\n", score, ageGroup);
            }
            default -> {
                // Invalid option, do nothing
            }
        }
    }

    private static double automatedReadabilityIndex(int characters, int words, int sentences) {
        // Calculate Automated Readability Index (ARI) score
        return 4.71 * (double) characters / (double) words + 0.5 * (double) words / (double) sentences - 21.43;
    }

    private static double smogIndex(int sentences, int polysyllables) {
        // Calculate Simple Measure of Gobbledygook (SMOG) score
        return 1.043 * Math.sqrt(polysyllables * 30 / (double) sentences) + 3.1291;
    }

    private static double fleschKincaidReadabilityTests(int words, int sentences, int syllables) {
        // Calculate Flesch-Kincaid Readability Tests (FK) score
        return 0.39 * ((double) words / (double) sentences) + 11.8 * ((double) syllables / (double) words) - 15.59;
    }

    private static double colemanLiauIndex(int characters, int words, int sentences) {
        // Calculate Coleman-Liau Index (CL) score
        double L = (double) characters / (double) words * 100.0;
        double S = (double) sentences / (double) words * 100.0;
        return 0.0588 * L - 0.296 * S - 15.8;
    }

    private static String upperAgeBound(double score) {
        // Helper method to determine the age group based on the score
        if (score > 13.0) {
            return "22";
        } else if (score > 12.0) {
            return "18";
        } else if (score > 11.0) {
            return "17";
        } else if (score > 10.0) {
            return "16";
        } else if (score > 9.0) {
            return "15";
        } else if (score > 8.0) {
            return "14";
        } else if (score > 7.0) {
            return "13";
        } else if (score > 6.0) {
            return "12";
        } else if (score > 5.0) {
            return "11";
        } else if (score > 4.0) {
            return "10";
        } else if (score > 3.0) {
            return "9";
        } else if (score > 2.0) {
            return "8";
        } else if (score > 1.0) {
            return "7";
        } else {
            return "6";
        }
    }
}
