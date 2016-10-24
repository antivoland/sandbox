package antivoland.amahir.translit.hmm;

import io.github.adrianulbona.hmm.State;

public class HmmTranslitState implements State {
    public final String chars;

    public HmmTranslitState(String chars) {
        this.chars = chars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HmmTranslitState that = (HmmTranslitState) o;

        return chars.equals(that.chars);
    }

    @Override
    public int hashCode() {
        return chars.hashCode();
    }
}
