package antivoland.amahir.bullshit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Bullshit {
    private static final String SEPARATOR = " ";

    public static class Dictionary {
        public enum Contains {YES, NO, MAYBE}

        private final TreeSet<String> phrases;

        public Dictionary(Set<String> phrases) {
            this.phrases = new TreeSet<>(phrases);
        }

        public Contains contains(String phrase) {
            String ceiling = phrases.ceiling(phrase);
            if (ceiling == null) {
                return Contains.NO;
            }
            if (ceiling.equals(phrase)) {
                return Contains.YES;
            }
            if (ceiling.startsWith(phrase)) {
                return Contains.MAYBE;
            }
            return Contains.NO;
        }
    }

    public static int nonbullshitability(String phrase, Dictionary dictionary) {
        return nonbullshitability(phrase.split(SEPARATOR), 0, dictionary);
    }

    private static int nonbullshitability(String[] words, int from, Dictionary dictionary) {
        int nonbullshitability = 0;
        int i = from;
        while (i < words.length) {
            int[] to = seekDictionaryPhrases(words, i, dictionary);
            if (to.length == 0) {
                ++nonbullshitability;
            } else {
                int remainingNonbullshitability = 1 + nonbullshitability(words, i + 1, dictionary);
                for (int j = 0; j < to.length; ++j) {
                    remainingNonbullshitability = Math.min(remainingNonbullshitability, nonbullshitability(words, to[j] + 1, dictionary));
                }
                return nonbullshitability + remainingNonbullshitability;
            }
            ++i;
        }
        return nonbullshitability;
    }

    private static int[] seekDictionaryPhrases(String[] words, int from, Dictionary dictionary) {
        List<Integer> to = new ArrayList<>();
        String phrase = "";
        Dictionary.Contains contains;
        int i = from;
        do {
            phrase += i > from ? SEPARATOR + words[i] : words[i];
            contains = dictionary.contains(phrase);
            if (contains == Dictionary.Contains.YES) {
                to.add(i);
            }
            ++i;
        } while (contains != Dictionary.Contains.NO && i < words.length);
        return to.stream().mapToInt(e -> e).toArray();
    }
}
