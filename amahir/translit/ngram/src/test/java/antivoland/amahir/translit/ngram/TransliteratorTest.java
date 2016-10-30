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
        CorpusForecaster ruCorpusForecaster;
        CorpusForecaster laCorpusForecaster;
        try (Words ruWords = Words.ruWordFrequencies(); Words laWords = Words.laWordFrequencies()) {
            ruCorpusForecaster = new CorpusForecaster(ruWords.stream, ruSyllabifier, N);
            laCorpusForecaster = new CorpusForecaster(laWords.stream, laSyllabifier, N);
        }
        laRuTransliterator = new Transliterator(
                laSyllabifier,
                Dictionaries.laRu(),
                laCorpusForecaster,
                ruCorpusForecaster,
                true);
        ruLaTransliterator = new Transliterator(
                ruSyllabifier,
                Dictionaries.ruLa(),
                ruCorpusForecaster,
                laCorpusForecaster,
                false);
    }

    @Test
    public void testRuLaKnown() throws Exception {
        LOG.info("Transliterating known russian names");
        ValidationSets.ruLaKnown().entrySet().stream()
                .forEach(e -> testRuLaTransliteration(e.getKey(), e.getValue()));
    }

    @Test
    public void testRuLaUnknown() throws Exception {
        LOG.info("Transliterating unknown russian names");
        ValidationSets.ruLaUnknown().entrySet().stream()
                .forEach(e -> testRuLaTransliteration(e.getKey(), e.getValue()));
    }

    @Test
    public void testLaRuKnown() throws Exception {
        LOG.info("Transliterating known latin names");
        ValidationSets.laRuKnown().entrySet().stream()
                .forEach(e -> testLaRuTransliteration(e.getKey(), e.getValue()));
    }

    @Test
    public void testLaRuUnknown() throws Exception {
        LOG.info("Transliterating unknown latin names");
        ValidationSets.laRuUnknown().entrySet().stream()
                .forEach(e -> testLaRuTransliteration(e.getKey(), e.getValue()));
    }

    public void testRuLaTransliteration(String name, String expected) {
        String transliteration = ruLaTransliterator.transliterate(name, ForecastStrategy.BOTH); // todo: нужны веса для перемножения вероятностей
        int distance = LevenshteinDistance.computeLevenshteinDistance(transliteration, expected);
        LOG.info("RU-LA: {} -> {} ({}, {})", name, transliteration, expected, distance);
        Assert.assertTrue(distance <= MAX_DISTANCE);
    }

    public void testLaRuTransliteration(String name, String expected) {
        String transliteration = laRuTransliterator.transliterate(name, ForecastStrategy.OUTPUT); // todo: нужны веса для перемножения вероятностей
        int distance = LevenshteinDistance.computeLevenshteinDistance(transliteration, expected);
        LOG.info("LA-RU: {} -> {} ({}, {})", name, transliteration, expected, distance);
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
