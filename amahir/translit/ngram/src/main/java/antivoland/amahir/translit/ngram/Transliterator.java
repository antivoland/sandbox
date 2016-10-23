package antivoland.amahir.translit.ngram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Transliterator {
    private final Map<String, List<String>> dictionary;

    public Transliterator(Map<String, List<String>> dictionary) {
        this.dictionary = dictionary;
    }

    public List<List<String>> transliterate(List<String> word) {
        return transliterate(word, 0);
    }

    private List<List<String>> transliterate(List<String> word, int from) {
        if (from >= word.size()) {
            return Collections.emptyList();
        }

        List<List<String>> forks = new ArrayList<>();
        List<String> transliterations = dictionary.get(word.get(from));
        for (String transliteration : transliterations) {
            List<List<String>> remainingTransliterations = transliterate(word, from + 1);
            if (remainingTransliterations.isEmpty()) {
                forks.add(Collections.singletonList(transliteration));
            } else {
                for (List<String> remainingTransliteration : remainingTransliterations) {
                    List<String> fork = new ArrayList<>();
                    fork.add(transliteration);
                    fork.addAll(remainingTransliteration);
                    forks.add(fork);
                }
            }
        }
        return forks;
    }
}
