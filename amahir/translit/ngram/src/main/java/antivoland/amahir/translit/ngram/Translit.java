package antivoland.amahir.translit.ngram;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Translit {
    private final static int N = 3;

    private final Map<String, List<String>> dictionary;
    private final Syllabifier syllabifier;
//    private final Map<Ngram, Double> ngramProbability;

    public Translit(Map<String, List<String>> dictionary, Map<String, Integer> wordFrequency) {
        this.dictionary = dictionary;
        this.syllabifier = new Syllabifier(dictionary.keySet());
//        this.ngramProbability =
    }



//    public String transliterate(String word) {
////        syllabifier.syllabify(word);
//    }
//    private final Map<String, Integer> ngramsFrequency

    private Map<Ngram, Double> ngramProbability(
            String word,
            int wordFrequency,
            Syllabifier syllabifier, int N) {

        List<Ngram> ngrams = new ArrayList<>();
        List<List<String>> forks = syllabifier.syllabify(word);
        for (List<String> fork : forks) {
            for (int i = N; i <= fork.size(); ++i) {
                ngrams.add(new Ngram(fork.subList(i - N, i)));
            }
        }
        return ngrams.stream()
                .collect(Collectors.groupingBy(g -> g, Collectors.summingDouble(t -> (double) wordFrequency / forks.size())));
    }
}
