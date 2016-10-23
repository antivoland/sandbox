package antivoland.amahir.translit;

import io.github.adrianulbona.hmm.Observation;

public class TranslitObservation implements Observation {
    public final String chars;

    public TranslitObservation(String chars) {
        this.chars = chars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TranslitObservation that = (TranslitObservation) o;

        return chars.equals(that.chars);
    }

    @Override
    public int hashCode() {
        return chars.hashCode();
    }
}
