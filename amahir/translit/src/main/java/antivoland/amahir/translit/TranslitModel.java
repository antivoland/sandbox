package antivoland.amahir.translit;

import io.github.adrianulbona.hmm.Emission;
import io.github.adrianulbona.hmm.Model;
import io.github.adrianulbona.hmm.ReachableStateFinder;
import io.github.adrianulbona.hmm.Transition;
import io.github.adrianulbona.hmm.probability.EmissionProbabilityCalculator;
import io.github.adrianulbona.hmm.probability.ProbabilityCalculator;
import io.github.adrianulbona.hmm.probability.StartProbabilityCalculator;
import io.github.adrianulbona.hmm.probability.TransitionProbabilityCalculator;
import io.github.adrianulbona.hmm.solver.MostProbableStateSequenceFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TranslitModel {
    private final TranslitSequencer observationSequencer;
    private final TranslitSequencer stateSequencer;
    private final Model<TranslitState, TranslitObservation> model;

    public TranslitModel(
            List<Emission<TranslitState, TranslitObservation>> emissions,
            Map<String, Integer> wordFrequency, TranslitSequencer stateSequencer, TranslitSequencer observationSequencer) {

        this.observationSequencer = observationSequencer;
        this.stateSequencer = stateSequencer;
        this.model = new Model<>(
                probabilityCalculator(emissions, wordFrequency),
                reachableStatesFinder(emissions));
    }

    public String translit(String word) {
        List<TranslitObservation> observations = observationSequencer.allSequences(word).stream()
                .map(TranslitObservation::new)
                .collect(Collectors.toList());
        List<TranslitState> states = new MostProbableStateSequenceFinder<>(model).basedOn(observations);
        return String.join("", states.stream().map(s -> s.chars).collect(Collectors.toList()));
    }

    private ProbabilityCalculator<TranslitState, TranslitObservation> probabilityCalculator(
            List<Emission<TranslitState, TranslitObservation>> emissions,
            Map<String, Integer> wordFrequency) {

        return new ProbabilityCalculator<>(
                startProbabilityCalculator(wordFrequency),
                emissionProbabilityCalculator(emissions),
                transitionProbabilityCalculator(wordFrequency));
    }

    private StartProbabilityCalculator<TranslitState> startProbabilityCalculator(
            Map<String, Integer> wordFrequency) {

        Map<String, Integer> startFrequency = wordFrequency.entrySet().stream()
                .collect(Collectors.groupingBy(e -> stateSequencer.firstSequence(e.getKey()), Collectors.summingInt(Map.Entry::getValue)));
        Integer total = startFrequency.values().stream()
                .collect(Collectors.summingInt(e -> e));
        Map<TranslitState, Double> startProbability = startFrequency.entrySet().stream()
                .collect(Collectors.toMap(e -> new TranslitState(e.getKey()), e -> (double) e.getValue() / total));
        return startProbability::get;
    }

    private EmissionProbabilityCalculator<TranslitState, TranslitObservation> emissionProbabilityCalculator(
            List<Emission<TranslitState, TranslitObservation>> emissions) {

        Map<TranslitState, Integer> stateFrequency = emissions.stream()
                .collect(Collectors.groupingBy(Emission::getState, Collectors.summingInt(e -> 1)));
        Integer total = stateFrequency.values().stream()
                .collect(Collectors.summingInt(e -> e));
        Map<Emission<TranslitState, TranslitObservation>, Double> emissionProbability = emissions.stream()
                .collect(Collectors.toMap(e -> e, e -> (double) stateFrequency.get(e.getState()) / total));
        return emissionProbability::get;
    }

    private TransitionProbabilityCalculator<TranslitState> transitionProbabilityCalculator(
            Map<String, Integer> wordFrequency) {

        Map<Transition<TranslitState>, Integer> transitionFrequency = wordFrequency.entrySet().stream()
                .flatMap(wf -> transitionFrequency(wf.getKey(), wf.getValue(), stateSequencer).entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingInt(Map.Entry::getValue)));
        Map<TranslitState, Integer> total = transitionFrequency.entrySet().stream()
                .collect(Collectors.groupingBy(t -> t.getKey().getFrom(), Collectors.summingInt(Map.Entry::getValue)));
        Map<Transition<TranslitState>, Double> transitionProbability = transitionFrequency.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, tf -> (double) tf.getValue() / total.get(tf.getKey().getFrom())));
        return transitionProbability::get;
    }

    private Map<Transition<TranslitState>, Integer> transitionFrequency(
            String word,
            int wordFrequency,
            TranslitSequencer sequencer) {

        List<String> sequences = sequencer.allSequences(word);
        List<Transition<TranslitState>> transitions = new ArrayList<>();
        for (int i = 1; i < sequences.size(); ++i) {
            TranslitState from = new TranslitState(sequences.get(i - 1));
            TranslitState to = new TranslitState(sequences.get(i));
            transitions.add(new Transition<>(from, to));
        }
        return transitions.stream()
                .collect(Collectors.groupingBy(t -> t, Collectors.summingInt(t -> wordFrequency)));
    }

    private ReachableStateFinder<TranslitState, TranslitObservation> reachableStatesFinder(
            List<Emission<TranslitState, TranslitObservation>> emissions) {

        Map<TranslitObservation, List<TranslitState>> reachableStates = emissions.stream()
                .collect(Collectors.groupingBy(Emission::getObservation)).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream()
                                .map(Emission::getState)
                                .collect(Collectors.toList()))
                );
        return reachableStates::get;
    }
}
