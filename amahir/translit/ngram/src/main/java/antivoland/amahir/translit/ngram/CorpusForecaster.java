package antivoland.amahir.translit.ngram;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CorpusForecaster {
    private final Map<Ngram, Double> ngramProbabilities;
    private final int N;

    public CorpusForecaster(Map<String, Integer> wordFrequencies, Syllabifier syllabifier, int N) {
        Map<Ngram, Double> ngramFrequency = wordFrequencies.entrySet().stream()
                .flatMap(wf -> ngramFrequency(wf.getKey(), wf.getValue(), syllabifier, N).entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingDouble(Map.Entry::getValue)));
        Double total = ngramFrequency.values().stream()
                .collect(Collectors.summingDouble(f -> f));
        this.ngramProbabilities = ngramFrequency.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, nf -> nf.getValue() / total));
        this.N = N;
    }

    public Double syllableSequenceProbability(List<String> syllables) {
        Double wordProbability = 1.0;
        for (int i = 0; i < syllables.size(); ++i) {
            Ngram ngram = new Ngram(syllables.subList(Math.max(0, i - N), i));
            Double ngramProbability = ngramProbabilities.get(ngram);
            wordProbability *= ngramProbability != null ? ngramProbability : 0; // todo: epsilon
        }
        return wordProbability;
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
