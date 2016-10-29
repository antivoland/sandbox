package antivoland.amahir.translit.ngram;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CorpusForecaster {
    private final Map<Ngram, Double> ngramProbabilities;
    private final int N;
    private final double epsilon;

    public CorpusForecaster(Stream<WordFrequency> wordFrequencies, Syllabifier syllabifier, int N) {
        Map<Ngram, Double> ngramFrequency = wordFrequencies
                .flatMap(wf -> ngramFrequencies(wf, syllabifier, N).entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingDouble(Map.Entry::getValue)));
        Double total = ngramFrequency.values().stream()
                .collect(Collectors.summingDouble(f -> f));
        this.ngramProbabilities = ngramFrequency.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, nf -> nf.getValue() / total));
        this.N = N;
        this.epsilon = this.ngramProbabilities.values().stream().min(Double::compare).get();
    }

    public Double syllableSequenceProbability(List<String> syllables) {
        Double wordProbability = 1.0;
        int m = 1;
        while (m <= syllables.size()) {
            Ngram ngram = new Ngram(Ngram.Align.MIDDLE, syllables.subList(Math.max(0, m - N), m));
            Double ngramProbability = ngramProbabilities.get(ngram);
            wordProbability *= ngramProbability != null ? ngramProbability : epsilon;
            ++m;
        }

        int r = Math.max(0, syllables.size() - N + 1);
        while (r < syllables.size()) {
            Ngram ngram = new Ngram(Ngram.Align.RIGHT, syllables.subList(r, syllables.size()));
            Double ngramProbability = ngramProbabilities.get(ngram);
            wordProbability *= ngramProbability != null ? ngramProbability : epsilon;
            ++r;
        }
        return wordProbability;
    }

    private static Map<Ngram, Double> ngramFrequencies(WordFrequency wordFrequency, Syllabifier syllabifier, int N) {
        List<Ngram> ngrams = new ArrayList<>();
        List<List<String>> forks = syllabifier.syllabify(wordFrequency.word);
        for (List<String> fork : forks) {
            int m = 1;
            while (m <= fork.size()) {
                ngrams.add(new Ngram(Ngram.Align.MIDDLE, fork.subList(Math.max(0, m - N), m)));
                ++m;
            }

            int r = Math.max(0, fork.size() - N + 1);
            while (r < fork.size()) {
                ngrams.add(new Ngram(Ngram.Align.RIGHT, fork.subList(r, fork.size())));
                ++r;
            }
        }
        return ngrams.stream()
                .collect(Collectors.groupingBy(ngram -> ngram, Collectors.summingDouble(ngram ->
                        (double) wordFrequency.frequency / forks.size() / ngram.syllables.size())));
    }
}
