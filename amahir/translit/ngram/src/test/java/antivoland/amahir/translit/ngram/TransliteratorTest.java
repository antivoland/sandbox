package antivoland.amahir.translit.ngram;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class TransliteratorTest {
    private static final Logger LOG = LoggerFactory.getLogger(TransliteratorTest.class);
    private static final int N = 3;
    private static final double[] INPUT_RATE_RANGE = new double[]{0, 1};
    private static final double INPUT_RATE_ALPHA = 1.1; // todo: 0.2
    private static final double[] LENGTH_DIFF_RATE_RANGE = new double[]{0, 100};
    private static final double LENGTH_DIFF_RATE_ALPHA = 100; // todo: 0.2
    private static final int MAX_DISTANCE = 30;

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
                true, // todo: true
                ValidationSets.laRuKnown(),
                "laru.strategies");
        LOG.info("LA-RU: model -> {}", laRuTransliterator);
        ruLaTransliterator = createTransliterator(
                ruSyllabifier,
                Dictionaries.ruLa(),
                ruCorpusForecaster,
                laCorpusForecaster,
                false,
                ValidationSets.ruLaKnown(),
                "rula.strategies");
        LOG.info("RU-LA: model -> {}", ruLaTransliterator);
    }

    private static Transliterator createTransliterator(
            Syllabifier inputSyllabifier,
            Map<String, List<String>> inputOutputDictionary,
            CorpusForecaster inputCorpusForecaster,
            CorpusForecaster outputCorpusForecaster,
            boolean seekHiddenInputs,
            Map<String, String> inputOutputKnown,
            String fileName) throws IOException {

        StringBuilder octave = new StringBuilder();
        // todo: gradient descend?
        TreeMap<Double, Transliterator> transliterators = new TreeMap<>();
        double inputRate = INPUT_RATE_RANGE[0];
        while (inputRate < INPUT_RATE_RANGE[1] + INPUT_RATE_ALPHA / 2) {
            double lengthDiffRate = LENGTH_DIFF_RATE_RANGE[0];
            while (lengthDiffRate < LENGTH_DIFF_RATE_RANGE[1] + LENGTH_DIFF_RATE_ALPHA / 2) {
                ForecastStrategy forecastStrategy = new ForecastStrategy(inputRate, 1 - inputRate, lengthDiffRate);
                Transliterator transliterator = new Transliterator(
                        inputSyllabifier,
                        inputOutputDictionary,
                        inputCorpusForecaster,
                        outputCorpusForecaster,
                        forecastStrategy,
                        seekHiddenInputs);

                List<Double> distances = inputOutputKnown.entrySet().stream()
                        .map(e -> {
                            String transliteration = transliterator.transliterate(e.getKey()).first().joinedOutput();
                            return (double) LevenshteinDistance.computeLevenshteinDistance(transliteration, e.getValue());
                        }).collect(Collectors.toList());

                double mean = distances.stream()
                        .collect(Collectors.summingDouble(d -> d)) / distances.size();
                double std = Math.sqrt(distances.stream()
                        .collect(Collectors.summingDouble(d -> Math.pow(d - mean, 2))) / distances.size());

                transliterators.put(std, transliterator);
                octave.append(String.format("%s %s %s\n", inputRate, lengthDiffRate, std));
                lengthDiffRate += LENGTH_DIFF_RATE_ALPHA;
            }
            inputRate += INPUT_RATE_ALPHA;
        }
        Files.write(Paths.get(fileName), octave.toString().getBytes());
        return transliterators.firstEntry().getValue();
    }

    // todo: remove
//    @Test
    public void test() {
        System.out.println("\n");
        laRuTransliterator.transliterate("inessa")
                .stream().limit(10).forEach(t -> LOG.debug(t.toString()));
        System.out.println("\n");
        laRuTransliterator.transliterate("jurij")
                .stream().limit(10).forEach(t -> LOG.debug(t.toString()));
        System.out.println("\n");
        laRuTransliterator.transliterate("miloslavskaya")
                .stream().limit(10).forEach(t -> LOG.debug(t.toString()));
        System.out.println("\n");
        ruLaTransliterator.transliterate("маресьев")
                .stream().limit(10).forEach(t -> LOG.debug(t.toString()));
        System.out.println("\n");
    }

    @Test
    public void testLaRuKnown() throws Exception {
        testTransliteration(laRuTransliterator, ValidationSets.laRuKnown(), "laru.known.test");
    }

    @Test
    public void testLaRuUnknown() throws Exception {
        testTransliteration(laRuTransliterator, ValidationSets.laRuUnknown(), "laru.unknown.test");
    }

    @Test
    public void testRuLaKnown() throws Exception {
        testTransliteration(ruLaTransliterator, ValidationSets.ruLaKnown(), "rula.known.test");
    }

    @Test
    public void testRuLaUnknown() throws Exception {
        testTransliteration(ruLaTransliterator, ValidationSets.ruLaUnknown(), "rula.unknown.test");
    }

    private static void testTransliteration(Transliterator transliterator, Map<String, String> set, String fileName) throws IOException {
        StringBuilder octave = new StringBuilder();
        StringBuilder stdout = new StringBuilder();
        set.entrySet().stream()
                .forEach(e -> {
                    TreeSet<Transliterator.Transliteration> transliterations = transliterator.transliterate(e.getKey());
                    logTransliterations(e.getKey(), transliterations);

                    Transliterator.Transliteration transliteration = transliterations.first();
                    int distance = LevenshteinDistance.computeLevenshteinDistance(transliteration.joinedOutput(), e.getValue());
                    octave.append(String.join(" ", new String[]{
                                    String.valueOf(e.getKey().length()),
                                    String.valueOf(transliteration.joinedOutput().length()),
                                    String.valueOf(transliteration.inputProbability),
                                    String.valueOf(transliteration.outputProbability),
                                    String.valueOf(distance),
                                    String.valueOf(transliteration.likelihood)
                            })
                    ).append('\n');
                    stdout.append(String.format("%s -> %s (%s, %s, %s)\n",
                            e.getKey(),
                            transliteration.joinedOutput(),
                            e.getValue(),
                            distance,
                            transliteration.likelihood
                    ));
                    Assert.assertTrue(distance <= MAX_DISTANCE);
                });
        Files.write(Paths.get(fileName), octave.toString().getBytes());
        LOG.info(fileName + ":\n" + stdout.toString());
    }

    private static void logTransliterations(String word, TreeSet<Transliterator.Transliteration> transliterations) {
        StringBuilder sb = new StringBuilder();
        transliterations.forEach(t -> sb.append(t).append('\n'));

        Path logDir = Paths.get("log");
        if (!Files.exists(logDir)) try {
            Files.createDirectories(logDir);
        } catch (IOException e) {
            throw new Error(e);
        }
        try {
            Files.write(logDir.resolve(word + ".translit"), sb.toString().getBytes());
        } catch (IOException e) {
            throw new Error(e);
        }
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
