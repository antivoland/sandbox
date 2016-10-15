package antivoland.amahir.translit;

import io.github.adrianulbona.hmm.State;

public class TranslitState implements State {
    public final String chars;

    public TranslitState(String chars) {
        this.chars = chars;
    }
}
