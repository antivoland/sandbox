package antivoland.amahir.translit.ngram;

public class WordFrequency {
    public final String word;
    public final double frequency;

    public WordFrequency(String word, int frequency, int power) {
        this.word = word;
        this.frequency = Math.pow(frequency, power);
    }
}
