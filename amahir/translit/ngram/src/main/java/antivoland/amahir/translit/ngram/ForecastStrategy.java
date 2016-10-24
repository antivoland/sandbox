package antivoland.amahir.translit.ngram;

import java.util.Comparator;

public enum ForecastStrategy implements Comparator<Transliterator.Transliteration> {
    INPUT((o1, o2) -> Double.compare(o1.inputProbability, o2.inputProbability)),
    OUTPUT((o1, o2) -> Double.compare(o1.outputProbability, o2.outputProbability)),
    BOTH((o1, o2) -> Double.compare(o1.bothProbability, o2.bothProbability));

    private final Comparator<Transliterator.Transliteration> comparator;

    ForecastStrategy(Comparator<Transliterator.Transliteration> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int compare(Transliterator.Transliteration o1, Transliterator.Transliteration o2) {
        return comparator.compare(o1, o2);
    }
}
