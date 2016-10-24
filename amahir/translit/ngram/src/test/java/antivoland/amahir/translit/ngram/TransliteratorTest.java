package antivoland.amahir.translit.ngram;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class TransliteratorTest {
    private static final Logger LOG = LoggerFactory.getLogger(TransliteratorTest.class);

    @Test
    public void test() throws Exception {
        Transliterator enRuTransliterator = new Transliterator(Syllabifiers.en(), Dictionaries.enRu());
        List<Transliteration> transliterations = enRuTransliterator.transliterate("inessa");

        int N = 3;
        Map<Ngram, Double> ruNgramProbabilities = Ngrams.ruNgramProbabilities(N);
        Map<Ngram, Double> enNgramProbabilities = Ngrams.enNgramProbabilities(N);

        for (Transliteration transliteration : transliterations) {
            Double inputProbability = wordProbability(transliteration.input, enNgramProbabilities, N);
            Double outputProbability = wordProbability(transliteration.output, ruNgramProbabilities, N);
            LOG.info("{} ({}) -> {} ({}) -> {}",
                    transliteration.input,
                    inputProbability,
                    transliteration.output,
                    outputProbability,
                    inputProbability * outputProbability
            );
        }
    }

    private Double wordProbability(List<String> word, Map<Ngram, Double> ngramProbabilities, int N) {
        Double wordProbability = 1.0;
        for (int i = 0; i < word.size(); ++i) {
            Ngram ngram = new Ngram(word.subList(Math.max(0, i - N), i));
            Double ngramProbability = ngramProbabilities.get(ngram);
            wordProbability *= ngramProbability != null ? ngramProbability : 0; // todo: epsilon
        }
        return wordProbability;
    }
}
