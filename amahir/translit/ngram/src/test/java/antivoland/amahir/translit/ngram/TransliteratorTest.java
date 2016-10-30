package antivoland.amahir.translit.ngram;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class TransliteratorTest {
    private static final Logger LOG = LoggerFactory.getLogger(TransliteratorTest.class);
    private static final int N = 3;
    private static final double LAMBDA = 0.1;
    private static final int MAX_DISTANCE = 3;

    private static Transliterator ruLaTransliterator;
    private static Transliterator laRuTransliterator;

    @BeforeClass
    public static void prepare() throws Exception {
        Syllabifier ruSyllabifier = Syllabifiers.ru();
        Syllabifier laSyllabifier = Syllabifiers.la();
        CorpusForecaster ruCorpusForecaster;
        CorpusForecaster laCorpusForecaster;
        try (Words ruWords = Words.ruWordFrequencies(); Words laWords = Words.laWordFrequencies()) {
            ruCorpusForecaster = new CorpusForecaster(ruWords.stream, ruSyllabifier, N);
            laCorpusForecaster = new CorpusForecaster(laWords.stream, laSyllabifier, N);
        }
        laRuTransliterator = createTransliterator(
                laSyllabifier,
                Dictionaries.laRu(),
                laCorpusForecaster,
                ruCorpusForecaster,
                true,
                ValidationSets.laRuKnown(),
                ValidationSets.laRuUnknown());
        LOG.info("LA-RU: model -> {}", laRuTransliterator);
        ruLaTransliterator = createTransliterator(
                ruSyllabifier,
                Dictionaries.ruLa(),
                ruCorpusForecaster,
                laCorpusForecaster,
                false,
                ValidationSets.ruLaKnown(),
                ValidationSets.ruLaUnknown());
        LOG.info("RU-LA: model -> {}", ruLaTransliterator);
    }

    private static Transliterator createTransliterator(
            Syllabifier inputSyllabifier,
            Map<String, List<String>> inputOutputDictionary,
            CorpusForecaster inputCorpusForecaster,
            CorpusForecaster outputCorpusForecaster,
            boolean seekHiddenInputs,
            Map<String, String> inputOuputKnown,
            Map<String, String> inputOuputUnknown) {

        Map<String, String> validationSet = new HashMap<>();
        validationSet.putAll(inputOuputKnown);
        validationSet.putAll(inputOuputUnknown);

        TreeMap<Double, Transliterator> transliterators = new TreeMap<>();
        double inputRate = 0;
        while (inputRate < 1) {
            ForecastStrategy forecastStrategy = new ForecastStrategy(inputRate, 1 - inputRate);
            Transliterator transliterator = new Transliterator(
                    inputSyllabifier,
                    inputOutputDictionary,
                    inputCorpusForecaster,
                    outputCorpusForecaster,
                    forecastStrategy,
                    seekHiddenInputs);

            List<Integer> distances = validationSet.entrySet().stream()
                    .map(e -> {
                        String transliteration = transliterator.transliterate(e.getKey());
                        return LevenshteinDistance.computeLevenshteinDistance(transliteration, e.getValue());
                    }).collect(Collectors.toList());
            transliterators.put((double) distances.stream().collect(Collectors.summingInt(d -> d)) / distances.size(), transliterator);
            inputRate += LAMBDA;
        }
        return transliterators.firstEntry().getValue();
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

    public void testLaRuTransliteration(String name, String expected) {
        String transliteration = laRuTransliterator.transliterate(name);
        int distance = LevenshteinDistance.computeLevenshteinDistance(transliteration, expected);
        LOG.info("LA-RU: {} -> {} ({}, {})", name, transliteration, expected, distance);
        Assert.assertTrue(distance <= MAX_DISTANCE);
    }

    public void testRuLaTransliteration(String name, String expected) {
        String transliteration = ruLaTransliterator.transliterate(name);
        int distance = LevenshteinDistance.computeLevenshteinDistance(transliteration, expected);
        LOG.info("RU-LA: {} -> {} ({}, {})", name, transliteration, expected, distance);
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
