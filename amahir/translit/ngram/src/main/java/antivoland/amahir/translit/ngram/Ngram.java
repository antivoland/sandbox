package antivoland.amahir.translit.ngram;

import java.util.List;

public class Ngram {
    private final List<String> syllables;

    public Ngram(List<String> syllables) {
        this.syllables = syllables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ngram ngram = (Ngram) o;

        return syllables.equals(ngram.syllables);

    }

    @Override
    public int hashCode() {
        return syllables.hashCode();
    }
}
