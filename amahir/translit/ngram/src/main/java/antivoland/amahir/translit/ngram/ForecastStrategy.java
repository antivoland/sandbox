package antivoland.amahir.translit.ngram;

import java.util.Comparator;

public class ForecastStrategy implements Comparator<Transliterator.Transliteration> {
    private final double inputRate;
    private final double outputRate;

    public ForecastStrategy(double inputRate, double outputRate) {
        this.inputRate = inputRate;
        this.outputRate = outputRate;
    }

    public double probability(double inputProbability, double outputProbability) {
        return inputProbability * inputRate + outputProbability * outputRate;
    }

    @Override
    public int compare(Transliterator.Transliteration o1, Transliterator.Transliteration o2) {
        return Double.compare(o1.probability, o2.probability);
    }
}
