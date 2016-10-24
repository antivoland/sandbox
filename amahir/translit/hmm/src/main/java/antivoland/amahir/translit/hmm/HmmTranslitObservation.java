package antivoland.amahir.translit.hmm;

import io.github.adrianulbona.hmm.Observation;

public class HmmTranslitObservation implements Observation {
    public final String chars;

    public HmmTranslitObservation(String chars) {
        this.chars = chars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HmmTranslitObservation that = (HmmTranslitObservation) o;

        return chars.equals(that.chars);
    }

    @Override
    public int hashCode() {
        return chars.hashCode();
    }
}
