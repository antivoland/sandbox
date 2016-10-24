package antivoland.amahir.translit.ngram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Transliterator {
    public class Transliteration {
        public final List<String> input;
        public final List<String> output;
        public final Double inputProbability;
        public final Double outputProbability;
        public final Double bothProbability;

        private Transliteration(List<String> input, List<String> output) {
            this.input = input;
            this.output = output;
            this.inputProbability = inputCorpusForecaster.syllableSequenceProbability(input);
            this.outputProbability = outputCorpusForecaster.syllableSequenceProbability(output);
            this.bothProbability = this.inputProbability * this.outputProbability;
        }
    }

    private final Syllabifier inputSyllabifier;
    private final Map<String, List<String>> inputOutputDictionary;
    private CorpusForecaster inputCorpusForecaster;
    private CorpusForecaster outputCorpusForecaster;

    public Transliterator(
            Syllabifier inputSyllabifier,
            Map<String, List<String>> inputOutputDictionary,
            CorpusForecaster inputCorpusForecaster,
            CorpusForecaster outputCorpusForecaster) {

        this.inputSyllabifier = inputSyllabifier;
        this.inputOutputDictionary = inputOutputDictionary;
        this.inputCorpusForecaster = inputCorpusForecaster;
        this.outputCorpusForecaster = outputCorpusForecaster;
    }

    public String transliterate(String word, ForecastStrategy forecastStrategy) {
        return String.join("",transliterate(word).stream().max(forecastStrategy).get().output);
    }

    private List<Transliteration> transliterate(String word) {
        List<Transliteration> transliterations = new ArrayList<>();
        List<List<String>> inputs = inputSyllabifier.syllabify(word);
        for (List<String> input : inputs) {
            List<List<String>> outputs = transliterate(input);
            transliterations.addAll(
                    outputs.stream()
                            .map(output -> new Transliteration(input, output))
                            .collect(Collectors.toList())
            );
        }
        return transliterations;
    }

    private List<List<String>> transliterate(List<String> input) {
        return transliterate(input, 0);
    }

    private List<List<String>> transliterate(List<String> input, int from) {
        if (from >= input.size()) {
            return Collections.emptyList();
        }

        List<List<String>> outputs = new ArrayList<>();
        List<String> outputSyllables = inputOutputDictionary.get(input.get(from));
        for (String outputSyllable : outputSyllables) {
            List<List<String>> remainingOutputs = transliterate(input, from + 1);
            if (remainingOutputs.isEmpty()) {
                outputs.add(Collections.singletonList(outputSyllable));
            } else {
                for (List<String> remainingOutput : remainingOutputs) {
                    List<String> fork = new ArrayList<>();
                    fork.add(outputSyllable);
                    fork.addAll(remainingOutput);
                    outputs.add(fork);
                }
            }
        }
        return outputs;
    }
}
