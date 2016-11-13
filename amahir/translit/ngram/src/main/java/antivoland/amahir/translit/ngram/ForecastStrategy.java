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

    public double likelihood(int inputLength, int outputLength, double inputProbability, double outputProbability, int lengthDiff) {
        // todo: hmmm
//        return inputProbability *outputProbability + inputRate * Math.pow(lengthDiff, 3) + lengthDiffRate*Math.pow(lengthDiff,2);

//        return inputProbability *outputProbability / Math.pow(-lengthDiff*inputRate-2, lengthDiffRate);

//        return inputProbability *outputProbability / Math.pow(lengthDiff*inputRate+2, lengthDiffRate);


//        return (inputProbability * Math.pow(100, inputRate-1) + outputProbability * Math.pow(100, outputRate-1))/
//                Math.pow(10, lengthDiff*lengthDiffRate);

//        return inputProbability * Math.pow(10000, inputRate-1) + outputProbability * Math.pow(10000, lengthDiffRate-1);
//        return lengthDiffRate*inputProbability * outputProbability /  Math.pow(inputRate, lengthDiff);

//        return (inputProbability * outputProbability *inputRate)/
//                Math.pow(lengthDiffRate, lengthDiff);

//        return Math.pow(inputRate, inputProbability*outputProbability)/Math.pow(lengthDiffRate, lengthDiff);


//        return inputProbability * outputProbability;

        /*
        double bothProbability = inputProbability * outputProbability;
        double meanLength = (inputLength + outputLength) / 2;
//        double minLength = Math.min(inputLength, outputLength);
        return Math.pow(bothProbability, Math.log(bothProbability)/meanLength);
//        return Math.pow(bothProbability, 1/meanLength);
*/

//        /* good choice!
//        double bothProbability = -1/Math.log(Math.pow(inputProbability,1.0/inputLength))
//                - 1/Math.log(Math.pow(outputProbability,1.0/outputLength));
        double bothProbability = -1/Math.log(inputProbability) - 1/Math.log(outputProbability);
        double meanLength = (inputLength + outputLength) / 2;
//        double minLength = Math.min(inputLength, outputLength);
//        return bothProbability*1/meanLength;
        return bothProbability;///Math.pow(1.5, lengthDiff+1);
//        */



//        return (inputProbability * inputRate + outputProbability * outputRate)/  Math.pow(10, (lengthDiff+1)*lengthDiffRate);
//        return Math.pow(inputProbability *outputProbability,(lengthDiff+1)*lengthDiffRate);
//        return (inputProbability * inputRate + outputProbability * outputRate) / Math.pow(1000, lengthDiff);
    //        return Math.pow(inputProbability * inputRate + outputProbability * outputRate,lengthDiff* lengthDiffRate +1);
//        return Math.log(inputProbability)*inputRate + Math.log(outputProbability)*outputRate;
//        return Math.pow(inputProbability * inputRate + outputProbability * outputRate,lengthDiff+1);//)/(lengthDiff+1);// / Math.pow(lengthDiffRate, lengthDiff);
//        return Math.pow(inputProbability * outputProbability , lengthDiff + 1);
//        return -(1/Math.log(inputProbability*inputRate) +1/Math.log(outputProbability*outputRate))/ lengthDiffRate/lengthDiff;
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
