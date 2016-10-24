package antivoland.amahir.translit.ngram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Transliterator {
    private final Syllabifier inputSyllabifier;
    private final Map<String, List<String>> inputOutputDictionary;

    public Transliterator(Syllabifier inputSyllabifier, Map<String, List<String>> inputOutputDictionary) {
        this.inputSyllabifier = inputSyllabifier;
        this.inputOutputDictionary = inputOutputDictionary;
    }

    public List<Transliteration> transliterate(String word) {
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
