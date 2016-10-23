package antivoland.amahir.translit.ngram;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Ngrams {
    public static Map<Ngram, Double> ruNgramProbabilities(int N) throws Exception {
        return ngramProbabilities(Words.ruWordFrequencies(), Syllabifiers.ru(), N);
    }

    public static Map<Ngram, Double> enNgramProbabilities(int N) throws Exception {
        return ngramProbabilities(Words.enWordFrequencies(), Syllabifiers.en(), N);
    }

    private static Map<Ngram, Double> ngramProbabilities(Map<String, Integer> wordFrequencies, Syllabifier syllabifier, int N) {
        Map<Ngram, Double> ngramFrequency = wordFrequencies.entrySet().stream()
                .flatMap(wf -> ngramFrequency(wf.getKey(), wf.getValue(), syllabifier, N).entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingDouble(Map.Entry::getValue)));

        Double total = ngramFrequency.values().stream()
                .collect(Collectors.summingDouble(f -> f));

        return ngramFrequency.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, nf -> nf.getValue() / total));
    }

    private static Map<Ngram, Double> ngramFrequency(String word, int wordFrequency, Syllabifier syllabifier, int N) {
        List<Ngram> ngrams = new ArrayList<>();
        List<List<String>> forks = syllabifier.syllabify(word);
        for (List<String> fork : forks) {
            for (int i = 0; i <= fork.size(); ++i) {
                ngrams.add(new Ngram(fork.subList(Math.max(0, i - N), i)));
            }
        }
        return ngrams.stream()
                .collect(Collectors.groupingBy(g -> g, Collectors.summingDouble(t -> (double) wordFrequency / forks.size())));
    }
}
