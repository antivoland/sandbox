package antivoland.amahir.translit;

import io.github.adrianulbona.hmm.Observation;

public class TranslitObservation implements Observation {
    public final String chars;

    public TranslitObservation(String chars) {
        this.chars = chars;
    }
}
