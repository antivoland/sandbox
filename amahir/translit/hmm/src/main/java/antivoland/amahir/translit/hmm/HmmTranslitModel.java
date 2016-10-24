package antivoland.amahir.translit.hmm;

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

public class HmmTranslitModel {
    private final HmmTranslitSequencer observationSequencer;
    private final HmmTranslitSequencer stateSequencer;
    private final Model<HmmTranslitState, HmmTranslitObservation> model;

    public HmmTranslitModel(
            List<Emission<HmmTranslitState, HmmTranslitObservation>> emissions,
            Map<String, Integer> wordFrequency, HmmTranslitSequencer stateSequencer, HmmTranslitSequencer observationSequencer) {

        this.observationSequencer = observationSequencer;
        this.stateSequencer = stateSequencer;
        this.model = new Model<>(
                probabilityCalculator(emissions, wordFrequency),
                reachableStatesFinder(emissions));
    }

    public String translit(String word) {
        List<HmmTranslitObservation> observations = observationSequencer.allSequences(word).stream()
                .map(HmmTranslitObservation::new)
                .collect(Collectors.toList());
        List<HmmTranslitState> states = new MostProbableStateSequenceFinder<>(model).basedOn(observations);
        return String.join("", states.stream().map(s -> s.chars).collect(Collectors.toList()));
    }

    private ProbabilityCalculator<HmmTranslitState, HmmTranslitObservation> probabilityCalculator(
            List<Emission<HmmTranslitState, HmmTranslitObservation>> emissions,
            Map<String, Integer> wordFrequency) {

        return new ProbabilityCalculator<>(
                startProbabilityCalculator(wordFrequency),
                emissionProbabilityCalculator(emissions),
                transitionProbabilityCalculator(wordFrequency));
    }

    private StartProbabilityCalculator<HmmTranslitState> startProbabilityCalculator(
            Map<String, Integer> wordFrequency) {

        Map<String, Integer> startFrequency = wordFrequency.entrySet().stream()
                .collect(Collectors.groupingBy(e -> stateSequencer.firstSequence(e.getKey()), Collectors.summingInt(Map.Entry::getValue)));
        Integer total = startFrequency.values().stream()
                .collect(Collectors.summingInt(e -> e));
        Map<HmmTranslitState, Double> startProbability = startFrequency.entrySet().stream()
                .collect(Collectors.toMap(e -> new HmmTranslitState(e.getKey()), e -> (double) e.getValue() / total));
        return startProbability::get;
    }

    private EmissionProbabilityCalculator<HmmTranslitState, HmmTranslitObservation> emissionProbabilityCalculator(
            List<Emission<HmmTranslitState, HmmTranslitObservation>> emissions) {

        Map<HmmTranslitState, Integer> stateFrequency = emissions.stream()
                .collect(Collectors.groupingBy(Emission::getState, Collectors.summingInt(e -> 1)));
        Integer total = stateFrequency.values().stream()
                .collect(Collectors.summingInt(e -> e));
        Map<Emission<HmmTranslitState, HmmTranslitObservation>, Double> emissionProbability = emissions.stream()
                .collect(Collectors.toMap(e -> e, e -> (double) stateFrequency.get(e.getState()) / total));
        return emissionProbability::get;
    }

    private TransitionProbabilityCalculator<HmmTranslitState> transitionProbabilityCalculator(
            Map<String, Integer> wordFrequency) {

        Map<Transition<HmmTranslitState>, Integer> transitionFrequency = wordFrequency.entrySet().stream()
                .flatMap(wf -> transitionFrequency(wf.getKey(), wf.getValue(), stateSequencer).entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingInt(Map.Entry::getValue)));
        Map<HmmTranslitState, Integer> total = transitionFrequency.entrySet().stream()
                .collect(Collectors.groupingBy(t -> t.getKey().getFrom(), Collectors.summingInt(Map.Entry::getValue)));
        Map<Transition<HmmTranslitState>, Double> transitionProbability = transitionFrequency.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, tf -> (double) tf.getValue() / total.get(tf.getKey().getFrom())));
        return transitionProbability::get;
    }

    private Map<Transition<HmmTranslitState>, Integer> transitionFrequency(
            String word,
            int wordFrequency,
            HmmTranslitSequencer sequencer) {

        List<String> sequences = sequencer.allSequences(word);
        List<Transition<HmmTranslitState>> transitions = new ArrayList<>();
        for (int i = 1; i < sequences.size(); ++i) {
            HmmTranslitState from = new HmmTranslitState(sequences.get(i - 1));
            HmmTranslitState to = new HmmTranslitState(sequences.get(i));
            transitions.add(new Transition<>(from, to));
        }
        return transitions.stream()
                .collect(Collectors.groupingBy(t -> t, Collectors.summingInt(t -> wordFrequency)));
    }

    private ReachableStateFinder<HmmTranslitState, HmmTranslitObservation> reachableStatesFinder(
            List<Emission<HmmTranslitState, HmmTranslitObservation>> emissions) {

        Map<HmmTranslitObservation, List<HmmTranslitState>> reachableStates = emissions.stream()
                .collect(Collectors.groupingBy(Emission::getObservation)).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream()
                                .map(Emission::getState)
                                .collect(Collectors.toList()))
                );
        return reachableStates::get;
    }
}
