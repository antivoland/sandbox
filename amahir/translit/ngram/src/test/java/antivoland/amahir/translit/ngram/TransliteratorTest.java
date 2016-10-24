package antivoland.amahir.translit.ngram;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransliteratorTest {
    private static final Logger LOG = LoggerFactory.getLogger(TransliteratorTest.class);
    private static final int N = 3;
    private static final int MAX_DISTANCE = 3;

    private Transliterator ruLaTransliterator;
    private Transliterator laRuTransliterator;

    @Before
    public void prepare() throws Exception {
        Syllabifier ruSyllabifier = Syllabifiers.ru();
        Syllabifier laSyllabifier = Syllabifiers.la();
        CorpusForecaster ruCorpusForecaster = new CorpusForecaster(Words.ruWordFrequencies(), ruSyllabifier, N);
        CorpusForecaster laCorpusForecaster = new CorpusForecaster(Words.laWordFrequencies(), laSyllabifier, N);
        laRuTransliterator = new Transliterator(laSyllabifier, Dictionaries.laRu(), laCorpusForecaster, ruCorpusForecaster);
        ruLaTransliterator = new Transliterator(ruSyllabifier, Dictionaries.ruLa(), ruCorpusForecaster, laCorpusForecaster);
    }

    @Test
    public void testRuKnown() {
        LOG.info("Transliterating known russian names");
        testTransliteration("александр", "alexandr", ruLaTransliterator);
        testTransliteration("сергей", "sergey", ruLaTransliterator);
        testTransliteration("елена", "elena", ruLaTransliterator);
        testTransliteration("андрей", "andrey", ruLaTransliterator);
        testTransliteration("алексей", "alexey", ruLaTransliterator);
        testTransliteration("ольга", "olga", ruLaTransliterator);
        testTransliteration("дмитрий", "dmitrij", ruLaTransliterator);
        testTransliteration("татьяна", "tatyana", ruLaTransliterator);
        testTransliteration("ирина", "irina", ruLaTransliterator);
        testTransliteration("инесса", "inessa", ruLaTransliterator);
        testTransliteration("петров", "petrov", ruLaTransliterator);
        testTransliteration("петров", "petroff", ruLaTransliterator);
        testTransliteration("юрий", "jurij", ruLaTransliterator);
    }

    @Test
    public void testRuUnknown() {
        LOG.info("Transliterating unknown russian names");
        testTransliteration("авундий", "avundiy", ruLaTransliterator);
        testTransliteration("плакилла", "plakilla", ruLaTransliterator);
        testTransliteration("усфазан", "usfazan", ruLaTransliterator);
        testTransliteration("феогния", "pheognia", ruLaTransliterator);
        testTransliteration("эпиктет", "epiktet", ruLaTransliterator);
    }

    @Test
    public void testEnKnown() {
        LOG.info("Transliterating known latin names");
        testTransliteration("alexandr", "александр", laRuTransliterator);
        testTransliteration("sergey", "сергей", laRuTransliterator);
        testTransliteration("elena", "елена", laRuTransliterator);
        testTransliteration("andrey", "андрей", laRuTransliterator);
        testTransliteration("alexey", "алексей", laRuTransliterator);
        testTransliteration("olga", "ольга", laRuTransliterator);
        testTransliteration("dmitrij", "дмитрий", laRuTransliterator);
        testTransliteration("tatyana", "татьяна", laRuTransliterator);
        testTransliteration("irina", "ирина", laRuTransliterator);
        testTransliteration("inessa", "инесса", laRuTransliterator);
        testTransliteration("petrov", "петров", laRuTransliterator);
        testTransliteration("petroff", "петров", laRuTransliterator);
        testTransliteration("jurij", "юрий", laRuTransliterator);
    }

    @Test
    public void testEnUnknown() {
        LOG.info("Transliterating unknown russian names");
        testTransliteration("avundiy", "авундий", laRuTransliterator);
        testTransliteration("plakilla", "плакилла", laRuTransliterator);
        testTransliteration("usfazan", "усфазан", laRuTransliterator);
        testTransliteration("pheognia", "феогния", laRuTransliterator);
        testTransliteration("epiktet", "эпиктет", laRuTransliterator);
    }

    public void testTransliteration(String name, String expected, Transliterator transliterator) {
        String transliteration = transliterator.transliterate(name, ForecastStrategy.BOTH);
        int distance = LevenshteinDistance.computeLevenshteinDistance(transliteration, expected);
        LOG.info("{} -> {} ({}, {})", name, transliteration, expected, distance);
        Assert.assertTrue(distance <= MAX_DISTANCE);
    }

    // https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
    private static class LevenshteinDistance {
        private static int minimum(int a, int b, int c) {
            return Math.min(Math.min(a, b), c);
        }

        public static int computeLevenshteinDistance(CharSequence lhs, CharSequence rhs) {
            int[][] distance = new int[lhs.length() + 1][rhs.length() + 1];

            for (int i = 0; i <= lhs.length(); i++)
                distance[i][0] = i;
            for (int j = 1; j <= rhs.length(); j++)
                distance[0][j] = j;

            for (int i = 1; i <= lhs.length(); i++)
                for (int j = 1; j <= rhs.length(); j++)
                    distance[i][j] = minimum(
                            distance[i - 1][j] + 1,
                            distance[i][j - 1] + 1,
                            distance[i - 1][j - 1] + ((lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1));

            return distance[lhs.length()][rhs.length()];
        }
    }
}
