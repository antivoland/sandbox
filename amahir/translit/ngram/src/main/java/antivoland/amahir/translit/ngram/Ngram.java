package antivoland.amahir.translit.ngram;

import java.util.List;

public class Ngram {
    public enum Align {MIDDLE, RIGHT}

    public final Align align;
    public final List<String> syllables;

    public Ngram(Align align, List<String> syllables) {
        this.align = align;
        this.syllables = syllables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ngram ngram = (Ngram) o;
        return align == ngram.align && syllables.equals(ngram.syllables);

    }

    @Override
    public int hashCode() {
        int result = align.hashCode();
        result = 31 * result + syllables.hashCode();
        return result;
    }
}
