package antivoland.amahir.translit.ngram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Transliterator {
    private static final Logger LOG = LoggerFactory.getLogger(Transliterator.class);
    private static final String EMPTY_SYLLABLE = "";

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
    private final CorpusForecaster inputCorpusForecaster;
    private final CorpusForecaster outputCorpusForecaster;
    private final boolean seekHiddenInputs; // todo: для имени pelmen нужно будет задавать количество искомых скрытых симаолов

    public Transliterator(
            Syllabifier inputSyllabifier,
            Map<String, List<String>> inputOutputDictionary,
            CorpusForecaster inputCorpusForecaster,
            CorpusForecaster outputCorpusForecaster,
            boolean seekHiddenInputs) {

        this.inputSyllabifier = inputSyllabifier;
        this.inputOutputDictionary = inputOutputDictionary;
        this.inputCorpusForecaster = inputCorpusForecaster;
        this.outputCorpusForecaster = outputCorpusForecaster;
        this.seekHiddenInputs = seekHiddenInputs;
    }

    public String transliterate(String word, ForecastStrategy forecastStrategy) {
        List<Transliteration> transliterations = transliterate(word);
        if (LOG.isDebugEnabled()) {
            transliterations.stream()
                    .sorted(forecastStrategy.reversed())
                    .forEach(t -> LOG.debug("{} -> {} ({} = {} * {})", t.input, t.output, t.bothProbability, t.inputProbability, t.outputProbability));
        }
        return String.join("", transliterations.stream().max(forecastStrategy).get().output);
    }

    private List<Transliteration> transliterate(String word) {
        List<Transliteration> transliterations = new ArrayList<>();
        List<List<String>> inputs = inputSyllabifier.syllabify(word);

        if (seekHiddenInputs) {
            List<List<String>> hiddenInputs = new ArrayList<>();
            for (List<String> input : inputs) {
                for (int i = 1; i < input.size(); ++i) {
                    List<String> hiddenInput = new ArrayList<>(input);
                    hiddenInput.add(i, EMPTY_SYLLABLE);
                    hiddenInputs.add(hiddenInput);
                }
            }
            inputs.addAll(hiddenInputs);
        }

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
        if (outputSyllables == null) {
            outputSyllables = Collections.singletonList(EMPTY_SYLLABLE);
        }
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
