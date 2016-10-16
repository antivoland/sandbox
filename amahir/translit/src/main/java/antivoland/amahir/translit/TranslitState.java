package antivoland.amahir.translit;

import io.github.adrianulbona.hmm.State;

public class TranslitState implements State {
    public final String chars;

    public TranslitState(String chars) {
        this.chars = chars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TranslitState that = (TranslitState) o;

        return chars.equals(that.chars);
    }

    @Override
    public int hashCode() {
        return chars.hashCode();
    }
}
