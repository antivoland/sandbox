package antivoland.amahir.translit.ngram;

import java.util.List;

public class Transliteration {
    public final List<String> input;
    public final List<String> output;

    public Transliteration(List<String> input, List<String> output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transliteration transliteration = (Transliteration) o;
        return input.equals(transliteration.input) && output.equals(transliteration.output);

    }

    @Override
    public int hashCode() {
        int result = input.hashCode();
        result = 31 * result + output.hashCode();
        return result;
    }
}
