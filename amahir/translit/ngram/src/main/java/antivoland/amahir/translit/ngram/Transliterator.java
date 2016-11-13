package antivoland.amahir.translit.ngram;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Transliterator {
    private static final String EMPTY_SYLLABLE = "";

    public class Transliteration {
        public final List<String> input;
        public final List<String> output;
        public final double likelihood;
        public final double inputProbability; // todo: remove
        public final double outputProbability; // todo: remove
        public final int lengthDiff; // todo: remove

        private Transliteration(List<String> input, List<String> output) {
            this.input = input;
            this.output = output;

            double inputProbability = inputCorpusForecaster.syllableSequenceProbability(input);
            double outputProbability = outputCorpusForecaster.syllableSequenceProbability(output);

            int inputLength = input.stream().collect(Collectors.summingInt(String::length));
            int outputLength = output.stream().collect(Collectors.summingInt(String::length));
            int lengthDiff = Math.abs(inputLength - outputLength);

            this.inputProbability = inputProbability; // todo: remove
            this.outputProbability = outputProbability; // todo: remove
            this.lengthDiff = lengthDiff; // todo: remove

            this.likelihood = forecastStrategy.likelihood(inputLength, outputLength, inputProbability, outputProbability, lengthDiff);
        }

        public String joinedOutput() {
            return String.join("", output);
        }

        @Override
        public String toString() {
            return "{" +
                    "input=" + input +
                    ", output=" + output +
                    ", likelihood=" + likelihood +
                    ", inputProbability=" + inputProbability +
                    ", outputProbability=" + outputProbability +
                    ", lengthDiff=" + lengthDiff +
                    '}';
        }
    }

    private final Syllabifier inputSyllabifier;
    private final Map<String, List<String>> inputOutputDictionary;
    private final CorpusForecaster inputCorpusForecaster;
    private final CorpusForecaster outputCorpusForecaster;
    private final ForecastStrategy forecastStrategy;
    private final boolean seekHiddenInputs; // todo: для имени pelmen нужно будет задавать количество искомых скрытых символов

    public Transliterator(
            Syllabifier inputSyllabifier,
            Map<String, List<String>> inputOutputDictionary,
            CorpusForecaster inputCorpusForecaster,
            CorpusForecaster outputCorpusForecaster,
            ForecastStrategy forecastStrategy,
            boolean seekHiddenInputs) {

        this.inputSyllabifier = inputSyllabifier;
        this.inputOutputDictionary = inputOutputDictionary;
        this.inputCorpusForecaster = inputCorpusForecaster;
        this.outputCorpusForecaster = outputCorpusForecaster;
        this.forecastStrategy = forecastStrategy;
        this.seekHiddenInputs = seekHiddenInputs;
    }

    public TreeSet<Transliteration> transliterate(String word) {
        TreeSet<Transliteration> transliterations = new TreeSet<>(forecastStrategy.reversed());
        List<List<String>> inputs = inputSyllabifier.syllabify(word);
        for (List<String> input : inputs) {
            List<List<String>> outputs = transliterate(input);
            if (seekHiddenInputs) {
                for (int i = 1; i <= input.size(); ++i) {
                    List<String> hiddenInput = new ArrayList<>(input);
                    hiddenInput.add(i, EMPTY_SYLLABLE);
                    outputs.addAll(transliterate(hiddenInput));
                }
            }
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
            List<String> fork = new ArrayList<>();
            if (!EMPTY_SYLLABLE.equals(outputSyllable)) {
                fork.add(outputSyllable);
            }
            if (remainingOutputs.isEmpty()) {
                outputs.add(fork);
            } else {
                remainingOutputs.forEach(remainingOutput -> outputs.add(
                        Stream.concat(fork.stream(), remainingOutput.stream()).collect(Collectors.toList())
                ));
            }
//            for (List<String> remainingOutput : remainingOutputs) {
//                outputs.add(Stream.concat(fork.stream(), remainingOutput.stream()).collect(Collectors.toList()));
//            }

//            if (remainingOutputs.isEmpty()) {
////                outputs.add(EMPTY_SYLLABLE.equals(outputSyllable) ? Collections.emptyList() : Collections.singletonList(outputSyllable));
////            } else {
//                for (List<String> remainingOutput : remainingOutputs) {
//                    List<String> fork = new ArrayList<>();
//                    if (!EMPTY_SYLLABLE.equals(outputSyllable)) {
//                        fork.add(outputSyllable);
//                    }
//                    fork.addAll(remainingOutput);
//                    outputs.add(fork);
//                }
//            }
        }
        return outputs;
    }

    @Override
    public String toString() {
        return "{" +
                "outputCorpusForecaster=" + outputCorpusForecaster +
                ", forecastStrategy=" + forecastStrategy +
                ", seekHiddenInputs=" + seekHiddenInputs +
                '}';
    }
}
