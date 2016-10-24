package antivoland.amahir.translit.hmm;

import io.github.adrianulbona.hmm.Emission;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HmmTranslitSequencer {
    private final Set<String> sequences;

    public HmmTranslitSequencer(List<Emission<HmmTranslitState, HmmTranslitObservation>> emissions) {
        this.sequences = emissions.stream()
                .map(e -> e.getState().chars)
                .collect(Collectors.toSet());
    }

    public String firstSequence(String word) {
        return nextSequence(word, 0);
    }

    public List<String> allSequences(String word) {
        List<String> charSequences = new ArrayList<>();
        int from = 0;
        while (from <= word.length() - 1) {
            String chars = nextSequence(word, from);
            charSequences.add(chars);
            from += chars.length() > 0 ? chars.length() : 1;
        }
        return charSequences;
    }

    private String nextSequence(String word, int from) {
        int to = from;
        do {
            ++to;
        } while (to <= word.length() && sequences.contains(word.substring(from, to)));
        return from < to ? word.substring(from, to - 1) : null;
    }
}
