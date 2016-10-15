package antivoland.amahir.translit;

import io.github.adrianulbona.hmm.Emission;
import io.github.adrianulbona.hmm.Model;
import io.github.adrianulbona.hmm.ReachableStateFinder;
import io.github.adrianulbona.hmm.Transition;
import io.github.adrianulbona.hmm.probability.EmissionProbabilityCalculator;
import io.github.adrianulbona.hmm.probability.ProbabilityCalculator;
import io.github.adrianulbona.hmm.probability.StartProbabilityCalculator;
import io.github.adrianulbona.hmm.probability.TransitionProbabilityCalculator;

import java.util.*;
import java.util.stream.Collectors;

public class TranslitModel extends Model<TranslitState, TranslitObservation> {
    public TranslitModel(Map<String, Set<String>> dictionary, Map<String, Integer> wordFrequency) {
        super(probabilityCalculator(dictionary, wordFrequency), reachableStatesFinder(dictionary));
    }

    private static ProbabilityCalculator<TranslitState, TranslitObservation> probabilityCalculator(Map<String, Set<String>> dictionary, Map<String, Integer> wordFrequency) {
        return new ProbabilityCalculator<>(
                startProbabilityCalculator(dictionary, wordFrequency),
                emissionProbabilityCalculator(dictionary),
                transitionProbabilityCalculator(dictionary, wordFrequency));
    }

    public static List<TranslitObservation> parse(String word, Map<String, Set<String>> dictionary) {
        return charSequences(word, dictionary).stream().map(e->new TranslitObservation(e)).collect(Collectors.toList());
    }

    private static ReachableStateFinder<TranslitState, TranslitObservation> reachableStatesFinder(Map<String, Set<String>> dictionary) {
        Map<String, List<Emission<TranslitState, TranslitObservation>>> reversed = dictionary.entrySet().stream().flatMap(e -> e.getValue().stream()
                .map(v -> new Emission<>(new TranslitState(e.getKey()), new TranslitObservation(v))))
                .collect(Collectors.groupingBy(em -> em.getObservation().chars));
        return observation -> {
            return reversed.get(observation.chars).stream().map(e -> e.getState()).collect(Collectors.toList());
        };
    }

    private static EmissionProbabilityCalculator<TranslitState, TranslitObservation> emissionProbabilityCalculator(Map<String, Set<String>> dictionary) {
        Map<String, List<Emission<TranslitState, TranslitObservation>>> reversed = dictionary.entrySet().stream().flatMap(e -> e.getValue().stream()
                .map(v -> new Emission<>(new TranslitState(e.getKey()), new TranslitObservation(v))))
                .collect(Collectors.groupingBy(em -> em.getObservation().chars));


        Map<Emission<TranslitState, TranslitObservation>, Double> emissionProbabilities = new HashMap<>();
        for (List<Emission<TranslitState, TranslitObservation>> emissions : reversed.values()) {
            for (Emission<TranslitState, TranslitObservation> emission : emissions) {
                emissionProbabilities.put(emission, (double) 1 / emissions.size());
            }
        }
        return emission -> emissionProbabilities.get(emission);
    }

    private static StartProbabilityCalculator<TranslitState> startProbabilityCalculator(Map<String, Set<String>> dictionary, Map<String, Integer> wordFrequency) {
        Map<String, Integer> startFrequency = wordFrequency.entrySet().stream().collect(Collectors.groupingBy(e -> firstCharSequence(e.getKey(), dictionary), Collectors.summingInt(Map.Entry::getValue)));
        Integer total = startFrequency.values().stream().collect(Collectors.summingInt(e -> e));
        Map<String, Double> startProbability = startFrequency.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (double) e.getValue() / total));
        return state -> startProbability.get(state.chars);
    }

    private static TransitionProbabilityCalculator<TranslitState> transitionProbabilityCalculator(Map<String, Set<String>> dictionary, Map<String, Integer> wordFrequency) {
        Map<Transition<TranslitState>, Integer> transitionFrequency = wordFrequency.entrySet().stream()
                .flatMap(e -> transitionFrequency(e.getKey(), e.getValue(), dictionary).entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingInt(Map.Entry::getValue)));

        Integer total = transitionFrequency.values().stream().collect(Collectors.summingInt(e -> e));
        Map<Transition<TranslitState>, Double> transitionProbability = transitionFrequency.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> (double) e.getValue() / total));
        return transition -> transitionProbability.get(transition);
    }

    public static Map<Transition<TranslitState>, Integer> transitionFrequency(String word, int wordFrequency, Map<String, Set<String>> dictionary) {
        List<String> charSequences = charSequences(word, dictionary);
        List<Transition<TranslitState>> transitions = new ArrayList<>();
        for (int i = 1; i < charSequences.size(); ++i) {
            transitions.add(new Transition<>(new TranslitState(charSequences.get(i - 1)), new TranslitState(charSequences.get(i))));
        }
        return transitions.stream().collect(Collectors.toMap(t -> t, t -> 1)).entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingInt(Map.Entry::getValue)))
                .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue() * wordFrequency));
    }

    public static String firstCharSequence(String word, Map<String, Set<String>> dictionary) {
        return nextCharSequence(word, 0, dictionary);
    }

    public static List<String> charSequences(String word, Map<String, Set<String>> dictionary) {
        List<String> charSequences = new ArrayList<>();
        int from = 0;
        while (from <= word.length() - 1) {
            String chars = nextCharSequence(word, from, dictionary);
            charSequences.add(chars);
            from += chars.length();
        }
        return charSequences;
    }

    private static String nextCharSequence(String word, int from, Map<String, Set<String>> dictionary) {
        int to = from;
        do {
            ++to;
        } while (to <= word.length() && dictionary.containsKey(word.substring(from, to)));
        return from < to ? word.substring(from, to - 1) : null;
    }
}
