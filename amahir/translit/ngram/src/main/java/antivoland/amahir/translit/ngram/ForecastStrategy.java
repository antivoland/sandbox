package antivoland.amahir.translit.ngram;

import java.util.Comparator;

public class ForecastStrategy implements Comparator<Transliterator.Transliteration> {
    private final double inputRate;
    private final double outputRate;
    private final double lengthDiffRate;

    public ForecastStrategy(double inputRate, double outputRate, double lengthDiffRate) {
        this.inputRate = inputRate;
        this.outputRate = outputRate;
        this.lengthDiffRate = lengthDiffRate;
    }

    public double likelihood(double inputProbability, double outputProbability, int lengthDiff) {
        // todo: hmmm
//        return -Math.log(inputProbability * inputRate + outputProbability * outputRate)/(lengthDiff+1);// / Math.pow(lengthDiffRate, lengthDiff);
//        return inputProbability * inputRate + outputProbability * outputRate;
//        return (inputProbability * inputRate + outputProbability * outputRate) / Math.pow(1000, lengthDiff);
        return Math.pow(inputProbability * inputRate + outputProbability * outputRate,lengthDiff* lengthDiffRate +1);
//        return Math.log(inputProbability)*inputRate + Math.log(outputProbability)*outputRate;
//        return Math.pow(inputProbability * inputRate + outputProbability * outputRate,lengthDiff+1);//)/(lengthDiff+1);// / Math.pow(lengthDiffRate, lengthDiff);
//        return Math.pow(inputProbability * outputProbability , lengthDiff + 1);
    }

    @Override
    public int compare(Transliterator.Transliteration o1, Transliterator.Transliteration o2) {
        return Double.compare(o1.likelihood, o2.likelihood);
    }

    @Override
    public String toString() {
        return "{" +
                "inputRate=" + inputRate +
                ", outputRate=" + outputRate +
                ", lengthDiffRate=" + lengthDiffRate +
                '}';
    }
}
