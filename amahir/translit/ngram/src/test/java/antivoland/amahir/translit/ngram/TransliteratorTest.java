package antivoland.amahir.translit.ngram;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransliteratorTest {
    private static final Logger LOG = LoggerFactory.getLogger(TransliteratorTest.class);

    @Test
    public void test() throws Exception {
        Transliterator enRuTransliterator = new Transliterator(Dictionaries.enRu());
        Syllabifier enSyllabifier = Syllabifiers.en();
        Map<Ngram, Double> ruNgramProbabilities = Ngrams.ruNgramProbabilities(3);
        Map<Ngram, Double> enNgramProbabilities = Ngrams.enNgramProbabilities(3);

        Map<List<String>, Double> probab = new HashMap<>();

        List<List<String>> forks = enSyllabifier.syllabify("inessa");
        for (List<String> fork : forks) {
            Double forkProbability = forkProbability(fork, enNgramProbabilities, 3);
            List<List<String>> transliterations = enRuTransliterator.transliterate(fork);
            for (List<String> transliteration : transliterations) {
                Double translProbability = translitProbability(transliteration, ruNgramProbabilities, 3);
                LOG.info("{} -> {}, {}", fork, transliteration, translProbability * forkProbability);
                probab.put(transliteration, translProbability * forkProbability);
            }
        }

        Optional<Map.Entry<List<String>, Double>> max = probab.entrySet().stream()
                .max((x1, x2) -> Double.compare(x1.getValue(), x2.getValue()));
    }

    private Double translitProbability(List<String> translit, Map<Ngram, Double> ngramProbabilities, int N) {
        Double probability = 1.0;
        for (int i = 0; i < translit.size(); ++i) {
            Ngram ngram = new Ngram(translit.subList(Math.max(0, i - N), i));
            Double ngramProbability = ngramProbabilities.get(ngram);
            if (ngramProbability != null) {
                probability *= ngramProbability;
            }
        }
        return probability;
    }

    private Double forkProbability(List<String> fork, Map<Ngram, Double> ngramProbabilities, int N) {
        Double probability = 1.0;
        for (int i = 0; i < fork.size(); ++i) {
            Ngram ngram = new Ngram(fork.subList(Math.max(0, i - N), i));
            Double ngramProbability = ngramProbabilities.get(ngram);
            if (ngramProbability != null) {
                probability *= ngramProbability;
            }
        }
        return probability;
    }

//    @Test
//    public void testRu() throws Exception {
//        LOG.info("Syllabifying russian names");
//        Syllabifier ruSyllabifier = Syllabifiers.ru();
//        testForks("александр", ruSyllabifier, 2);
//        testForks("сергей", ruSyllabifier, 1);
//        testForks("елена", ruSyllabifier, 1);
//        testForks("андрей", ruSyllabifier, 1);
//        testForks("алексей", ruSyllabifier, 2);
//        testForks("ольга", ruSyllabifier, 1);
//        testForks("дмитрий", ruSyllabifier, 2);
//        testForks("татьяна", ruSyllabifier, 1);
//        testForks("ирина", ruSyllabifier, 1);
//        testForks("инесса", ruSyllabifier, 1);
//    }

//    @Test
//    public void testEn() throws Exception {
//        LOG.info("Syllabifying transliterated names");
//        Syllabifier enSyllabifier = Syllabifiers.en();
//        testForks("alexandr", enSyllabifier, 1);
//        testForks("sergey", enSyllabifier, 1);
//        testForks("elena", enSyllabifier, 1);
//        testForks("andrey", enSyllabifier, 1);
//        testForks("alexey", enSyllabifier, 1);
//        testForks("olga", enSyllabifier, 1);
//        testForks("dmitrij", enSyllabifier, 1);
//        testForks("tatyana", enSyllabifier, 2);
//        testForks("irina", enSyllabifier, 1);
//        testForks("inessa", enSyllabifier, 4);
//    }

//    private void testForks(List<String> name, Transliterator transliterator, int expected) {
//        List<List<String>> forks = transliterator.transliterate(name);
//        LOG.info("{} -> {}", name, forks);
//        Assert.assertEquals(expected, forks.size());
//    }
}
