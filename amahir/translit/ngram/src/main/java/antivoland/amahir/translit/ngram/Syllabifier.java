package antivoland.amahir.translit.ngram;

import java.util.*;

public class Syllabifier {
    private static class Dictionary {
        public enum Contains {YES, NO, MAYBE}

        private final TreeSet<String> syllables;

        public Dictionary(Set<String> syllables) {
            this.syllables = new TreeSet<>(syllables);
        }

        public Contains contains(String phrase) {
            String ceiling = syllables.ceiling(phrase);
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

    private final Dictionary dictionary;

    public Syllabifier(Set<String> syllables) {
        this.dictionary = new Dictionary(syllables);
    }

    public List<List<String>> syllabify(String word) {
        return syllabify(word, 0);
    }

    private List<List<String>> syllabify(String word, int from) {
        if (from >= word.length()) {
            return Collections.emptyList();
        }
        List<List<String>> forks = new ArrayList<>();
        for (String syllable : nextDictionarySyllables(word, from)) {
            List<List<String>> remaining = syllabify(word, from + syllable.length());
            if (remaining.isEmpty()) {
                forks.add(Collections.singletonList(syllable));
            } else {
                for (List<String> syllables : remaining) {
                    List<String> fork = new ArrayList<>();
                    fork.add(syllable);
                    fork.addAll(syllables);
                    forks.add(fork);
                }
            }
        }
        return forks;
    }

    private List<String> nextDictionarySyllables(String word, int from) {
        List<String> syllables = new ArrayList<>();
        String syllable = "";
        Dictionary.Contains contains;
        int i = from;
        do {
            syllable += word.charAt(i);
            contains = dictionary.contains(syllable);
            if (contains == Dictionary.Contains.YES) {
                syllables.add(syllable);
            }
            ++i;
        } while (contains != Dictionary.Contains.NO && i < word.length());
        return syllables;
    }
}
